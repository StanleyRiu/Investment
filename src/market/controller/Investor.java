package market.controller;

import market.model.Dividend;
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
		Investor investor = new Investor();
		investor.checkDividend(5);
	}
	
	public void checkInstitution() {
		Institution twse = new Institution();
		
		twse.fetchInstitution();
		
		InstitutionTable inst = new InstitutionTable();
		inst.insertInstitution(twse.getAlInst());
		
		MarketInfo mi = new MarketInfo();
		mi.showInstitution();

		//System.out.println("done");
	}

	public boolean checkDividend(int lastNyear) {
		/*
		 * if (args.length != 3) { System.err.println("Usage: "
		 * +System.getProperty("sun.java.command")+
		 * " year <TSE|OTC> filename.csv"); System.exit(0); }
		 */
		if (lastNyear <= 0) return false;
		Dividend dividend = new Dividend();
		dividend.doImport(lastNyear);
		return true;
	}

	public void checkYieldRate() {
		
	}
}
