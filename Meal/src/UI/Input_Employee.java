package UI;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import DB.Driver_Connect;

public class Input_Employee extends JDialog{

	Connection con;
	JLabel[] labels = new JLabel[4];
	String[] text = {"사원번호 : ","*사 원 명 : ","*패스워드 : ","*패스워드 재입력 : "};
	String correct_text = "";
	JTextField jtf[] = new JTextField[2];
	JPasswordField jpf[] = new JPasswordField[2];
	Last_Label last = new Last_Label(text[3]);
	JButton Input_Button = new JButton("등록");
	JButton Close_Button = new JButton("닫기");
	Button_Action action = new Button_Action();
	

	public Input_Employee(JFrame jframe, Connection con) {
		super(jframe,"신규사원 등록",true);
		this.con = con;
		this.setLayout(new GridLayout(5,2));
		
		for(int i = 0; i < 4; i++) {
			labels[i] = new JLabel(text[i]);
			if(i == 3) {
				labels[i] = last;
			}
		}
			
		for(int i = 0; i < 4; i++) {
			this.add(labels[i]);
			
			if(i < 2) {
				this.add(jtf[i] = new JTextField());
			}else {
				this.add(jpf[i%2] = new JPasswordField());
				jpf[i%2].addKeyListener(new Input_Action());
			}
		}
		
		jtf[0].setEnabled(false);
		
		
		this.add(Input_Button);
		this.add(Close_Button);
		
		Input_Button.addActionListener(action);
		Close_Button.addActionListener(action);
		
		Container c = getContentPane();
		
		setVisible(false);
		setSize(400,300);
		c.setFocusable(true);
		c.requestFocus();
		
	}
	
	class Last_Label extends JLabel{
		
		public Last_Label(String text) {
			super(text);
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			if(correct_text.equals("일치")) {
				g.setColor(Color.BLUE);
			}else {
				g.setColor(Color.RED);
			}
			g.drawString(correct_text, 100, 30);
		}
	}
	class Button_Action implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			
			JButton button = (JButton)e.getSource();
			String text = button.getText();
			
			char[] ch1 = jpf[0].getPassword();
			String pw1 = "";
			for(char chr : ch1) pw1 += Character.toString(chr);
			
			char[] ch2 = jpf[1].getPassword();
			String pw2 = "";
			for(char chr : ch2) pw2 += Character.toString(chr);
			
			switch(text) {
			case "등록" :
				if(jtf[1].getText().equals("") || pw1.equals("") || pw2.equals("") ) {
					JOptionPane.showMessageDialog(null,"필수항목(*)누락","Message",JOptionPane.ERROR_MESSAGE);
				}else if(correct_text.equals("불일치")) {
					JOptionPane.showMessageDialog(null,"패스워드 확인 요망","Message",JOptionPane.ERROR_MESSAGE);
				}else{
					JOptionPane.showMessageDialog(null,"사원등록이 완료되었습니다.","Message",JOptionPane.INFORMATION_MESSAGE);
					try{
						PreparedStatement ps = con.prepareStatement("INSERT INTO member VALUES(?,?,?,?)");
						ps.setString(1, jtf[0].getText());ps.setString(2, jtf[1].getText());ps.setString(3, pw2);ps.setString(4, "0");
						
						ps.executeUpdate();
						Close();
					}catch(SQLException ex) {
						System.out.println(ex.getMessage());
					}
				}break;
			case "닫기" : Close(); break;
			}
			
		}
	}
	
	class Input_Action extends KeyAdapter{
		@Override
		public void keyReleased(KeyEvent e) {
			char[] ch1 = jpf[0].getPassword();
			String pw1 = "";
			for(char chr : ch1) pw1 += Character.toString(chr);
			
			char[] ch2 = jpf[1].getPassword();
			String pw2 = "";
			for(char chr : ch2) pw2 += Character.toString(chr);
			
			
			if(!pw1.equals("")) {
				
				if(pw1.equals(pw2)) {
					correct_text = "일치";
				}else {
					correct_text = "불일치";
				}
			}else {
				correct_text = "";
			}
			last.repaint();
		}
	}
	
	public void Open() {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM member");
			ResultSet result = ps.executeQuery();
			result.next();
			int memberNo = Integer.parseInt(result.getString("COUNT(*)"))+10001;
			jtf[0].setText(""+memberNo);
			jtf[1].setText("");jpf[0].setText("");jpf[1].setText("");
		}catch(SQLException ex){
			System.out.println(ex.getMessage());
		}
		this.setVisible(true);
	}
	
	public void Close() {
		this.setVisible(false);
	}
}
