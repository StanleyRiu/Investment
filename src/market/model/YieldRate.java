package market.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import market.model.dao.DividendDAO;

public class YieldRate {
/*
 * english version:
 * http://www.twse.com.tw/en/trading/exchange/MI_INDEX/MI_INDEX3_print.php?genpage=genpage/Report201603/A11220160314ALLBUT0999_1.php&type=csv
 * 
 * chinese version:
 * http://www.twse.com.tw/ch/trading/exchange/MI_INDEX/MI_INDEX.php
 * download=csv
 * qdate=105/05/05
 * selectType=ALLBUT0999 
 * x-www-form-urlencoded
 */

	public void fetchDailyQuotes(String qdate) {
		String url = "http://www.twse.com.tw/ch/trading/exchange/MI_INDEX/MI_INDEX.php";

		Network net = new Network();
		/*first phase
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
		*/
		//second phase
		StringBuilder postData = new StringBuilder();
		try {
			Map<String, Object> params = new LinkedHashMap<>();
			params.put("download", "csv");
			params.put("qdate", qdate);
			params.put("selectType", "ALLBUT0999");
			for (Map.Entry<String, Object> param : params.entrySet()) {
				if (postData.length() != 0)	postData.append('&');
				postData.append(URLEncoder.encode(param.getKey(), "BIG5"));
				postData.append('=');
				postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "BIG5"));
			}
//			byte[] postDataBytes = postData.toString().getBytes("BIG5");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		StringBuilder sb = net.doPost(url, postData.toString());
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
}
