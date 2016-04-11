package market.utility;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

import market.model.db.dao.Table;

public class DividendTable extends Table {

	public DividendTable() {
		// TODO Auto-generated constructor stub
	}

	
	//public int insertInstitution(Timestamp tradingDate, String item, int totalBuy, int totalSell, int difference) {
	public int insertDividend(ArrayList<DividendDAO> alInst) {
		market.utility.DividendDAO dividend = null;
		Iterator<DividendDAO> it = alInst.iterator();
		DateFormat df = DateFormat.getDateInstance();
		String sql = null;
		
		while (it.hasNext()) {
			dividend = it.next();
			
			sql = "insert into dividend (year, id, name, cash, stock) values (?, ?, ?, ?, ?)";
			PreparedStatement pstmt = null;
			
			try {
				 pstmt = con.prepareStatement(sql);
				 pstmt.setInt(1, dividend.getYear());
				 pstmt.setString(2, dividend.getId());
				 pstmt.setString(3, dividend.getName());
				 pstmt.setFloat(4, dividend.getCash());
				 pstmt.setFloat(5, dividend.getStock());
				 
				 pstmt.executeUpdate();
				 
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println(dividend.getYear()+", "+dividend.getId());
			}
		}
		return 0;
	}
}
