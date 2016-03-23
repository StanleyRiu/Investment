package market.model.db.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

import market.model.dao.InstitutionDaily;

public class InstitutionTable extends Table {

	public InstitutionTable() {
		// TODO Auto-generated constructor stub
	}

	
	//public int insertInstitution(Timestamp tradingDate, String item, int totalBuy, int totalSell, int difference) {
	public int insertInstitution(ArrayList<InstitutionDaily> alInst) {
		InstitutionDaily inst = null;
		Iterator<InstitutionDaily> it = alInst.iterator();
		DateFormat df = DateFormat.getDateInstance();
		
		while (it.hasNext()) {
			inst = it.next();
			
			String sql = "insert into institution (trading_date, item, total_buy, total_sell, difference) values (?, ?, ?, ?, ?)";
			PreparedStatement pstmt = null;
			
			try {
				 pstmt = con.prepareStatement(sql);
				 java.util.Date d = df.parse(inst.getTradingDate());
//System.out.println(inst.getTradingDate()+" "+d.toString());
				 pstmt.setDate(1, new Date(d.getTime()));
				 pstmt.setString(2, inst.getItem());
				 pstmt.setLong(3, Long.parseLong(inst.getTotalBuy()));
				 pstmt.setLong(4, Long.parseLong(inst.getTotalSell()));
				 pstmt.setLong(5, Long.parseLong(inst.getDifference()));
				 
				 pstmt.executeUpdate();
				 
				 sql = "select trading_date from institution";
				 Statement stmt = con.createStatement();
				 ResultSet rs = stmt.executeQuery(sql);
				 while (rs.next()) {
					 System.out.println(rs.getDate("trading_date").toString());
				 }
			} catch (SQLException | ParseException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
}