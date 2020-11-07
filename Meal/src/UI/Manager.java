package UI;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Manager extends JFrame{
	JButton[] buttons = new JButton[5];
	String[] text = {"메뉴등록","메뉴관리","결제조회","종류별 차트","종 료"};
	Button_Action button_action = new Button_Action();
	Input_Menu input_menu;
	Menu_Kanri menu_kanri;
	Purchase_Info purchase_info;
	Chart_Dialog chart_dialog;
	public Manager() {
		
		setTitle("관리화면");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.add(new Buttons(),BorderLayout.NORTH);
		c.add(new Picture(),BorderLayout.CENTER);
		
		input_menu = new Input_Menu(this);
		menu_kanri = new Menu_Kanri(this);
		purchase_info = new Purchase_Info(this);
		chart_dialog = new Chart_Dialog(this);
		setVisible(true);
		setSize(600,500);
	}
	
	class Buttons extends JPanel{
		
		public Buttons() {
			for(int i=0; i < 5; i++) {
				buttons[i] = new JButton(text[i]);
				buttons[i].addActionListener(button_action);
				this.add(buttons[i]);
			}
		}
	}
	
	class Picture extends JPanel{
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			ImageIcon icon = new ImageIcon("식권발매//식권발매//DataFiles//식당.jpg");
			Image img = icon.getImage();
			g.drawImage(img, 30, 15,this.getWidth()-60,this.getHeight()-30, this);
		}
	}
	
	class Button_Action implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton jb = (JButton)e.getSource();
			String name = jb.getText();
			
			switch(name) {
			case "메뉴등록" : input_menu.setVisible(true);break;
			case "메뉴관리" : menu_kanri.open_kanri();break;
			case "결제조회" : purchase_info.setVisible(true);break;
			case "종류별 차트" : chart_dialog.open_chart();break;
			case "종 료" : System.exit(0);break;
			}	
		}
	}	
}