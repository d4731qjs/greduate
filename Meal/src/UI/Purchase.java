package UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import DB.Driver_Connect;
import java.util.*;
import java.text.*;
import java.sql.*;

public class Purchase extends JDialog {

	JFrame jframe;
	JLabel Title = new JLabel("", SwingConstants.CENTER);
	JLabel Price = new JLabel("0원");
	Vector<JButton> Buttons = new Vector<JButton>();
	Connection con;
	PurChase_Cancel_Action PCA = new PurChase_Cancel_Action();
	
	JTable Table;
	JComboBox combo;
	JPasswordField PW_Field;
	Message_Panel message;
	
	Vector<Vector<String>> data = new Vector<Vector<String>>();
	Vector<String> col = new Vector<String>();
	JTextField Amount_Field = new JTextField();
	JTextField Product_Field = new JTextField();

	JButton[] Cal_Buttons = new JButton[14];
	int cuisineNo;
	
	Left_Foods_Area LFA = new Left_Foods_Area();
	Right_UI_Area RUA = new Right_UI_Area();

	int total_Price = 0;

	public Purchase(JFrame jframe, Connection con) {
		super(jframe, "결제", true);
		this.jframe = jframe;
		this.con = con;
		PW_Field = new JPasswordField();
		message = new Message_Panel();
		this.setLayout(new BorderLayout());
		this.add(new Top_Area(), BorderLayout.NORTH);
		this.add(new Center_Area(), BorderLayout.CENTER);
		setSize(950, 650);
		setVisible(false);
	}

	class Top_Area extends JPanel {

		public Top_Area() {
			this.setLayout(new BorderLayout());
			this.add(Title, BorderLayout.CENTER);
			Title.setVerticalAlignment(SwingConstants.BOTTOM);
			Title.setFont(new Font("돋음", Font.BOLD, 24));
			Title.setVerticalAlignment(SwingConstants.CENTER);
			this.add(new Top_Area_Price(), BorderLayout.SOUTH);
		}
	}

	class Top_Area_Price extends JPanel {

		public Top_Area_Price() {
			this.setLayout(new FlowLayout(FlowLayout.RIGHT));
			this.add(Price);
			Price.setFont(new Font("돋음", Font.PLAIN, 22));
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setFont(new Font("돋음", Font.BOLD, 22));
			g.drawString("총결제금액:", 650, 30);
		}
	}

	class Center_Area extends JPanel {

		public Center_Area() {
			this.setLayout(null);
			LFA.setSize(550, 520);
			LFA.setLocation(10, 10);
			RUA.setSize(355, 520);
			RUA.setLocation(570, 10);
			this.add(LFA);
			this.add(RUA);
		}

	}

	class Left_Foods_Area extends JPanel {

		public Left_Foods_Area() {
		}

		public void changeLayout() {
			try {
				switch (Title.getText()) {
				case "한식":
					cuisineNo = 1;
					break;
				case "중식":
					cuisineNo = 2;
					break;
				case "일식":
					cuisineNo = 3;
					break;
				case "양식":
					cuisineNo = 4;
					break;
				}

				PreparedStatement ps = con.prepareStatement("SELECT * FROM meal WHERE cuisineNo = " + cuisineNo);
				ResultSet result = ps.executeQuery();

				for (int i = 0; i < Buttons.size(); i++) {
					this.remove(Buttons.get(i));
				}

				Buttons.removeAllElements();

				while (result.next()) {
					JButton jbutton = new JButton("<html><center>" + result.getString("mealName") + "<br><br>"
							+ result.getString("price") + "원</center>");
					if (Integer.parseInt(result.getString("maxCount")) < 1
							|| Integer.parseInt(result.getString("todayMeal")) != 1) {
						jbutton.setEnabled(false);
					}
					jbutton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							JButton jbutton = (JButton) e.getSource();
							String text = jbutton.getText();
							text = text.substring(14, text.indexOf("<br>"));
							Product_Field.setText(text);
						}
					});
					Buttons.add(jbutton);

				}

			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}

			int row = Buttons.size() / 5;
			row = Buttons.size() % 5 != 0 ? row + 1 : row;
			this.setLayout(new GridLayout(row, 5));

			for (int i = 0; i < Buttons.size(); i++) {
				this.add(Buttons.get(i));
				Buttons.get(i).setHorizontalAlignment(SwingConstants.CENTER);
			}
		}
	}

	class Right_UI_Area extends JPanel {

		public Right_UI_Area() {
			this.setLayout(new GridLayout(2, 1, 0, 20));
			this.add(new Table_Area());
			this.add(new Bottom_Area());
		}
	}

	class Table_Area extends JPanel {

		public Table_Area() {
			this.setLayout(new BorderLayout(0, 10));

			col.add("상품번호");
			col.add("품명");
			col.add("수량");
			col.add("금액");
			DefaultTableModel model = new DefaultTableModel(data,col);
			Table = new JTable(model);
			JScrollPane scroll = new JScrollPane(Table);
			
			Table.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					
					if (e.getClickCount() == 2) {
						int row = Table.getSelectedRow();
						
						String name = (String)Table.getValueAt(row, 1);
						int row_Price = Integer.parseInt((String)Table.getValueAt(row,3));
						
						for (int i = 0; i < Buttons.size(); i++) {
							String Button_Name = Buttons.get(i).getText();
							Button_Name = Button_Name.substring(14, Button_Name.indexOf("<br>"));
							if (name.equals(Button_Name)) {
								Buttons.get(i).setEnabled(true);
								break;
							}
						}
						
						total_Price -= row_Price;
						Price.setText(total_Price+"원");
						data.removeElementAt(row);
						Table.updateUI();
					}
				}
			});

			JPanel panel = new JPanel();
			JLabel 선택품명 = new JLabel("선택품명 :");
			JLabel 수량 = new JLabel("수량 : ");

			Product_Field.setPreferredSize(new Dimension(190, 25));
			Amount_Field.setPreferredSize(new Dimension(50, 25));
			Product_Field.setBackground(null);
			Amount_Field.setBackground(null);
			Product_Field.setFont(new Font("돋음", Font.BOLD, 10));
			Product_Field.setEnabled(false);
			Amount_Field.setEnabled(false);

			panel.add(선택품명);
			panel.add(Product_Field);
			panel.add(수량);
			panel.add(Amount_Field);

			this.add(scroll, BorderLayout.CENTER);
			this.add(panel, BorderLayout.SOUTH);
		}
	}

	class Bottom_Area extends JPanel {

		public Bottom_Area() {
			this.setLayout(new BorderLayout(0, 5));

			this.add(new Calcu_Area(), BorderLayout.CENTER);

			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(1, 2));
			panel.add(Cal_Buttons[12] = new JButton("결제"));
			panel.add(Cal_Buttons[13] = new JButton("취소"));
			panel.setSize(200, 200);

			this.add(panel, BorderLayout.SOUTH);
			
			Cal_Buttons[12].addActionListener(PCA);
			Cal_Buttons[13].addActionListener(PCA);
			
		}
	}

	class Calcu_Area extends JPanel {

		public Calcu_Area() {
			this.setLayout(new BorderLayout());
			JPanel Left_Panel = new JPanel();
			Left_Panel.setLayout(new BorderLayout());
			JPanel Num_Pad = new JPanel();
			Num_Pad.setLayout(new GridLayout(3, 3));
			for (int i = 1; i <= 9; i++) {
				Num_Pad.add(Cal_Buttons[i] = new JButton("" + i));
				Cal_Buttons[i].setPreferredSize(new Dimension(100, 100));
				Cal_Buttons[i].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						JButton jbutton = (JButton) e.getSource();
						String amount = Amount_Field.getText();
						if (amount.length() < 2) {
							amount = amount + jbutton.getText();
						}

						if (Integer.parseInt(amount) > 10) {
							Amount_Field.setText(amount);
							JOptionPane.showMessageDialog(null, "10개 이상 주문은 제한됩니다.", "경고", JOptionPane.ERROR_MESSAGE);
							Amount_Field.setText("");
						} else {
							Amount_Field.setText(amount);
						}
					}
				});
			}
			Left_Panel.add(Num_Pad, BorderLayout.CENTER);
			Left_Panel.add(Cal_Buttons[0] = new JButton("0"), BorderLayout.SOUTH);
			Cal_Buttons[0].setPreferredSize(new Dimension(0, 50));
			Cal_Buttons[0].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					JButton jbutton = (JButton) e.getSource();
					String amount = Amount_Field.getText();
					if (amount.length() < 2) {
						amount = amount + jbutton.getText();
					}

					if (Integer.parseInt(amount) > 10) {
						Amount_Field.setText(amount);
						JOptionPane.showMessageDialog(null, "10개 이상 주문은 제한됩니다.", "경고", JOptionPane.ERROR_MESSAGE);
						Amount_Field.setText("");
					} else {
						Amount_Field.setText(amount);
					}
				}
			});

			JPanel Input_Panel = new JPanel();
			Input_Panel.setLayout(new BorderLayout(0, 1));
			Input_Panel.add(Cal_Buttons[10] = new JButton("입력"), BorderLayout.CENTER);
			Input_Panel.add(Cal_Buttons[11] = new JButton("초기화"), BorderLayout.SOUTH);
			Cal_Buttons[11].setPreferredSize(new Dimension(80, 50));
			Cal_Buttons[10].addActionListener(new Input_Button_Action());
			Cal_Buttons[11].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					total_Price = 0;
					Price.setText("0원");

					for (int i = 0; i < data.size(); i++) {
						String name = data.get(i).get(1);
						for (int j = 0; j < Buttons.size(); j++) {
							String Button_Name = Buttons.get(j).getText();
							Button_Name = Button_Name.substring(14, Button_Name.indexOf("<br>"));
							if (name.equals(Button_Name)) {
								Buttons.get(j).setEnabled(true);
								break;
							}
						}
					}

					data.removeAllElements();
					Table.updateUI();

					Amount_Field.setText("");
					Product_Field.setText("");
				}
			});

			this.add(Left_Panel, BorderLayout.CENTER);
			this.add(Input_Panel, BorderLayout.EAST);
		}
	}

	
	
	class Message_Panel extends JPanel{
		
		public Message_Panel() {
			
			try {
				PreparedStatement ps = con.prepareStatement("SELECT memberNo FROM member");
				
				ResultSet result = ps.executeQuery();
				Vector<String> vector = new Vector<String>();
				
				while(result.next()) {
					vector.add(result.getString("memberNo"));
				}
				
				combo = new JComboBox(vector);
				this.setLayout(new GridLayout(2,2));
				this.add(new JLabel("사원번호",SwingConstants.CENTER));
				this.add(combo);
				this.add(new JLabel("패스워드",SwingConstants.CENTER));
				this.add(PW_Field);
				
			}catch(SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
	
	/*--------------------------------------------------------------------------------------------------------------------*/
	 
	class Input_Button_Action implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (Product_Field.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "품명을 선택해주세요", "경고", JOptionPane.ERROR_MESSAGE);
			} else if (Amount_Field.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "수량을 입력해주세요", "경고", JOptionPane.ERROR_MESSAGE);
			} else {
				try {
					PreparedStatement ps = con
							.prepareStatement("SELECT * FROM meal WHERE mealName = '" + Product_Field.getText() + "'");

					ResultSet result = ps.executeQuery();

					result.next();
					int maxCount = Integer.parseInt(result.getString("maxCount"));
					if (maxCount < Integer.parseInt(Amount_Field.getText())) {
						JOptionPane.showMessageDialog(null, "10개 이상 주문은 제한됩니다.", "경고", JOptionPane.ERROR_MESSAGE);
					} else {
						Vector<String> vector = new Vector<String>();
						vector.add(result.getString("mealNo"));
						vector.add(Product_Field.getText());
						vector.add(Amount_Field.getText());
						String price = Integer.toString(
								Integer.parseInt(Amount_Field.getText()) * Integer.parseInt(result.getString("price")));
						vector.add(price);
						data.add(vector);
						Table.updateUI();
						total_Price = 0;
						DefaultTableModel model = (DefaultTableModel) Table.getModel();
						for (int i = 0; i < model.getRowCount(); i++)
							total_Price += Integer.parseInt((String) model.getValueAt(i, 3));

						Price.setText(total_Price + "원");

						for (int i = 0; i < Buttons.size(); i++) {
							String text = Buttons.get(i).getText();
							text = text.substring(14, text.indexOf("<br>"));

							if (text.equals(Product_Field.getText())) {
								Buttons.get(i).setEnabled(false);
								Product_Field.setText("");
								Amount_Field.setText("");
								break;
							}
						}
					}
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		}
	}
	
	class PurChase_Cancel_Action implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			
			JButton button = (JButton)e.getSource();
			String text = button.getText();
			
			switch(text) {
			case "결제" :
				if(Table.getRowCount() != 0) {
					combo.setSelectedIndex(0);
					while(true) {
					
						PW_Field.setText("");
				
						int re = JOptionPane.showConfirmDialog(null, message,"결제자 인증",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
					
						if(re == JOptionPane.OK_OPTION) {
						int memNo = Integer.parseInt((String)combo.getSelectedItem());
						char[] ch = PW_Field.getPassword();
						String memPW = "";
						
						for(char chr : ch) {
							memPW += chr;
						}
						
						
						try {
							PreparedStatement ps = con.prepareStatement("SELECT * FROM member WHERE memberNo = " + memNo + " AND passwd = '" 
						+ memPW + "'");
							
							ResultSet result = ps.executeQuery();
							
							if(result.next()) {
								
								ps = con.prepareStatement("Select point from member where memberNo = '" + memNo + "'");
								
								result = ps.executeQuery();
								result .next();
								int point = Integer.parseInt(result.getString("point"));
								result.close();
								
								point -= total_Price;
								
								if(point < 0) {
									JOptionPane.showMessageDialog(null,"포인트가 모자릅니다.","Message",JOptionPane.ERROR_MESSAGE);
									break;
								}
								
								ps = con.prepareStatement("Update member Set point = " + point + " Where memberNo = '" + memNo + "'");
								ps.executeUpdate();
						
								
							}else {
								JOptionPane.showMessageDialog(null,"패스워드가 일치하지 않습니다.","Message",JOptionPane.ERROR_MESSAGE);
							}
							
							
								SimpleDateFormat format1 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
								Calendar cal = Calendar.getInstance();
								String date = format1.format(cal.getTime());
								
								for(int i = 0; i < Table.getRowCount(); i++) {
									
									ps = con.prepareStatement("SELECT COUNT(*) FROM orderlist");
									result = ps.executeQuery();
									result.next();
									int orderNo = Integer.parseInt(result.getString("COUNT(*)"))+1;
									
									String mealNo = (String)Table.getValueAt(i, 0);
									String orderCount = (String)Table.getValueAt(i, 2);
									String amount = (String)Table.getValueAt(i, 3);
									
									ps = con.prepareStatement("INSERT INTO orderlist VALUES(?,?,?,?,?,?,?)");
									ps.setString(1, Integer.toString(orderNo));
									ps.setString(2,Integer.toString(cuisineNo));
									ps.setString(3,mealNo);
									ps.setString(4,Integer.toString(memNo));
									ps.setString(5,orderCount);
									ps.setString(6,amount);
									ps.setString(7,date);
									
									ps.executeUpdate();
									
									
								}
								JOptionPane.showMessageDialog(null,"결제가 완료되었습니다.\n식권을 출력합니다.","Message",JOptionPane.INFORMATION_MESSAGE);
								
								
								for (int i = 0; i < data.size(); i++) {
									String name = data.get(i).get(1);
									for (int j = 0; j < Buttons.size(); j++) {
										String Button_Name = Buttons.get(j).getText();
										Button_Name = Button_Name.substring(14, Button_Name.indexOf("<br>"));
										if (name.equals(Button_Name)) {
											Buttons.get(j).setEnabled(true);
											break;
										}
									}
								}
								
								close_Purchase();
								PrintForm printform = new PrintForm(Table,memNo,cuisineNo);
								
								data.removeAllElements();
								Table.updateUI();
								total_Price = 0;
								Price.setText("0원");
								
								
								break;
							
						
						}catch(SQLException ex) {
							System.out.println(ex.getMessage());
						}
					
					}
					else {
						break;
					}
				}
				}else {
					JOptionPane.showMessageDialog(null, "메뉴를 선택하세요","Message",JOptionPane.ERROR_MESSAGE);
				};break;
			case "취소" : 
				close_Purchase();break;
			}
		}
	}

	/*--------------------------------------------------------------------------------------------------------------------*/
	public void open_Purchase(String menu) {
		Title.setText(menu);
		LFA.changeLayout();
		Amount_Field.setText("");
		Product_Field.setText("");
		this.setVisible(true);
	}
	
	public void close_Purchase() {
		this.setVisible(false);
	}
}

