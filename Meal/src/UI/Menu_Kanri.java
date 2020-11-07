package UI;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import DB.Driver_Connect;

import java.sql.*;
import java.util.*;

public class Menu_Kanri extends JDialog{
	enum Status { SELECTED, DESELECTED, INDETERMINATE }
	JTable table = null;
	Vector<Object> col = new Vector<Object>();
	Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	Connection con = Driver_Connect.makeConnection("/meal");
	PreparedStatement ps;
	Button_Action button_action = new Button_Action();
	JComboBox<String> Menu_Combo = new JComboBox<String>();
	JFrame jframe;
	Modify_Menu modify_menu;
	
	public Menu_Kanri(JFrame jframe) {
		super(jframe,"메뉴 관리",true);
		this.jframe = jframe;
		modify_menu = new Modify_Menu(jframe);
		this.setLayout(new BorderLayout());
		Toolbar top = new Toolbar();
		top.setFloatable(false);
		add(top,BorderLayout.NORTH);
		
		
		col.add(Status.INDETERMINATE);col.add("menuName");col.add("price");col.add("maxCount");col.add("todayMeal");
		
	
		DefaultTableModel model = new DefaultTableModel(data, col) {

	        @Override
	        public Class<?> getColumnClass(int columnIndex) {
	            if (columnIndex == 0) {
	                return Boolean.class;
	            } else {
	                return String.class;
	            }
	        }
	    };
	    
		table = new JTable(model);
		JTableHeader header = table.getTableHeader();
		
		//헤더 클릭시 전체 선택
		header.addMouseListener(new Header_Action());
        
		JScrollPane sp = new JScrollPane(table);
		
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcmSchedule = table.getColumnModel();
		for (int i = 1; i < tcmSchedule.getColumnCount(); i++) {
			tcmSchedule.getColumn(i).setCellRenderer(tScheduleCellRenderer);
		}
		add(sp,BorderLayout.CENTER);
			
		setSize(500,600);
		setVisible(false);
	}
	
	class Toolbar extends JToolBar{
		JButton[] jb = new JButton[4];
		String[] text = {"수정","삭제","오늘의메뉴 선정","닫기"};
		public Toolbar() {
			this.setLayout(new FlowLayout(FlowLayout.CENTER));
			JLabel label = new JLabel("종류");
			Menu_Combo.addItem("한식");Menu_Combo.addItem("중식");Menu_Combo.addItem("일식");Menu_Combo.addItem("양식");
			Menu_Combo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int index = Menu_Combo.getSelectedIndex();
					select(index+1);
				}
			});
			add(label); add(Menu_Combo);
			
			
			
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
			try {
				switch(name) {

				case "수정" : 
					
					int row = 0;
					int b_count = 0;
					
					for(int i = 0; i <table.getRowCount(); i++) {
						boolean b = (boolean)table.getValueAt(i, 0);
						if(b) {
							b_count++;
							row = i;
						}
						if(b_count > 1) break;
					}
					
					if(b_count == 1) {
						modify_menu.Open_Modify((String)table.getValueAt(row, 1));
						select(Menu_Combo.getSelectedIndex()+1);
					}else if(b_count > 1){
						JOptionPane.showMessageDialog(null, "메뉴를 한개만 선택해주세요!","수정 실패",JOptionPane.ERROR_MESSAGE);
					}else{
						JOptionPane.showMessageDialog(null, "메뉴를 한개 선택해주세요!","수정 실패",JOptionPane.ERROR_MESSAGE);
					}break;
					
				case "삭제" :
					b_count = 0;
					
					for(int i = 0; i <table.getRowCount(); i++) {
						boolean b = (boolean)table.getValueAt(i, 0);
						if(b) {
							b_count++;
							row = i;
						}
						if(b_count > 1) break;
					}
						int result = JOptionPane.showConfirmDialog(null, "선택한 메뉴를 정말로 삭제하시겠습니까?", "메뉴삭제",
								JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
						if(result == JOptionPane.OK_OPTION) {
							int row_Count = table.getRowCount();
							Vector<Vector<Object>> index_vector = new Vector<Vector<Object>>();
							
							for(int i = 0; i <row_Count; i++) {
								boolean b = (boolean)table.getValueAt(i, 0);
								
								if(b) {
									String row_name = (String)table.getValueAt(i, 1);
									ps = con.prepareStatement("DELETE FROM meal WHERE mealName = '" + row_name + "'");
									int re = ps.executeUpdate();
									if(re != 0) {
										index_vector.add(data.get(i));
									}
								}
							}
							
							for(int i = 0; i < index_vector.size(); i++) {
								data.remove(index_vector.get(i));
							}
							
							table.updateUI();
							JOptionPane.showMessageDialog(null,"삭제되었습니다.","삭제완료",JOptionPane.INFORMATION_MESSAGE);
						}
					break;
					
				case "오늘의메뉴 선정" :
					b_count = 0;
					
					for(int i = 0; i <table.getRowCount(); i++) {
						boolean b = (boolean)table.getValueAt(i, 0);
						if(b) b_count++;
					}
					System.out.println(b_count);
					if(b_count >= 1 && b_count <= 25) {
						result = JOptionPane.showConfirmDialog(null, "선택한 메뉴로 선정하시겠습니까?", "메뉴선정",
								JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
						if(result == JOptionPane.OK_OPTION) {
							int row_Count = table.getRowCount();
							
							for(int i = 0; i <row_Count; i++) {
								boolean b = (boolean)table.getValueAt(i, 0);
								int value;
								
								if(b) value = 1; else value = 0;
								
								String mealName = (String)table.getValueAt(i, 1);
								ps = con.prepareStatement("Update meal SET todayMeal = "+ value +" WHERE mealName = '" + mealName +"'");
								ps.executeUpdate();
							}
							
							select(Menu_Combo.getSelectedIndex()+1);
							JOptionPane.showMessageDialog(null,"선정되었습니다.","선정완료",JOptionPane.INFORMATION_MESSAGE);
						}
					}else if(b_count > 25){
						JOptionPane.showMessageDialog(null,"25개 초과는 제한됩니다.","선정실패",JOptionPane.INFORMATION_MESSAGE);
					};break;
				case "닫기" :
					invisible();break;
				}
			}catch(SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
	
	class Header_Action extends MouseAdapter{
		@Override 
		public void mouseClicked(MouseEvent e) {
	        JTableHeader header = (JTableHeader)e.getSource();
	        JTable table = header.getTable();
	        TableColumnModel columnModel = table.getColumnModel();
	        int vci = columnModel.getColumnIndexAtX(e.getX());
	        int mci = table.convertColumnIndexToModel(vci);
	        if(mci == 0) {
	          TableColumn column = columnModel.getColumn(vci);
	          Object v = column.getHeaderValue();
	          boolean b = Status.DESELECTED.equals(v)?true:false;
	          TableModel m = table.getModel();
	          for(int i=0; i < m.getRowCount(); i++) m.setValueAt(b, i, mci);
	          column.setHeaderValue(b?Status.SELECTED:Status.DESELECTED);
	          header.repaint();
	        }
	     }
	}
	public void select(int index) {
		try {
			ps = con.prepareStatement("SELECT mealName,price,maxCount,todayMeal FROM meal WHERE cuisineNo = " + index );
			ResultSet result = ps.executeQuery();
			data.removeAllElements();
			while(result.next()) {
				Vector<Object> vector = new Vector<Object>();
				vector.add(false);
				vector.add(result.getString(1));
				vector.add(result.getString(2));
				vector.add(result.getString(3));
				vector.add(Integer.parseInt(result.getString(4))==0?"N":"Y");
				data.add(vector);
			}
			col.remove(0);
			col.insertElementAt(Status.DESELECTED, 0);
			table.updateUI();
		}catch(SQLException e){
			System.out.println(e.getMessage());
		}
	}
	
	public void invisible() {
		this.setVisible(false);
	}
	
	public void open_kanri() {
		select(Menu_Combo.getSelectedIndex()+1);
		this.setVisible(true);
	}
}
