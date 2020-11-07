package UI;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

import javax.swing.*;

import DB.Driver_Connect;

import java.util.*;

public class Main extends JFrame{
	JButton[] buttons = new JButton[5];
	String[] text = {"사용자","관리자","사원등록","포인트 충전","종료"};
	
	Button_Action button_action = new Button_Action();
	
	JPasswordField JPF = new JPasswordField();
	
	Manager_Action MA = new Manager_Action();
	
	Input_Employee input_employee;
	
	Connection con = Driver_Connect.makeConnection("/meal");
	
	Charge_Point Charge;
	public Main() {
		
		setTitle("메인");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		input_employee = new Input_Employee(this, con);
		
		Container c = getContentPane();
		c.setLayout(new GridLayout(5,1));
		
		for(int i = 0; i < 5; i++) {
			buttons[i] = new JButton(text[i]);
			c.add(buttons[i]);
			buttons[i].addActionListener(button_action);
		}
		
		JPF.setEnabled(false);
		JPF.setFont(new Font("Arial",Font.BOLD,14));
		
		Charge = new Charge_Point(con);
		
		setSize(250,300);
		setVisible(true);
	}
	
	
	class Button_Action implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton jb = (JButton)e.getSource();
			String name = jb.getText();
			
			switch(name) {
			case "사용자" : 
				new Customer(con); dispose(); break;
			case "관리자" :
				JPF.setText("");
				int result = JOptionPane.showConfirmDialog(null,new Manager_PW_Panel(),"관리자 패스워드를 입력하세요", JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
				if(result == JOptionPane.OK_OPTION) {
					char[] ch = JPF.getPassword();
					String pw = "";
					for(char chr : ch) pw += Character.toString(chr);
					
					if(pw.equals("0000")) {
						new Manager();
					}else {
						JOptionPane.showMessageDialog(null, "비밀번호를 확인하세요","비밀번호 틀림",JOptionPane.ERROR_MESSAGE);
					}
				}break;
				
			case "사원등록" :
				input_employee.Open();break;
				
			case "포인트 충전" : 
				Charge.Change_Visible(true);break;
			case "종료" :
				dispose();
			}
		}
	}  
	
	class Manager_PW_Panel extends JPanel{
		
		public Manager_PW_Panel() {
			this.setLayout(new BorderLayout());
			JPanel Add_Panel = new JPanel();
			Add_Panel.setLayout(new GridLayout(4,1,0,0));
			JPanel[] Button_Panel = new JPanel[4];
			Vector<Integer> nums = new Vector<Integer>();
			
			for(int i = 0; i < Button_Panel.length; i++){
				Button_Panel[i] = new JPanel();
				Button_Panel[i].setLayout(new GridLayout(1,3));
			}
			
			for(int i = 1; i <= 9; i++) {
				
				while(true) {
					int num = (int)(Math.random()*10);
				
					if(nums.contains(num)) {
						continue;
					}
					JButton button  = new JButton(Integer.toString(num));
					button.addActionListener(MA);
					Button_Panel[(i-1)/3].add(button);
					nums.add(num);
					break;
				}
			}
			while(true) {
				int num = (int)(Math.random()*10);
				if(nums.contains(num)) {
					continue;
				}
				JButton jb = new JButton(Integer.toString(num));
				Button_Panel[3].add(jb);
				jb.addActionListener(MA);
				nums.add(num);
				break;
			}
			
			for(int i = 0; i < Button_Panel.length; i++){
				Add_Panel.add(Button_Panel[i]);
			}

			this.add(JPF ,BorderLayout.NORTH);
			this.add(Add_Panel,BorderLayout.CENTER);
			
		}
		
	}
	
	class Manager_Action implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton jb = (JButton)e.getSource();
			String text = jb.getText();
			char[] ch = JPF.getPassword();
			String pw = "";
			for(char chr : ch) pw += Character.toString(chr);
			JPF.setText(pw+text);
		}
	}
	
	public static void main(String[] args) {
		new Main();
	}
}
