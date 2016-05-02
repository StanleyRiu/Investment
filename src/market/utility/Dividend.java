package market.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import market.model.Network;

public class Dividend extends Network {
	private DividendDAO dividendDao;
	private ArrayList<DividendDAO> dividendList;
	private String csvFileName;
	private int year;
	private String marketType;
	
	public static void main(String[] args) {
		/*
		 * if (args.length != 3) { System.err.println("Usage: "
		 * +System.getProperty("sun.java.command")+
		 * " year <TSE|OTC> filename.csv"); System.exit(0); }
		 */
		Dividend dividend = new Dividend();
		dividend.doImport(5);
	}

	public Dividend() {
	}

	public void doImport(int lastN) {
		/*
		 * "sii"	上市
		 * "otc"	上櫃
		 * "rotc"	興櫃
		 * "pub"	公開發行
		 */

		Calendar cal = Calendar.getInstance();
		DividendTable dividendTable = new DividendTable();
		
		for (int i = 0; i < lastN; i++) {
			int year = cal.get(Calendar.YEAR)-1911-i;
			System.out.println("start fetch year "+year);
			fetchDividendCSV(year, "sii");
			System.out.println("finished fetch year "+year);
			dividendTable.insertDividend(dividendList);
			System.out.println("finished insert year "+year+" to db");
		}
		System.out.println("finished import");
	}

	public void fetchDividendCSV(int year, String marketType) {
		this.year = year;
		this.marketType = marketType;
		/*
		 * 
		 * http://mops.twse.com.tw/server-java/t05st09sub x-www-form-urlencoded
		 * TYPEK=sii YEAR=104 first= step=1
		 * 
		 * http://mops.twse.com.tw/server-java/t105sb02 x-www-form-urlencoded
		 * filename=t05st09_new_20160417_210745368.csv firstin=true step=10
		 * 
		 */
		String url1 = "http://mops.twse.com.tw/server-java/t05st09sub";
		String url2 = "http://mops.twse.com.tw/server-java/t105sb02";

		Network net = new Network();
		//first phase
		String content = null;
		try {
			content = "TYPEK=" + URLEncoder.encode(marketType, "BIG5") + "&YEAR=" + URLEncoder.encode(String.valueOf(year), "BIG5")
					+ "&first=" + URLEncoder.encode("", "BIG5") + "&step=" + URLEncoder.encode("1", "BIG5");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		StringBuilder sb = net.doPost(url1, content);
		String[] lines = sb.toString().split(System.getProperty("line.separator"));

		for (String line : lines) {
			if (line.contains("filename")) {
				String[] pieces = line.split("'");
				csvFileName = pieces[5];
				break;
				
			}
		}
		
		//second phase
		StringBuilder postData = new StringBuilder();
		try {
			Map<String, Object> params = new LinkedHashMap<>();
			params.put("filename", csvFileName);
			params.put("firstin", "true");
			params.put("step", "10");
			for (Map.Entry<String, Object> param : params.entrySet()) {
				if (postData.length() != 0)	postData.append('&');
				postData.append(URLEncoder.encode(param.getKey(), "BIG5"));
				postData.append('=');
				postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "BIG5"));
			}
			byte[] postDataBytes = postData.toString().getBytes("BIG5");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		sb = net.doPost(url2, postData.toString());
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("C:\\Users\\Stanley\\Downloads\\dividend.csv"));
			pw.print(sb);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		lines = sb.toString().split("\""+System.getProperty("line.separator"));
		HashSet<String> hs = new HashSet<String>();
		dividendList = new ArrayList<DividendDAO>();
		  
		for (String line : lines) {
			String[] cells = line.split("\",");
			//for (String s : cells) s = s.replace("\"", "");
			if (cells[0].replace("\"", "").matches("^\\d{4,}.+")) {	//filter out useless lines
				String[] corps = cells[0].replace("\"", "").split(" - ");
				if (hs.contains(corps[0])) continue;
				hs.add(corps[0]);
				
				dividendDao = new DividendDAO();
				float cash = 0, stock = 0;
				try {
					cash = Float.parseFloat(cells[9].replace("\"", "")) + Float.parseFloat(cells[10].replace("\"", ""));
					stock = Float.parseFloat(cells[12].replace("\"", "")) + Float.parseFloat(cells[13].replace("\"", ""));
				} catch (NumberFormatException e) {
					System.err.println(line);
					e.printStackTrace();
				}
				dividendDao.setYear(year);
				dividendDao.setMarketType(marketType);
				dividendDao.setId(corps[0].trim());
				dividendDao.setName(corps[1].trim());
				dividendDao.setCash(cash);
				dividendDao.setStock(stock);
				dividendList.add(dividendDao);
				//System.out.println(cash+" "+stock);
			}
		}
	}


	public void importCSV(String filename) {
		String line = null;
		int lineNum = 0;
		FileHandler f = new FileHandler(filename);
		while ((line = f.readLine()) != null) {
			dividendDao = new DividendDAO();
			if (lineNum++ == 0)
				continue;
			String[] elements = line.split(",");
			String[] corp = elements[0].split(" - ");
			float cash = Float.parseFloat(elements[1]) + Float.parseFloat(elements[2]);
			float stock = Float.parseFloat(elements[3]) + Float.parseFloat(elements[4]);
			dividendDao.setYear(year);
			dividendDao.setMarketType(marketType);
			dividendDao.setId(corp[0].trim());
			dividendDao.setName(corp[1].trim());
			dividendDao.setCash(cash);
			dividendDao.setStock(stock);

			dividendList.add(dividendDao);
			/*
			 * for(int i=0; i<data.length; i++) { if (i==0) { String[] corp
			 * = data[i].split("-"); for (String each: corp) {
			 * System.out.print(each.trim()+"\t"); } } else
			 * System.out.print(data[i]+"\t"); }
			 */
			// System.out.println(line+"======="+corp[0].trim()+"\t"+corp[1].trim()+"\t"+cash+"\t"+stock);
		}
	}

}