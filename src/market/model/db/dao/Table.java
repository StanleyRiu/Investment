package market.model.db.dao;

import java.sql.Connection;
import market.model.SQLiteDB;

public class Table {
	protected Connection con = null;
	private SQLiteDB sqlite = null;

	public Table() {
		sqlite = new SQLiteDB("Investment.sqlite");
		con = sqlite.getConnection();
	}
	
}
