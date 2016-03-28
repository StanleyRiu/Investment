package market.model.db.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import market.model.SQLiteDB;

public class Table {
	protected Connection con = null;
	private SQLiteDB sqlite = null;

	public Table() {
		sqlite = new SQLiteDB("Investment.sqlite");
		con = sqlite.getConnection();
	}
	
	public static long getLastFetchDate(String tableName) {
		Connection con = null;
		SQLiteDB sqlite = null;
		sqlite = new SQLiteDB("Investment.sqlite");
		con = sqlite.getConnection();
		Statement stmt = null;
		try {
			String sql = "select max(trading_date) from "+tableName;
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
				return rs.getLong(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return 0;
	}

}
