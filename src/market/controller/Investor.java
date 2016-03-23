package market.controller;

import java.util.Date;

import market.model.TWSE;
import market.model.db.dao.InstitutionTable;

public class Investor {

	public Investor() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		//Date d = new Date(1458576000000L);
		//System.out.println(d.toString());
		//System.exit(0);
		TWSE twse = new TWSE();
		
		twse.fetchInstitution();
		
		InstitutionTable inst = new InstitutionTable();
		inst.insertInstitution(twse.getAlInst());
		
	}

}
