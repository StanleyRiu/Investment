package market.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import market.model.dao.DividendDAO;
import market.model.dao.YieldRateDAO;

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
	private YieldRateDAO yieldRateDao;
	private ArrayList<YieldRateDAO> yieldRateList;

	Calendar cal = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat();
	
	public void go() {
		sdf.applyPattern("yyyy");
		String yyyy = sdf.format(cal.getTime());
		sdf.applyPattern("/MM/dd");
		String MMdd = sdf.format(cal.getTime());
		
		System.out.println((Integer.parseInt(yyyy)-1911)+MMdd);
	}
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
			pw = new PrintWriter(new File("C:\\Users\\Stanley\\Downloads\\dailyQuotes.csv"));
			//pw.print(sb);
			//pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String[] lines = sb.toString().split(System.getProperty("line.separator"));

		for (String line : lines) {
			String[] pieces = line.split("\",");
			String piece = null;
			//piece=pieces[0].replaceAll("=?\"| *", "");
			if ((piece=pieces[0].replaceAll("=?\"| *", "")).matches("^\\w{4,}")) {

				String name = pieces[1].replace("\"", "").trim();
				float price = Float.parseFloat(pieces[8].replace("\"", "").trim());
				
				pw.print(piece+"\t");
				System.out.print(piece+"\t");
				
				yieldRateDao = new YieldRateDAO();
				yieldRateDao.setId(piece);
				yieldRateDao.setName(name);
				yieldRateDao.setQuoteDate(qdate);
				yieldRateDao.setPrice(price);
				float cash = 0;
				float stock = 0;
				yieldRateDao.setCash(cash);
				yieldRateDao.setStock(stock);
				yieldRateList.add(yieldRateDao);
			}
				
		}
		pw.close();
	}
}
