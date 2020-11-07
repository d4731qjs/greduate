package DB;
import java.sql.*;

public class Driver_Connect {
	
	public static Connection makeConnection(String text) {
		String url = "jdbc:mysql://localhost:3306" + text + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
		String id = "root";
		String pass = "1234";
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("드라이브적재성공");
			con = DriverManager.getConnection(url, id, pass);
			System.out.println("데이터베이스 연결 성공");
		}catch(ClassNotFoundException e) {
			System.out.println("드라이브를 찾을 수 없습니다.");
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return con;
		
	}
}