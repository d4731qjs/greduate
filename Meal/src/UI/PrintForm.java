package UI;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.imageio.ImageIO;
import javax.print.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class PrintForm extends JFrame{
	
	JTable Table;
	String date;
	int memNo;
	int cuisineNo;
	Vector<JPanel> Tickets = new Vector<JPanel>();
	Print_Save_Action PSA = new Print_Save_Action();
	JButton Print_Button = new JButton("인쇄");
	JButton Save_Button = new JButton("저장");
	JPanel Grid_Panel; 
	
	
	public PrintForm(JTable Table, int memNo, int cuisineNo) {
	
		setTitle("식권");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.Table = Table;
		this.memNo = memNo;
		this.cuisineNo = cuisineNo;
		
		SimpleDateFormat format1 = new SimpleDateFormat ("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		date = format1.format(cal.getTime());
		String topText = date + "-" + memNo + "-" + cuisineNo;
		
		for(int i = 0; i < Table.getRowCount(); i++) {
			int Count = Integer.parseInt((String)Table.getValueAt(i, 2));
			
			for(int j = 0; j < Count; j ++) {
				MyPanel panel = new MyPanel(i,j,Count,topText);
				
			}
		}
		
		Container c = getContentPane();
		Grid_Panel = new JPanel();
		Grid_Panel.setBackground(Color.WHITE);
		Grid_Panel.setOpaque(true);
		int row = Tickets.size()%2 == 0 ? Tickets.size()/2 : Tickets.size()/2+1 ;
		
		Grid_Panel.setLayout(new GridLayout(row,2,10,10));
		
		for(int i = 0; i < Tickets.size(); i++) {
			Grid_Panel.add(Tickets.get(i));
		}
		
		JScrollPane scroll = new JScrollPane(Grid_Panel);
		c.add(scroll,BorderLayout.CENTER);
		
		JPanel Bottom_Panel = new JPanel();
		Bottom_Panel.setLayout(new GridLayout(1,2));
		Bottom_Panel.add(Print_Button);
		Bottom_Panel.add(Save_Button);
		Print_Button.addActionListener(PSA);
		Save_Button.addActionListener(PSA);
		c.add(Bottom_Panel,BorderLayout.SOUTH);
		
		setSize(600,500);
		setVisible(true);
		
	}
	
	class MyPanel extends JPanel{
		
		public MyPanel(int i, int j, int Count, String topText) {
			this.setLayout(new BorderLayout(5,15));
			JLabel top_label = new JLabel(topText,SwingConstants.LEFT);
			top_label.setFont(new Font("돋움",Font.BOLD,14));
			
			DecimalFormat formatter = new DecimalFormat("###,###");
			String price = formatter.format(Integer.parseInt((String)Table.getValueAt(i,3))/Count);
			JLabel center_label = new JLabel("<html><center>식권<br>"+price+"원",SwingConstants.CENTER);
			center_label.setFont(new Font("돋움",Font.BOLD,30));
			
			JPanel Bottom_Panel = new JPanel();
			Bottom_Panel.setLayout(new GridLayout(1,2));
			JLabel Bottom_Left = new JLabel("메뉴: " + Table.getValueAt(i, 1),SwingConstants.LEFT);
			JLabel Bottom_Right = new JLabel((j+1)+"/"+Count,SwingConstants.RIGHT);
			Bottom_Left.setFont(new Font("돋움",Font.BOLD,14));
			Bottom_Right.setFont(new Font("돋움",Font.BOLD,14));
			Bottom_Panel.add(Bottom_Left);
			Bottom_Panel.add(Bottom_Right);
			
			this.add(top_label,BorderLayout.NORTH);
			this.add(center_label,BorderLayout.CENTER);
			this.add(Bottom_Panel,BorderLayout.SOUTH);
			this.setBorder(new LineBorder(Color.BLACK,2));
			if(i%2 == 0) {
				this.setBackground(Color.PINK);
				Bottom_Panel.setBackground(Color.PINK);
			}
			else {
				this.setBackground(Color.CYAN);
				Bottom_Panel.setBackground(Color.CYAN);
			}
			
			Tickets.add(this);
			
		}
	}
	
	class Print_Save_Action implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton)e.getSource();
			String text = button.getText();
			
			switch(text) {
				
			case "저장" : 
				Image_Save();break;
			case "인쇄" :
				Image_Print(Grid_Panel);break;
			}
			
		}
	}
	
	public void Image_Save() {
		
		FileDialog fd = new FileDialog(this,"파일저장",FileDialog.SAVE);
		fd.setVisible(true);        
        
        String path = fd.getDirectory() + fd.getFile()+".jpg"; 
        
        File Save_Path = new File(path);
        
		BufferedImage image = new BufferedImage(Grid_Panel.getWidth(),Grid_Panel.getHeight(),BufferedImage.TYPE_INT_RGB);
		Grid_Panel.paint(image.getGraphics());
		try {
			System.out.println(ImageIO.write(image, "jpg",Save_Path));
		}catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void Image_Print(JPanel jpanel) {
		
		PrinterJob printJob = PrinterJob.getPrinterJob();
		
		printJob.setPrintable(new Printable() {
			@Override
			public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
				
				if(pageIndex  > 0) {
					return Printable.NO_SUCH_PAGE;
				}
				
				Graphics2D graphics2D = (Graphics2D)graphics;
				graphics2D.translate(pageFormat.getImageableX() * 2, pageFormat.getImageableY() * 2);
				graphics2D.scale(1.1,1.1);
				jpanel.paint(graphics2D);
				return Printable.PAGE_EXISTS;
			}
		});
		
		boolean result = printJob.printDialog();
		
		if(result) {
			try{printJob.print();}catch(PrinterException ex){System.out.println(ex.getMessage());}
		}
	}
}
