package market.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Dividend extends FileHandler {
	private int year = 105;
	private DividendDAO dividendDao;
	private ArrayList<DividendDAO> dividendList = new ArrayList<DividendDAO>();
	
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Usage: "+System.getProperty("program.name")+" filename.csv");
			System.exit(0);
		}
		
		Dividend dividend = new Dividend(args[0]);
		dividend.doImport();
	}

	public Dividend(String filename) {
		super(filename);
	}
	public void doImport() {
		this.importCSV();
		DividendTable dividendTable = new DividendTable();
		dividendTable.insertDividend(dividendList);
	
	}
	public void importCSV() {
		String line = null;
		int lineNum = 0;
		try {
			while ((line = br.readLine()) != null) {
				dividendDao = new DividendDAO();
				if (lineNum++ == 0) continue;
				String[] elements = line.split(",");
				String[] corp = elements[0].split(" - ");
				float cash = Float.parseFloat(elements[1])+Float.parseFloat(elements[2]);
				float stock = Float.parseFloat(elements[3])+Float.parseFloat(elements[4]);
				dividendDao.setYear(year);
				dividendDao.setId(corp[0].trim());
				dividendDao.setName(corp[1].trim());
				dividendDao.setCash(cash);
				dividendDao.setStock(stock);
				
				dividendList.add(dividendDao);
/*
				for(int i=0; i<data.length; i++) {
					if (i==0) {
						String[] corp = data[i].split("-");
						for (String each: corp) {
							System.out.print(each.trim()+"\t");
						}
					} else System.out.print(data[i]+"\t");
				}
*/
				//System.out.println(line+"======="+corp[0].trim()+"\t"+corp[1].trim()+"\t"+cash+"\t"+stock);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}