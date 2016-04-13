package market.controller;

import java.util.Date;

import market.model.Institution;
import market.model.db.dao.InstitutionTable;
import market.view.MarketInfo;

public class Investor {

	public Investor() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		//Date d = new Date(1458576000000L);
		//System.out.println(d.toString());
		//System.exit(0);
		Institution twse = new Institution();
		
		twse.fetchInstitution();
		
		InstitutionTable inst = new InstitutionTable();
		inst.insertInstitution(twse.getAlInst());
		
		MarketInfo mi = new MarketInfo();
		mi.showInstitution();
		
		//System.out.println("done");
	}

}
