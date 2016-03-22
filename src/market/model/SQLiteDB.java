package market.model;
//STEP 1. Import required packages
import java.sql.*;

public class SQLiteDB {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "org.sqlite.JDBC";
	private String dbURL = "";
	private Connection con = null;

	public SQLiteDB(String dbFile) {
		dbURL = dbFile;
		
		try {
			// STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);
			this.con = this.getConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		if (this.con != null) return this.con;
		
		Connection conn = null;

		try {
			// STEP 3: Open a connection
			conn = DriverManager.getConnection("jdbc:sqlite:"+dbURL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
}