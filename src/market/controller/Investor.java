package market.controller;

import market.model.TWSE;
import market.model.db.dao.InstitutionTable;

public class Investor {

	public Investor() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		TWSE twse = new TWSE();
		
		twse.fetchInstitution();
		
		InstitutionTable inst = new InstitutionTable();
		inst.insertInstitution(twse.getAlInst());
		
	}

}
