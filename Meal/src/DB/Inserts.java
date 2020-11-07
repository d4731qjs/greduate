package DB;
import java.io.*;
import java.util.*;
import java.sql.*;

public class Inserts {
	
	String[] address = {"식권발매//식권발매//DataFiles//member.txt",
			"식권발매//식권발매//DataFiles//cuisine.txt",
			"식권발매//식권발매//DataFiles//meal.txt",
			"식권발매//식권발매//DataFiles//orderlist.txt"
	};
	
	String[] insert_Sentance = {"INSERT INTO member VALUES(?,?,?,?)",
			"INSERT INTO cuisine VALUES(?,?)",
			"INSERT INTO meal VALUES(?,?,?,?,?,?)",
			"INSERT INTO orderlist VALUES(?,?,?,?,?,?,?)"};
	
	PreparedStatement ps = null;
	Connection con = null;
	
	int count;
	public Inserts() {
		
		con = Driver_Connect.makeConnection("/meal");
		
		for(int i = 0; i <4; i++) {

			try {
				
				Scanner fscanner = new Scanner(new FileInputStream(address[i]));
				ps = con.prepareStatement(insert_Sentance[i]);
				fscanner.nextLine();
				while(fscanner.hasNext()) {
					String text = fscanner.nextLine();
					
					StringTokenizer st = new StringTokenizer(text,"\t");
							
					Vector<String> data = new Vector<String>();
					
					while(st.hasMoreTokens()) {
						String token = st.nextToken();
						data.add(token);
					}
					
					for(int j = 0; j < data.size(); j++) {
						
						ps.setString(j+1,data.get(j));
					}
					
					int result = ps.executeUpdate();
					
					if(result == 1) {
						System.out.println("추가 성공");
					}else {
						System.out.println("추가 실패");
					}
				}
				
				
			}catch(IOException e) {
				System.out.println(e.getMessage());
			}catch(SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
	
	public static void main(String[] args) {
		new Create_Table();
		new Inserts();
	}
}
