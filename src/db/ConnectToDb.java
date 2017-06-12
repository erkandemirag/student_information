package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectToDb {

	private static Connection conn;

	public static Connection getInstance() {
		if (conn == null) {
			getConnection();
		}
		return conn;
	}

	private static void getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/student_informations";
			Properties info = new Properties();
			info.put("user", "root");
			info.put("password", "123");

			conn = DriverManager.getConnection(url, info);
			if (conn != null) {
				System.out.println("Connected to the database information_schema");
			}
		} catch (SQLException ex) {
			System.out.println("An error occurred. Maybe user/password is invalid");
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return;
		}
	}

}
