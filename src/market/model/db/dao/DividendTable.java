package market.model.db.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import market.model.dao.DividendDAO;

public class DividendTable extends Table {

	public DividendTable() {
	}

	
	//public int insertInstitution(Timestamp tradingDate, String item, int totalBuy, int totalSell, int difference) {
	public int insertDividend(ArrayList<DividendDAO> alInst) {
		market.model.dao.DividendDAO dividend = null;
		Iterator<DividendDAO> it = alInst.iterator();
//		DateFormat df = DateFormat.getDateInstance();
		String sql = null;
		
		while (it.hasNext()) {
			dividend = it.next();
			
			sql = "insert into dividend (year, market_type, id, name, cash, stock) values (?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt = null;
			
			try {
				 pstmt = con.prepareStatement(sql);
				 pstmt.setInt(1, dividend.getYear());
				 pstmt.setString(2, dividend.getMarketType());
				 pstmt.setString(3, dividend.getId());
				 pstmt.setString(4, dividend.getName());
				 pstmt.setFloat(5, dividend.getCash());
				 pstmt.setFloat(6, dividend.getStock());
				 
				 pstmt.executeUpdate();
				 
			} catch (SQLException e) {
				//e.printStackTrace();
				System.out.println(dividend.getYear()+", "+dividend.getId());
			}
		}
		return 0;
	}
}
