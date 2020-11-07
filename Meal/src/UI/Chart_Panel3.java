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

//규형이 월별차트 --> OrderData 클래스랑, TableDialog 까지 저거 3개입니다!!

public class Chart_Panel3 extends JPanel{
	int lunch = 0; int dinner = 0; int breakfast = 0; ; int allCount = 0;
	JButton Image_Save = new JButton("차트이미지 저장");
	JButton Close = new JButton("닫기");
	
	JButton dayButton = new JButton("월"); //버튼

	//콤보박스에 사용될 월 리스트
	private static String[] monthList = {
		"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
	private String month; //콤보박스에 선택된 아이템을 할당할 변수
	JComboBox<String> dayText = new JComboBox<String>(monthList); //콤보박스 생성 및 리스트 초기화

	private List orderDataList;
	
	JButton luch_food= new JButton("점심");
	JButton dinner_food = new JButton("저녁");

	TableDialog tableDialog;

	JComboBox<String> combo;
	
	Connection con;
	JFrame jframe;
	
		public Chart_Panel3(Connection con) {
			this.setBackground(new Color(255,255,255));
			this.setLayout(null);
			
			this.con = con;
			this.add(luch_food);//버튼 생성 후 위치 고
			luch_food.addActionListener(new Button_Action());
			luch_food.setBounds(455, 106,55,30);
			luch_food.setSize(60,30);
			this.add(dinner_food);//
			dinner_food.addActionListener(new Button_Action());
			dinner_food.setBounds(455, 136,55,30);
			dinner_food.setSize(60,30);
					
			this.add(dayText);//
			dayText.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					search();
				}
			});;
			dayText.setBounds(385, 16,55,30);

			
			open_chart(Integer.parseInt((String)dayText.getSelectedItem()));
			this.add(dayButton);
			
			dayButton.addActionListener(new Button_Action()); //버튼요~
			dayText.setPreferredSize(new Dimension(70, 25)); //치는 택박
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			int sum = 0;
			
			g.drawString("점심("+ lunch + ")",405,124);
			g.drawString("저녁("+ dinner + ")",405,154);
			
			g.setColor(Color.red);
			g.fillRect(380, 110, 20, 20);

			int rect = 0;

			try {
				rect = 360 * lunch / allCount;
			} catch (ArithmeticException e) {
				e.printStackTrace();
			}

			g.fillArc(50, 10, 300, 300, 0, rect);
			sum = rect;
			
			g.setColor(Color.blue);
			g.fillRect(380, 140, 20, 20);
			rect = 360-sum;
			g.fillArc(50, 10, 300, 300, sum, rect);			

		}
	
	
	class Button_Action implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
						
			Object o = e.getSource();
			String text = "";
			if (o instanceof JButton) {
				JButton jb = (JButton) o;
				text = jb.getText();
			}							
			
			switch(text) {
			case "점심" :
				tableDialog = new TableDialog(jframe, Integer.parseInt(month), 0);
				break;//
			case "저녁" :
				tableDialog = new TableDialog(jframe, Integer.parseInt(month), 1);
				break;//
			}
			
		}
	}
	
	/**
	 * 월별 orderlist를 조회하는 함수
	 */
	private void search() {
		month = (String)dayText.getSelectedItem();
		open_chart(Integer.parseInt(month));
	}
	
	public void open_chart(int month) {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT date_format(orderDate,'%h') FROM orderlist"
					+ " WHERE MONTH(orderDate) = " + month); //month 받아오기
			ResultSet result = ps.executeQuery();
			lunch = 0 ; dinner = 0; breakfast = 0; ; allCount = 0;

			while(result.next()) {
				int TimeNo = Integer.parseInt((String)result.getString(1));
				switch(TimeNo) {
						
				case 05 :  //오후 5 시
				case 06 :  //오후 6시
				case 07 :  //오후 7시
				case 8 : dinner++; break;	//오후 8시
				case 11 : ; // 오전 11시
			    case 12 :  //오전 12시
				case 1 :  //오전 1시
				case 2 : lunch++; break; //오전2시
		
				}
				allCount++;
			}
			allCount = allCount - breakfast;

			repaint();
			this.setVisible(true);

		} catch(SQLException ex){
			System.out.println(ex.getMessage());
		}
	}
	public void close_chart() {
		this.setVisible(false);
	}

}
