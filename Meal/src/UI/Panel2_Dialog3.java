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
import java.util.ArrayList;

public class  Panel2_Dialog3 extends JDialog{

	int count1= 0; int count2= 0; int count3 = 0; int count4= 0 ; int allCount = 0;
	int order_cnt =0; String mealname_list_1=""; String mealname_list_2=""; String mealname_list_3="";
	
	JButton Image_Save = new JButton("차트이미지 저장");
	JButton Close = new JButton("닫기");
	Button_Panel button_panel = new Button_Panel();
	Chart_Panel chart_panel = new Chart_Panel();
	Connection con = Driver_Connect.makeConnection("/meal");
	JDialog jd = this;
	public  Panel2_Dialog3() {
		
		this.setTitle("판매량 차트");
		this.setLayout(new BorderLayout());
		this.add(button_panel,BorderLayout.NORTH);
		this.add(chart_panel,BorderLayout.CENTER);
	
		setVisible(false);
		setSize(540,400);
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
			g.drawString("일식 판매량 TOP3 차트", 100, 25);
		}
	}
	class Chart_Panel extends JPanel{
		public Chart_Panel() {
			this.setBackground(new Color(255,255,255));
			this.setOpaque(true);
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			int sum = 0;
			
			g.drawString("1위 "+ mealname_list_1 +"(" + count1 + ")",425,124);
			g.drawString("2위 "+ mealname_list_2 +"(" + count2 + ")",425,154);
			g.drawString("3위 "+ mealname_list_3 +"(" + count3 + ")",425,184);
			g.drawString("기타("+ count4 + ")",425,214);
			
			g.setColor(Color.red);
			g.fillRect(400, 110, 20, 20);
			int Rect = 360*count1/allCount;
			g.fillArc(50, 10, 300, 300, 0, Rect);
			sum += Rect;
			
			g.setColor(Color.cyan);
			g.fillRect(400, 140, 20, 20);
			Rect = 360*count2/allCount;
			g.fillArc(50, 10, 300, 300, sum, Rect);
			sum += Rect;
			
			g.setColor(Color.blue);
			g.fillRect(400, 170, 20, 20);
			Rect = 360*count3/allCount;
			g.fillArc(50, 10, 300, 300, sum, Rect);
			sum += Rect;
			
			g.setColor(Color.yellow);
			g.fillRect(400, 200, 20, 20);
			Rect = 360-sum;
			g.fillArc(50, 10, 300, 300, sum, Rect);

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
				        
						BufferedImage image = new BufferedImage(chart_panel.getWidth(),chart_panel.getHeight(),BufferedImage.TYPE_INT_RGB);
						chart_panel.paint(image.getGraphics());
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
		
		try {
			ArrayList<String> mealnum_list= new ArrayList<String> ();       
	        ArrayList<String> mealname_list= new ArrayList<String> ();      
		    ArrayList<String> sum_order_list= new ArrayList<String> ();     
			
			PreparedStatement ps = con.prepareStatement("SELECT sum(orderCount) AS CNT FROM orderlist WHERE cuisineNo=3");
			ResultSet result = ps.executeQuery();
			allCount = 0;
			while(result.next()) {
					//System.out.println((String)result.getString("CNT"));
					int all_cnt = Integer.parseInt((String)result.getString("CNT"));
					order_cnt= all_cnt;// 한중일양 별 전체 주문
			}
		
			PreparedStatement ps1 = con.prepareStatement("SELECT mealNo, sum(orderCount) AS SUM_ORDER FROM orderlist WHERE cuisineNo=3 GROUP BY mealNo ORDER BY sum(orderCount) desc limit 3");
			ResultSet result1= ps1.executeQuery();
			while(result1.next()) {
					//System.out.println((String)result1.getString("mealNo"));
					//System.out.println((String)result1.getString("SUM_ORDER"));
					mealnum_list.add((String)result1.getString("mealNo"));
					sum_order_list.add((String)result1.getString("SUM_ORDER"));
			}
			for(int i=0; i<mealnum_list.size(); i++) {
					PreparedStatement ps2 = con.prepareStatement("SELECT mealName FROM meal WHERE mealNo= '"+mealnum_list.get(i)+"' ");
					ResultSet result2 = ps2.executeQuery();
					while(result2.next()) {
							//System.out.println(result2.getString("mealName"));
							mealname_list.add(result2.getString("mealName"));
					}
		    }
		  
			
			count1 =  Integer.parseInt(sum_order_list.get(0));
			count2 =   Integer.parseInt(sum_order_list.get(1));
			count3 =  Integer.parseInt(sum_order_list.get(2));
			count4 =  order_cnt - (count1  + count2  + count3 );
			mealname_list_1=mealname_list.get(0);
			mealname_list_2=mealname_list.get(1);
			mealname_list_3=mealname_list.get(2);
			allCount = order_cnt;
		

			chart_panel.repaint();
			this.setVisible(true);
			
		}catch(SQLException ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public void close_chart() {
		this.setVisible(false);
	}

}
