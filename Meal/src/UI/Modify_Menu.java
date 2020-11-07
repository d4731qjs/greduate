package UI;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import DB.Driver_Connect;

public class Modify_Menu extends JDialog{

	Connection con = Driver_Connect.makeConnection("/meal");
	JTextField jtf = new JTextField();
	JButton Input_Button = new JButton("수정");
	JButton Close_Button = new JButton("닫기");
	Button_Action action = new Button_Action();
	JComboBox<String> Menu_Combo = new JComboBox<String>();
	JComboBox<Integer> Price_Combo = new JComboBox<Integer>();
	JComboBox<Integer> Amount_Combo = new JComboBox<Integer>();
	public Modify_Menu(JFrame jframe) {
		super(jframe,"메뉴 수정",true);
		
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
		jtf.setEnabled(false);
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
			case "수정" :
				
					try {
							int cuisineNo = Menu_Combo.getSelectedIndex()+1;
							int price = (int)Price_Combo.getSelectedItem();
							int amount = (int)Amount_Combo.getSelectedItem();
							System.out.println(jtf.getText());
							PreparedStatement ps = con.prepareStatement("UPDATE meal SET cuisineNo = " + cuisineNo + ", price = " + price + 
									", maxCount =" + amount + " Where mealName = '"  + jtf.getText() + "'");
							 int re = ps.executeUpdate();
							 if(re != 0) {
								 JOptionPane.showMessageDialog(null, "메뉴가 수정되었습니다!","수정 성공",JOptionPane.INFORMATION_MESSAGE);
								 Switch_Visible(false);
							 }
					}catch(SQLException ex) {
						System.out.println(ex.getMessage());
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
	
	public void Open_Modify(String Menu_Name) {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM meal WHERE mealName = '" + Menu_Name + "'");
			ResultSet result = ps.executeQuery();
			
			result.next();
			
			jtf.setText(Menu_Name);
			Menu_Combo.setSelectedIndex(Integer.parseInt(result.getString("cuisineNo"))-1);
			Price_Combo.setSelectedItem(Integer.parseInt(result.getString("price")));
			Amount_Combo.setSelectedItem(Integer.parseInt(result.getString("maxCount")));
			
		}catch(SQLException ex) {
			System.out.println(ex.getMessage());
		}
		this.setVisible(true);
	}
	
}
