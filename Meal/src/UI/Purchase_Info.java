package UI;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.*;
import javax.swing.table.*;
import DB.Driver_Connect;
import java.sql.*;
import java.text.MessageFormat;
import java.util.*;

public class Purchase_Info extends JDialog{
	
	String cuisine[] = {"한식","중식","일식","양식"};
	
	JTable table = null;
	Vector<String> col = new Vector<String>();
	Vector<Vector<String>> data = new Vector<Vector<String>>();
	Connection con;
	PreparedStatement ps;
	Button_Action button_action = new Button_Action();
	

	JTextField jtf;
	
	public Purchase_Info(JFrame jframe) {
		super(jframe,"결제조회",true);
		this.setLayout(new BorderLayout());
		Toolbar top = new Toolbar();
		top.setFloatable(false);
		add(top,BorderLayout.NORTH);
		
		con = Driver_Connect.makeConnection("/meal");
		col.add("종류");col.add("메뉴명");col.add("사원명");col.add("결제수량");col.add("총결제금액");col.add("결제일");
		DefaultTableModel  model  = new DefaultTableModel(data,col);
		table = new JTable(model);
		
		JScrollPane sp = new JScrollPane(table);
		
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcmSchedule = table.getColumnModel();
		for (int i = 0; i < tcmSchedule.getColumnCount(); i++) {
			tcmSchedule.getColumn(i).setCellRenderer(tScheduleCellRenderer);
		}
		add(sp,BorderLayout.CENTER);
		//customer_fix = new Customer_Fix(jframe,table,data);
		
		setSize(700,700);
		setVisible(false);
	}
	
	class Toolbar extends JToolBar{
		JButton[] jb = new JButton[4];
		String[] text = {"조회","전체보기","인쇄","닫기"};
		public Toolbar() {
			this.setLayout(new FlowLayout(FlowLayout.CENTER));
			JLabel label = new JLabel("메뉴명");
			jtf = new JTextField(15);
			jtf.setMaximumSize(jtf.getPreferredSize());
			add(label); add(jtf);
			for(int i = 0; i < jb.length; i++) {
				jb[i] = new JButton(text[i]);
				jb[i].addActionListener(button_action);
				add(jb[i]);
			}
		}
	}
	
	class Button_Action implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			
			JButton jb = (JButton)e.getSource();
			String name = jb.getText();
			
				switch(name) {
				case "조회" :  
					select(jtf.getText());break;
					
				case "전체보기" :
					jtf.setText("");
					select("");break;
				
				case "인쇄" : 
					
					MessageFormat header = new MessageFormat("결제 현황");
					MessageFormat footer = new MessageFormat("page");
					try {
						table.print(JTable.PrintMode.FIT_WIDTH,header,footer);
					} catch (PrinterException ex) {
						System.out.println(ex.getMessage());
					}
					break;
					
				case "닫기" :
					invisible();break;
					
				}
		}
	}
	
	public void select(String text) {
		try {
			ps = con.prepareStatement("SELECT * FROM meal WHERE mealName LIKE '%" + text + "%'" );
			ResultSet result = ps.executeQuery();
			data.removeAllElements();
			
			while(result.next()) {
				int mealNo = Integer.parseInt(result.getString("mealNo"));
				String mealName = result.getString("mealName");
				
				ps = con.prepareStatement("SELECT * FROM orderlist WHERE mealNo = " + mealNo );
				ResultSet result2 = ps.executeQuery();
				
				while(result2.next()) {
					Vector<String> vector = new Vector<String>();
					vector.add(cuisine[Integer.parseInt(result2.getString("cuisineNo"))-1]);
					vector.add(mealName);
					ps = con.prepareStatement("SELECT * FROM member WHERE memberNo = '" + result2.getString("memberNo") + "'");
					ResultSet result3 = ps.executeQuery();
					result3.next();
					vector.add(result3.getString("memberName"));
					vector.add(result2.getString("orderCount"));
					vector.add(result2.getString("amount"));
					vector.add(result2.getString("orderDate"));
					data.add(vector);
				}
			}

			table.updateUI();
			
		}catch(SQLException e){
			System.out.println(e.getMessage());
		}
	}
	
	public void invisible() {
		this.setVisible(false);
	}
}
