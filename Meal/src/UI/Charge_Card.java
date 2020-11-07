package UI;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Charge_Card extends JDialog{

	Connection con;
	
	JTextField Price_Amount = new JTextField(6);
	JTextField[] Card_Num = new JTextField[4];
	JTextField[] Varid_Date = new JTextField[2];
	JButton[] Buttons = new JButton[2];
	JComboBox<String> Card_Com;
	Vector<String> Card_Coms = new Vector<String>();
	JComboBox<String> combo = new JComboBox<String>();
	
	Card_Num_Key CNK = new Card_Num_Key();
	Varid_Date_Key VNK = new Varid_Date_Key();
	
	
	public Charge_Card(Connection con) {
		setTitle("카드 결제");
		this.con = con;
		this.setLayout(new GridLayout(4,1));
		this.add(new Price_Area());
		this.add(new Varid_Combo_Area());
		this.add(new Card_Num_Area());
		this.add(new Buttons_Area());
		setSize(400,300);
		setVisible(false);
	}
	
	class Price_Area extends JPanel{
		
		public Price_Area() {
			
			JPanel panel_left = new JPanel();
			panel_left.setLayout(new FlowLayout(FlowLayout.LEFT,10,20));
			JLabel name = new JLabel("충전금액 : ");
			name.setFont(new Font("맑은고딕",Font.PLAIN,18));
			panel_left.add(name);
			Price_Amount.setFont(new Font("맑은고딕",Font.BOLD,20));
			panel_left.add(Price_Amount);
			
			JPanel panel_right = new JPanel();
			panel_right.setLayout(new FlowLayout(FlowLayout.RIGHT,10,20));
			try {
				PreparedStatement ps = con.prepareStatement("SELECT memberNo FROM member");
				
				ResultSet result = ps.executeQuery();
				Vector<String> vector = new Vector<String>();
				
				while(result.next()) {
					vector.add(result.getString("memberNo"));
				}
				
				combo = new JComboBox(vector);
				panel_right.add(combo);
				
			}catch(SQLException ex) {
				System.out.println(ex.getMessage());
			}
			this.add(panel_left);
			this.add(panel_right);
		}
	}
	
	class Card_Num_Area extends JPanel{
		
		public Card_Num_Area() {
			
			this.setLayout(new FlowLayout(FlowLayout.CENTER));
			for(int i = 0; i < 4; i++) {
				Card_Num[i] = new JTextField(4);
				Card_Num[i].setFont(new Font("맑은고딕",Font.BOLD,20));
				Card_Num[i].addKeyListener(CNK);
				Card_Num[i].setHorizontalAlignment(SwingConstants.CENTER);
				this.add(Card_Num[i]);
				
				if(i < 3)
					this.add(new JLabel("-"));
			}
		}
	}
	
	class Varid_Combo_Area extends JPanel{
		
		public  Varid_Combo_Area() {
			
			this.setLayout(new GridLayout(1,2));
			
			Card_Coms.add("NH농협");
			Card_Com = new JComboBox<String>(Card_Coms);
			Card_Com.setFont(new Font("맑은고딕",Font.PLAIN,18));
			JPanel left = new JPanel();
			left.setLayout(new FlowLayout(FlowLayout.LEFT));
			left.add(Card_Com);

			JPanel right = new JPanel();
			right.setLayout(new FlowLayout(FlowLayout.RIGHT));
			right.add(Varid_Date[0] = new JTextField("MM",2));
			Varid_Date[0].setFont(new Font("맑은고딕",Font.PLAIN,18));
			Varid_Date[0].addMouseListener(new Varid_Mouse());
			Varid_Date[0].addKeyListener(VNK);
			
			right.add(new JLabel("/"));
			
			right.add(Varid_Date[1] = new JTextField("YY",2));
			Varid_Date[1].setFont(new Font("맑은고딕",Font.PLAIN,18));
			Varid_Date[1].addMouseListener(new Varid_Mouse());
			Varid_Date[1].addKeyListener(VNK);
			
			this.add(left);
			this.add(right);
		}
		
	} 
	
	class Buttons_Area extends JPanel{
		
		
		public Buttons_Area() {
			
			String[] texts = {"결제","취소"};
			String[] images = {"식권발매//식권발매//DataFiles/master.png","식권발매//식권발매//DataFiles/visa.jpg"};
			for(int i = 0; i < Buttons.length; i++) {
				
				Buttons[i] = new JButton(texts[i]);
				Buttons[i].setFont(new Font("맑은고딕",Font.BOLD,18));
				Buttons[i].addActionListener(new Buttons_Action());
				this.add(Buttons[i]);
				
			}
			
			for(int i = 0; i < Buttons.length; i++) {
				
				ImageIcon icon = new ImageIcon(images[i]);
				Image image  =(icon.getImage()).getScaledInstance(90, 50, Image.SCALE_SMOOTH);
				ImageIcon icon2 = new ImageIcon(image);
				JLabel Image_Label = new JLabel(icon2);
				this.add(Image_Label);
				
			}
			
		}
	}
	
	class Varid_Mouse extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			JTextField f = (JTextField)e.getSource();
			f.setText("");
		}
	} 
	
	class Price_Key extends KeyAdapter{
		
		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			super.keyReleased(e);
		}
	}
	class Card_Num_Key extends KeyAdapter{
		
		@Override
		public void keyReleased(KeyEvent e) {
			
			JTextField f = (JTextField)e.getSource();
			String text = f.getText();
			
			try {
				Integer.parseInt(text);
				if(text.length() == 4) {
					
					for(int i = 0; i < Card_Num.length-1; i++) {
						
						if(f == Card_Num[i]) 
							Card_Num[i+1].requestFocus();
					}
				}
				else if(text.length() >= 5){
					for(int i = 0; i < Card_Num.length; i++) {
						
						if(f == Card_Num[i] && i != 3) {	
							text = text.substring(0,4);
							f.setText(text);
							Card_Num[i+1].requestFocus();
						}
						else if(f == Card_Num[i] && i == 3) {
							text = text.substring(0,4);
							f.setText(text);
						}
							
					}
				}
			}catch(NumberFormatException ex) {
				if(!text.equals("")) {
					JOptionPane.showMessageDialog(null, "숫자만 입력하여주세요","경고",JOptionPane.WARNING_MESSAGE);
					text = text.substring(0,text.length()-1);
					f.setText(text);
				}
			}
		}
		
	}
	
	class Varid_Date_Key extends KeyAdapter{
		
		@Override
		public void keyReleased(KeyEvent e) {
			
			JTextField f = (JTextField)e.getSource();
			String text = f.getText();
			
			try {
				Integer.parseInt(text);
				if(text.length() == 2) {
						
					if(f == Varid_Date[0]) {
						Varid_Date[1].requestFocus();	
						Varid_Date[1].setText("");
					}
				}
				else if(text.length() >= 3){
					for(int i = 0; i < Card_Num.length; i++) {
						
						if(f== Varid_Date[0]) {	
							text = text.substring(0,2);
							f.setText(text);
							Varid_Date[1].requestFocus();
							Varid_Date[1].setText("");
						}
						else {
							text = text.substring(0,2);
							f.setText(text);
						}
							
					}
				}
			}catch(NumberFormatException ex) {
				if(!text.equals("")) {
					JOptionPane.showMessageDialog(null, "숫자만 입력하여주세요","경고",JOptionPane.WARNING_MESSAGE);
					text = text.substring(0,text.length()-1);
					f.setText(text);
				}
			}
		}
		
	}
	
	
	class Buttons_Action implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			JButton button = (JButton)e.getSource();
			String text = button.getText();
			
			switch(text) {
			case "결제" :
				
				if(blank_test()) 
					JOptionPane.showMessageDialog(null, "빈칸을 모두 채워주세요","경고",JOptionPane.WARNING_MESSAGE);
				else if(card_num_test())
					JOptionPane.showMessageDialog(null, "카드번호가 올바르지 않습니다.","경고",JOptionPane.WARNING_MESSAGE);
				else if(varid_test()) 
					JOptionPane.showMessageDialog(null, "유효기간이 올바르지 않습니다.","경고",JOptionPane.WARNING_MESSAGE);
				else if(price_test())
					JOptionPane.showMessageDialog(null, "가격을 숫자로 입력하세요.","경고",JOptionPane.WARNING_MESSAGE);
				else {
					try {
						String memberNo = (String)combo.getSelectedItem();
						PreparedStatement  ps = con.prepareStatement("Select point from member where memberNo = '"  
						+ memberNo + "'");
						
						ResultSet result = ps.executeQuery();
						result.next();
						int point = Integer.parseInt(result.getString("point"));
						point += Integer.parseInt(Price_Amount.getText());
						result.close();
						
						 ps = con.prepareStatement("Update member Set point = " + point + " where memberNo = '" + memberNo + "'" );
						 
						 int re = ps.executeUpdate();
						 if(re == 1) {
							 ps = con.prepareStatement("Select memberName, point from member where memberNo = '" + memberNo + "'");
							 result  = ps.executeQuery();
							 result.next();
							 String name = result.getString("memberName");
							 String points = result.getString("point");
							 JOptionPane.showMessageDialog(null, "충전이 완료되었습니다. " 
									 + name + "님의 현재 잔액은 " + points + "point 입니다.","완료",JOptionPane.INFORMATION_MESSAGE);
							 Change_Visible(false);
						 }
						 
					}catch(SQLException ex) {
						System.out.println(ex.getMessage());
					}
				}break;
			case "취소" : 
				Change_Visible(false);
			}
			
		}
		
	}
	public void Change_Visible(boolean bol) {
		this.setVisible(bol);
	}
	
	public boolean blank_test() {
		
		boolean test = false;
		
		if(Price_Amount.getText().equals(""))
			test = true;
		
		for(int i = 0; i < Card_Num.length; i++) {
			if(Card_Num[i].getText().equals(""))
				test = true;
			
			if(test)
				break;
		}
		
		for(int i = 0; i < Varid_Date.length; i++) {
			if(Varid_Date[i].getText().equals(""))
				test = true;
			
			if(test)
				break;
		}
		
		return test;
		
	}
	
	public boolean card_num_test() {
		
		int sum = 0;
		int last_num = 0;
		try {
			for(int i = 0; i < Card_Num.length; i++) {
				String num = Card_Num[i].getText();
				int index1 = Integer.parseInt(num.charAt(0)+"") * 2;
				int index3 = Integer.parseInt(num.charAt(2)+"") * 2;
				int index2 = Integer.parseInt(num.charAt(1)+"");
				int index4 = Integer.parseInt(num.charAt(3)+"");
				
				sum +=  (index1 / 10) + (index1 % 10); 
				
				sum +=  (index3 / 10) + (index3 % 10);
				
				sum += index2;
				
				if(i != Card_Num.length-1) sum += index4;
				else 	last_num = index4;
			}
			
			if(last_num == 10 - (sum % 10))
				return false;
			else
				return true;
		}catch(StringIndexOutOfBoundsException ex) {
			return true;
		}
	} 
	
	public boolean varid_test() {
		
		String month = Varid_Date[0].getText();
		String year = Varid_Date[1].getText();
		
		if(month.equals("MM") || year.equals("YY")) {
			return true;
		}
		try {
			if(Integer.parseInt(month) > 12 ||
					Integer.parseInt(month) < 0) {
				return true;
			}
		}catch(NumberFormatException ex) {
			return true;
		}
		
		return false;
	}
	
	public boolean price_test() {
	
		try {
			Integer.parseInt(Price_Amount.getText());
		}catch(NumberFormatException ex) {
			return true;
		}
		
		return false;
	}
	
}
