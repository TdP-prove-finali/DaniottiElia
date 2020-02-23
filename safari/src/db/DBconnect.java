package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariDataSource;


public class DBconnect {
	
	static private final String jdbcUrl = "jdbc:mysql://localhost/safari?user=root";

	public static Connection getConnection() {

		try {
			Connection connection = DriverManager.getConnection(jdbcUrl);
			return connection;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException("Cannot get a connection " + jdbcUrl, e);
		}
	}
}
