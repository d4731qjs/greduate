package UI;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import DB.Driver_Connect;
import java.sql.*;

public class Chart_Panel2 extends JPanel{
	int kcount = 0; int ccount = 0; int jcount = 0; int wcount = 0 ; int allCount = 0;
	
	JButton K_food= new JButton("한식");// 각 버튼 생
	JButton C_food = new JButton("중식");//
	JButton J_food = new JButton("일식");//
	JButton W_food = new JButton("양식");// 
	
	 Panel2_Dialog1 chart_dialog1 = new Panel2_Dialog1();//chart_dialog 생성
	 Panel2_Dialog2 chart_dialog2 = new Panel2_Dialog2();;//
	 Panel2_Dialog3 chart_dialog3 = new Panel2_Dialog3();;//
	 Panel2_Dialog4 chart_dialog4 = new Panel2_Dialog4();;//
	
	Connection con;
	
		public Chart_Panel2(Connection con) {
			this.con = con;
			this.setBackground(new Color(255,255,255));
			this.setOpaque(true);
			this.setLayout(null);
			
			this.add(K_food);//버튼 생성 후 위치 고
			K_food.addActionListener(new Button_Action());
			K_food.setBounds(425, 106,75,30);
			K_food.setSize(90,30);
			this.add(C_food);//
			C_food.addActionListener(new Button_Action());
			C_food.setBounds(425, 136,75,30);
			C_food.setSize(90,30);
			this.add(J_food);//
			J_food.addActionListener(new Button_Action());
			J_food.setBounds(425, 166,75,30);
			J_food.setSize(90,30);
			this.add(W_food);//
			W_food.addActionListener(new Button_Action());
			W_food.setBounds(425, 196,75,30);
			W_food.setSize(90,30);
			
			try {
				PreparedStatement ps = con.prepareStatement("SELECT cuisineNo , sum(orderCount) AS CNT FROM orderlist  GROUP BY cuisineNo");
				
				ResultSet result = ps.executeQuery();
				kcount = 0; ccount = 0; jcount = 0; wcount = 0; allCount = 0;
				
				while(result.next()) {
					int cuisineNo = Integer.parseInt((String)result.getString("cuisineNo"));
					int CNT = Integer.parseInt((String)result.getString("CNT"));
					switch(cuisineNo) {
						case 1 : kcount = CNT; break;
						case 2 : ccount = CNT; break;
						case 3 : jcount = CNT; break;
						case 4 : wcount = CNT; break;
					}
					allCount = kcount + ccount  + jcount  + wcount ;
				}
				
				repaint();
				this.setVisible(true);
				
			}catch(SQLException ex){
				System.out.println(ex.getMessage());
			}
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			int sum = 0;
			K_food.setText("한식("+ kcount + ")"); //버튼 텍스트 변
			C_food.setText("중식("+ccount + ")");
			J_food.setText("일식("+ jcount + ")");
			W_food.setText("양식("+ wcount + ")");

			g.drawString("",425,124);
			g.drawString("",425,154);
			g.drawString("",425,184);
			g.drawString("",425,214);
			
			g.setColor(Color.red);
			g.fillRect(400, 110, 20, 20);
			int Rect = 360*kcount/allCount;
			g.fillArc(50, 10, 300, 300, 0, Rect);
			sum += Rect;
			
			g.setColor(Color.cyan);
			g.fillRect(400, 140, 20, 20);
			Rect = 360*ccount/allCount;
			g.fillArc(50, 10, 300, 300, sum, Rect);
			sum += Rect;
			
			g.setColor(Color.blue);
			g.fillRect(400, 170, 20, 20);
			Rect = 360*jcount/allCount;
			g.fillArc(50, 10, 300, 300, sum, Rect);
			sum += Rect;
			
			g.setColor(Color.yellow);
			g.fillRect(400, 200, 20, 20);
			Rect = 360-sum;
			g.fillArc(50, 10, 300, 300, sum, Rect);

		}
	
	
	class Button_Action implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			JButton jb = (JButton)e.getSource();
			String text ="";
			
		    text = jb.getText().substring(0, 2); 
		    
			switch(text) {
			case "한식" : 
				 chart_dialog1.open_chart();break;//
			case "중식" : 
				 chart_dialog2.open_chart();break;//
			case "일식" :  
				 chart_dialog3.open_chart();break;//
			case "양식" : 
				 chart_dialog4.open_chart();break;//
			}
			
		}
	}
	
	public void close_chart() {
		this.setVisible(false);
	}

}
