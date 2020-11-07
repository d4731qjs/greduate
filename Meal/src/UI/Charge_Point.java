package UI;
import java.awt.*;
import java.sql.Connection;
import java.awt.event.*;
import javax.swing.*;

public class Charge_Point extends JDialog{
	
	Button_Action button_action = new Button_Action();
	JLabel Title = new JLabel("회원 포인트 충전",SwingConstants.CENTER);
	JLabel Buttom = new JLabel("현금 충전시 5% 추가 충전의 혜택이 있습니다.",SwingConstants.CENTER);
	JButton[] buttons = new JButton[2];
	String[] b_title = {"현금","카드"};
	
	Charge_Card charge_card;
	
	public Charge_Point(Connection con) {
		setTitle("포인트 충전");
		setLayout(new BorderLayout());
		Title.setFont(new Font("맑은 고딕",Font.PLAIN,24));
		this.add(Title,BorderLayout.NORTH);
		
		JPanel Button_Frame = new JPanel();
		Button_Frame.setLayout(new FlowLayout(FlowLayout.CENTER,20,40));
		for(int i = 0; i < 2; i++) {
			
			buttons[i] = new JButton(b_title[i]);
			buttons[i].setFont(new Font("돋움",Font.BOLD,16));
			buttons[i].addActionListener(button_action);
			Button_Frame.add(buttons[i]);
			
		}
		
		this.add(Button_Frame,BorderLayout.CENTER);
		
		Buttom.setFont(new Font("맑은 고딕",Font.PLAIN,12));
		Buttom.setForeground(Color.red);
		this.add(Buttom,BorderLayout.SOUTH);
		
		charge_card = new Charge_Card(con);
		setVisible(false);
		setSize(300,200);	
	}
	
	class Button_Action implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {

			String text = ((JButton)e.getSource()).getText();
			
			switch(text) {
			case "현금" : 
				JOptionPane.showMessageDialog(null,"현금 충전은 직원에게 문의해주세요.","현금 결제",JOptionPane.INFORMATION_MESSAGE);break;
			case "카드" : 
				charge_card.Change_Visible(true);
			}
			
		}
	}
	
	public void Change_Visible(boolean bol) {
		this.setVisible(bol);
	}

}
