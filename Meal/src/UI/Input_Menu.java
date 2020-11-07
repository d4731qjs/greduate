package UI;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import DB.Driver_Connect;

public class Input_Menu extends JDialog{

	Connection con = Driver_Connect.makeConnection("/meal");
	JTextField jtf = new JTextField();
	JButton Input_Button = new JButton("등록");
	JButton Close_Button = new JButton("닫기");
	Button_Action action = new Button_Action();
	JComboBox<String> Menu_Combo = new JComboBox<String>();
	JComboBox<Integer> Price_Combo = new JComboBox<Integer>();
	JComboBox<Integer> Amount_Combo = new JComboBox<Integer>();
	public Input_Menu(JFrame jframe) {
		super(jframe,"신규 메뉴 등록",true);
		
		this.setLayout(new GridLayout(5,2));
		JLabel[] labels = new JLabel[4];
		String[] text = {"종류","*메뉴명","가격","조리가능수량"};
		String[] menu_Text = {"한식","중식","일식","양식"};
		
		for(int i = 0; i < 4; i++) {
			labels[i] = new JLabel(text[i]);
		}
		
		for(int i = 0; i < 4; i++) {
			Menu_Combo.addItem(menu_Text[i]);
		}
		
		for(int i = 1000; i <= 12000; i+=500) {
			Price_Combo.addItem(i);
		}
		
		for(int i = 0; i <= 50; i++) {
			Amount_Combo.addItem(i);
		}
		
		for(int i = 0; i < 4; i++) {
			this.add(labels[i]);
			
			switch(i) {
				case  0 :this.add(Menu_Combo);break; 
				case  1 :this.add(jtf);break; 
				case  2 :this.add(Price_Combo);break; 
				case  3 :this.add(Amount_Combo);break;
			}
		}
		
		this.add(Input_Button);
		this.add(Close_Button);
		
		Input_Button.addActionListener(action);
		Close_Button.addActionListener(action);
		setVisible(false);
		setSize(400,200);
		
	}
	
	class Button_Action implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			
			JButton button = (JButton)e.getSource();
			String text = button.getText();
			
			switch(text) {
			case "등록" :
				if(!jtf.getText().equals("")) {
					try {
						PreparedStatement ps = con.prepareStatement("SELECT * FROM meal WHERE mealName = '" + jtf.getText() + "'");
						ResultSet result = ps.executeQuery();
					if(!result.next()) {
							ps = con.prepareStatement("SELECT COUNT(*) FROM meal");
						
							result = ps.executeQuery();
							result.next();
							int mealNo = Integer.parseInt(result.getString("COUNT(*)"))+1;
							
							 ps = con.prepareStatement("INSERT INTO meal VALUES(?,?,?,?,?,?)");
							 ps.setString(1, Integer.toString(mealNo));
							 
							 int cuisineNo = 0;
							 switch((String)Menu_Combo.getSelectedItem()) {
							 case "한식" :
								 cuisineNo = 1;break;
							 case "중식" :
								 cuisineNo = 2;break;
							 case "일식" :
								 cuisineNo = 3;break;
							 case "양식" :
								 cuisineNo = 4;break;
							 }
							 ps.setString(2,Integer.toString(cuisineNo));
							 ps.setString(3,jtf.getText());
							 ps.setString(4,Integer.toString((int)Price_Combo.getSelectedItem()));
							 ps.setString(5,Integer.toString((int)Amount_Combo.getSelectedItem()));
							 ps.setString(6,"1");
							
							 int re = ps.executeUpdate();
							 if(re != 0) {
								 JOptionPane.showMessageDialog(null, "메뉴가 등록되었습니다!","등록 성공",JOptionPane.INFORMATION_MESSAGE);
								 jtf.setText("");
								 Menu_Combo.setSelectedIndex(0);
								 Amount_Combo.setSelectedIndex(0);
								 Price_Combo.setSelectedIndex(0);
							 }
						}else {
							JOptionPane.showMessageDialog(null, "메뉴명이 중복됩니다!","등록 실패",JOptionPane.ERROR_MESSAGE);
						}
					}catch(SQLException ex) {
						System.out.println(ex.getMessage());
					}
					
				}else {
					JOptionPane.showMessageDialog(null, "메뉴명을 입력하세요!","등록 실패",JOptionPane.ERROR_MESSAGE);
				}break;
				
			case "닫기" :
				jtf.setText("");
				Switch_Visible(false);
			}
			
		}
	}
	
	public void Switch_Visible(boolean bol) {
		this.setVisible(bol);
	}
	
}
