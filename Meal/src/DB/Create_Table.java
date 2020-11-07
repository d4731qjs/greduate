package DB;
import java.sql.*;
import java.util.*;

public class Create_Table {
	
	Connection con = null;
	
	public Create_Table() {
		
		String create_database = "create Database if not exists meal;";
		
		String create_member = "create table if not exists member("
				+ "memberNo int not null, " 
				+ "memberName varchar(20), "
				+ "passwd varchar(4), "
				+ "point int not null, "
				+ "primary key(memberNo))";
		
		String create_cuisine = "create table if not exists cuisine("
				+ "cuisineNo int not null, " 
				+ "cuisineName varchar(10), "
				+"primary key(cuisineNo))";
		
		String create_meal = "create table if not exists meal("
				+ "mealNo int not null, " 
				+ "cuisineNo int, " 
				+ "mealName varchar(20), "
				+ "price int, " 
				+ "maxCount int, " 
				+ "todayMeal tinyint(1), "
				+"primary key(mealNo))";
		
		String create_orderlist = "create table if not exists orderlist("
				+ "orderNo int not null, " 
				+ "cuisineNo int, " 
				+ "mealNo int, "
				+ "memberNo int, " 
				+ "orderCount int, " 
				+ "amount int, "
				+ "orderDate datetime, "
				+"primary key(orderNo))";
				
		String[] var = {create_member ,create_cuisine, create_meal, create_orderlist};
		
		try {
			con = Driver_Connect.makeConnection("");
			Statement st = con.createStatement();
			st.executeUpdate(create_database);
			con = Driver_Connect.makeConnection("/meal");
			st = con.createStatement();
			
			for(int i = 0; i < 4; i++) {
				int result = st.executeUpdate(var[i]);
				if(result != 0 ) {System.out.println("성공");}
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}