package market.utility;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import market.model.Network;

public class Dividend extends Network {
	private int year;
	private String marketType;
	private DividendDAO dividendDao;
	private ArrayList<DividendDAO> dividendList = new ArrayList<DividendDAO>();
	private String csvFileName;
	
	public static void main(String[] args) {
		/*
		if (args.length != 3) {
			System.err.println("Usage: "+System.getProperty("sun.java.command")+" year <TSE|OTC> filename.csv");
			System.exit(0);
		}
		*/
		Dividend dividend = new Dividend(args);
		//dividend.doImport(dividend.csvFileName);
		dividend.fetchDividendCSV();
	}

	public Dividend(String[] args) {
		//super(args[2]);
		//this.year = Integer.parseInt(args[0]);
		//this.marketType = args[1];
	}
	
	public void fetchDividendCSV() {
		/*
		 * 
http://mops.twse.com.tw/server-java/t05st09sub
x-www-form-urlencoded
TYPEK=sii
YEAR=104
first=
step=1

http://mops.twse.com.tw/server-java/t105sb02
x-www-form-urlencoded
filename=t05st09_new_20160417_210745368.csv
firstin=true
step=10

		 */
		String url1 = "http://mops.twse.com.tw/server-java/t05st09sub";
		
		String url2 = "http://mops.twse.com.tw/server-java/t105sb02";

		Network net = new Network();
	    String content = null;
		try {
			content = "TYPEK=" + URLEncoder.encode("sii", "BIG5") +
			"&YEAR=" + URLEncoder.encode("104", "BIG5") +
			"&first=" + URLEncoder.encode("", "BIG5") +
			"&step=" + URLEncoder.encode("1", "BIG5");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		net.doPost(url1, content);
	}
	
	public void doImport(String filename) {
		this.importCSV(filename);
		DividendTable dividendTable = new DividendTable();
		dividendTable.insertDividend(dividendList);
	
	}
	public void importCSV(String filename) {
		String line = null;
		int lineNum = 0;
		FileHandler f = new FileHandler(filename);
		try {
			while ((line = f.br.readLine()) != null) {
				dividendDao = new DividendDAO();
				if (lineNum++ == 0) continue;
				String[] elements = line.split(",");
				String[] corp = elements[0].split(" - ");
				float cash = Float.parseFloat(elements[1])+Float.parseFloat(elements[2]);
				float stock = Float.parseFloat(elements[3])+Float.parseFloat(elements[4]);
				dividendDao.setYear(year);
				dividendDao.setMarketType(marketType);
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