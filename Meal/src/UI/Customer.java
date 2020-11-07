package UI;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

import javax.swing.*;
import java.util.*;
import java.text.*;

public class Customer extends JFrame{
	
	JButton[] buttons = new JButton[4];
	String[] text = {"사용자","관리자","사원등록","종료"};
	String[] url = {"식권발매//식권발매//DataFiles//menu_1.png",
			"식권발매//식권발매//DataFiles//menu_2.png",
			"식권발매//식권발매//DataFiles//menu_3.png",
			"식권발매//식권발매//DataFiles//menu_4.png"};
	String[] menus = {"한식","중식","일식","양식"};
	Purchase purchase;
	JLabel label = new JLabel("식권 발매 프로그램",SwingConstants.CENTER);
	Button_Action Button_action = new Button_Action();
	
	public Customer(Connection con) {
		
		setTitle("식권 발매 프로그램");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container c = getContentPane();
		
		label.setFont(new Font("돋음",Font.BOLD,40));
		
		JTabbedPane tab = new JTabbedPane(JTabbedPane.TOP);
		
		tab.add("메뉴",new Button_Area());
		
		c.add(label,BorderLayout.NORTH);
		c.add(tab,BorderLayout.CENTER);
		Time_Area time = new Time_Area();
		Thread thread = new Thread(time);
		c.add(time,BorderLayout.SOUTH);
		thread.start();
		
		purchase = new Purchase(this,con);
		
		setSize(600,800);
		setVisible(true);
	}
	
	class Button_Area extends JPanel{
		
		public Button_Area() {
			this.setLayout(new GridLayout(2,2));
			
			for(int i = 0; i < 4; i++) {
				ImageIcon icon = new ImageIcon(url[i]);
				buttons[i] = new JButton(icon);
				this.add(buttons[i]);
				buttons[i].setToolTipText(menus[i]);
				buttons[i].addActionListener(Button_action);
			}
		}
	}
	
	class Time_Area extends JPanel implements Runnable{
		
		JLabel label = new JLabel("ㅋㅋ");
		public Time_Area() {
			this.setBackground(Color.black);
			label.setFont(new Font("돋음",Font.BOLD,20));
			label.setForeground(Color.CYAN);
			label.setOpaque(true);
			label.setBackground(Color.black);
			this.add(label);
		}
		
		@Override
		public void run() {
			while(true) {
				SimpleDateFormat format1 = new SimpleDateFormat ("현재시간 : yyyy년 MM월 dd일 HH시 mm분 ss초");
				Calendar cal = Calendar.getInstance();
				String date = format1.format(cal.getTime());
				label.setText(date);
				try {
					Thread.sleep(1000);
				}catch(InterruptedException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	class Button_Action implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton)e.getSource();
			String text = button.getToolTipText();
			purchase.open_Purchase(text);
			
		}
	}
}
