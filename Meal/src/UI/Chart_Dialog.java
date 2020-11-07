package UI;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import DB.Driver_Connect;
import java.sql.*;

public class Chart_Dialog extends JDialog{

	int kcount = 0; int ccount = 0; int jcount = 0; int ycount = 0 ; int allCount = 0;
	JButton Image_Save = new JButton("차트이미지 저장");
	JButton Close = new JButton("닫기");
	Button_Panel button_panel = new Button_Panel();
	Connection con = Driver_Connect.makeConnection("/meal");
	JDialog jd = this;
	JTabbedPane tab = new JTabbedPane(JTabbedPane.TOP);
	Chart_Panel1 panel1 = new Chart_Panel1(con);
	Chart_Panel2 panel2 = new Chart_Panel2(con);
	Chart_Panel3 panel3 = new Chart_Panel3(con);
	String Title;
	public Chart_Dialog(JFrame jframe) {
		
		super(jframe,"결제별 종류현황",false);
		this.setLayout(new BorderLayout());
		this.add(button_panel,BorderLayout.NORTH);
		this.add(tab,BorderLayout.CENTER);
		tab.addChangeListener(new ChangeListener() {
			
			public void stateChanged(ChangeEvent e) {
				JTabbedPane pane = (JTabbedPane)e.getSource();
				int index = pane.getSelectedIndex();
				
				switch(index) {
				case 0 : 
					Title = "종류별 수익 차트";button_panel.repaint();break;
				case 1 : 
					Title = "종류별 판매량 차트";button_panel.repaint();break;
				case 2 : 
					Title = "시간별 판매량 차트";button_panel.repaint();break;
				}
				
			}
		});
		tab.add("차트1",panel1);
		tab.add("차트2",panel2);
		tab.add("차트3",panel3);
		setVisible(false);
		setSize(560,430);
	}
	
	class Button_Panel extends JPanel{
		
		public Button_Panel() {
			this.setBackground(new Color(255,255,255));
			this.setOpaque(true);
			
			this.setLayout(new FlowLayout(FlowLayout.RIGHT));
			
			this.add(Image_Save);
			Image_Save.addActionListener(new Button_Action());
			this.add(Close);
			Close.addActionListener(new Button_Action());
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			g.setFont(new Font("굴림",Font.BOLD,16));
			g.drawString(Title, 100, 25);
		}
	}
	
	class Button_Action implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			JButton jb = (JButton)e.getSource();
			String text = jb.getText();
			
			switch(text) {
			case "차트이미지 저장" :
			FileDialog fd = new FileDialog(jd,"파일저장",FileDialog.SAVE);
			fd.setVisible(true);        
	        
	        String path = fd.getDirectory() + fd.getFile()+".jpg"; 
	        
	        File Save_Path = new File(path);
	        
	        JPanel panel = (JPanel)tab.getSelectedComponent();
			BufferedImage image = new BufferedImage(panel.getWidth(),panel.getHeight(),BufferedImage.TYPE_INT_RGB);
			panel.paint(image.getGraphics());
			try {
				System.out.println(ImageIO.write(image, "jpg",Save_Path));
			}catch(IOException ex) {
				System.out.println(ex.getMessage());
			}break;
			case "닫기" : 
				close_chart();
			}
			
		}
	}
	
	public void open_chart() {
		
		this.setVisible(true);
			
	}
	
	public void close_chart() {
		this.setVisible(false);
	}

}
