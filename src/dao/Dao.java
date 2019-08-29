package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Dao {
	private final static String DRIVER = "com.mysql.jdbc.Driver";
	private final static String URL = "jdbc:mysql://localhost:3306/okhttpdemo";
	private final static String USER = "root";
	private final static String PWD = "root";

	public Connection getCon() throws ClassNotFoundException, SQLException{
		Class.forName(DRIVER);
		Connection con = DriverManager.getConnection(URL,USER,PWD);
		System.out.println(con);
		return con;
	}
	
	public void closeAll(ResultSet rs , Statement st , Connection con) throws SQLException{
		if(rs != null){
			rs.close();
		}
		if(st != null){
			st.close();
		}
		if(con != null){
			con.close();
		}
	}
	
}
