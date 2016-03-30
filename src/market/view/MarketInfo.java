package market.view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import market.model.db.dao.Table;

public class MarketInfo extends Table {

	public MarketInfo() {
		// TODO Auto-generated constructor stub
	}

	public void showInstitution() {
		String sql = null;
	
		try {
			sql = "select * from institution";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			Calendar cal = Calendar.getInstance();
			
			while (rs.next()) {
				if (rs.getString("item").contains("Foreign")) {
					cal.setTime(rs.getDate("trading_date"));
					System.out.println(String.format("%tF %<ta", cal.getTime())+'\t'+String.format("% ,10d", rs.getLong("difference")/100000000));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
