package UI;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.util.Arrays;


public class Chart_Panel1 extends JPanel{
	
	int price[] = new int[13]; int kprice[] = new int[13]; int cprice[] = new int[13]; int jprice[] = new int[13]; int yprice[] = new int[13];
	int kcount[] = new int[13]; int ccount[] = new int[13]; int jcount[] = new int[13]; int ycount[] = new int[13]; 
	int mi = 1;
	JComboBox<String> combo;
	String[] month = {"1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"};
	
		public Chart_Panel1(Connection con) {
			combo = new JComboBox<String>(month);
			this.setLayout(new BorderLayout());
			this.add(combo,BorderLayout.SOUTH);
			this.setBackground(new Color(255,255,255));
			this.setOpaque(true);
			
			try {
				PreparedStatement ps = con.prepareStatement("SELECT *  FROM orderlist");
				
				ResultSet result = ps.executeQuery();
				Arrays.fill(price, 0); Arrays.fill(kprice, 0); Arrays.fill(cprice, 0); Arrays.fill(jprice, 0);Arrays.fill(yprice, 0);
				Arrays.fill(kcount, 0); Arrays.fill(ccount, 0); Arrays.fill(jcount, 0);Arrays.fill(ycount, 0);
				while(result.next()) {
					String orderDate = result.getString("orderDate");
					String month = orderDate.substring(5, 7);
					int to = Integer.parseInt(month);
					int orderCount = result.getInt("orderCount");
					int amount = result.getInt("amount");
					for (int i = 0; i <= 11; i++) {
						if (to == i + 1) {
							price[i] += amount;
						}
					}

					int cuisineNo = Integer.parseInt((String) result.getString("cuisineNo"));
					for (int i = 0; i <= 11; i++) {
						if (to == i + 1) {
							if (cuisineNo == 1) {
								kprice[i] += amount;
								kcount[i] += orderCount;
							} else if (cuisineNo == 2) {
								cprice[i] += amount;
								ccount[i] += orderCount;
							} else if (cuisineNo == 3) {
								jprice[i] += amount;
								jcount[i] += orderCount;
							} else if (cuisineNo == 4) {
								yprice[i] += amount;
								ycount[i] += orderCount;
							}
						}
					}
				}
				repaint();
				this.setVisible(true);
				
			}catch(SQLException ex){
				System.out.println(ex.getMessage());
			}
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			g.setColor(Color.black);
			g.drawLine(50,250,350,250); //
			g.drawLine(50,20,50,250);  //
			g.drawString("(단위 : 만원)",10,20);
			
			for(int num = 1 ;num<11;num++)
			{
				g.drawString(num*10 +"",25,255-20*num); //
				g.drawLine(50, 250-20*num, 350,250-20*num); //
			}
			
			g.drawString("한식",75,270);
			g.drawString("중식",125,270);
			g.drawString("일식",175,270);
			g.drawString("양식",225,270);
			g.drawString("총금액",275,270);
			
		
			//
			combo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					String ms = combo.getSelectedItem().toString();	
					int idx = ms.indexOf("월");	
					String dal = ms.substring(0, idx);	
					mi = Integer.parseInt(dal);
					repaint();
				}
			});
			//combo.setSelectedIndex(1);
			for (int i = 0; i <= 11; i++) {
				if (mi == i+1) {
					
					g.setColor(Color.red);
					g.fillRect(75, 250 - (kprice[i] / 10000) * 2, 20, (kprice[i] / 10000) * 2);

					g.setColor(Color.cyan);
					g.fillRect(125, 250 - (cprice[i] / 10000) * 2, 20, (cprice[i] / 10000) * 2);

					g.setColor(Color.blue);
					g.fillRect(175, 250 - (jprice[i] / 10000) * 2, 20, (jprice[i] / 10000) * 2);

					g.setColor(Color.yellow);
					g.fillRect(225, 250 - (yprice[i] / 10000) * 2, 20, (yprice[i] / 10000) * 2);

					g.setColor(Color.black);
					g.fillRect(275, 250 - (price[i] / 10000) * 2, 20, (price[i] / 10000) * 2);

					g.drawString("한식(" + kcount[i] + ")", 425, 124);
					g.drawString("중식(" + ccount[i] + ")", 425, 154);
					g.drawString("일식(" + jcount[i] + ")", 425, 184);
					g.drawString("양식(" + ycount[i] + ")", 425, 214);

					g.setColor(Color.red);
					g.fillRect(400, 110, 20, 20);

					g.setColor(Color.cyan);
					g.fillRect(400, 140, 20, 20);

					g.setColor(Color.blue);
					g.fillRect(400, 170, 20, 20);

					g.setColor(Color.yellow);
					g.fillRect(400, 200, 20, 20);
				}
			}
		}
	
}
