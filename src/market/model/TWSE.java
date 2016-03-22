package market.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import market.model.dao.InstitutionDaily;

public class TWSE {
	//http://www.twse.com.tw/ch/trading/fund/BFI82U/BFI82U_print.php?begin_date=20160318&end_date=20160317&report_type=day&language=ch&save=csv
	//http://www.twse.com.tw/ch/trading/fund/BFI82U/BFI82U_print.php?begin_date=20160318&end_date=&report_type=day&language=en&save=csv
	private String baseUrl = "http://www.twse.com.tw/ch/trading/fund/BFI82U/BFI82U_print.php?begin_date=20160318&end_date=20160317&report_type=day&language=en&save=csv";

	private ArrayList<InstitutionDaily> alInst = new ArrayList<InstitutionDaily>();
	
	public ArrayList<InstitutionDaily> getAlInst() {
		return alInst;
	}

	public TWSE() {
	}
	
	public void fetchInstitution() {
		//StringBuilder sb = new StringBuilder(baseUrl);
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		cal.add(Calendar.DAY_OF_MONTH, -1);
		String tradingDay = sdf.format(cal.getTime());
		
		cal.add(Calendar.DAY_OF_MONTH, -1);
		String lastTradingDay = sdf.format(cal.getTime());
		
		baseUrl = baseUrl.replace("20160318", tradingDay);
		baseUrl = baseUrl.replace("20160317", lastTradingDay);
		//System.out.println(baseUrl);
		fetchURL();
	}
	
	private boolean fetchURL() {
		byte[] ip = {10, (byte) 160, 3, 88};
		InetAddress ia = null;
		try {
			ia = InetAddress.getByAddress(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		InetSocketAddress isa = new InetSocketAddress(ia, 8080);
		Proxy proxy = new Proxy(Proxy.Type.HTTP, isa);
		URL url = null;
		try {
			url = new URL(baseUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		URLConnection urlc = null;
		try {
			urlc = url.openConnection(proxy);
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(urlc.getInputStream(), "BIG5");
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(isr);
		
		try {
			String tradingDate = null;
			for (int i=0; br.ready(); i++) {
				String content = br.readLine();
				if (content.contains("查無資料")) {
					System.err.println("查無資料");
					return false;
				}
				else {
					String[] data = null;

					if (i == 0) {
						data = content.split(" ");
						tradingDate = data[0];
					} else if (i == 1) {
						continue;
					} else {
						InstitutionDaily inst = new InstitutionDaily();
						data = content.split(",");
System.out.println(content+" "+data[2]);
System.out.println(data[2]+" "+data[2].replace("\"", "")+" "+data[2].replace("\"", "").replace(",", ""));
						inst.setTradingDate(tradingDate);
						inst.setItem(data[0].replace("\"", "").replace(",", ""));
						inst.setTotalBuy(data[1].replace("\"", "").replace(",", ""));
						inst.setTotalSell(data[2].replace("\"", "").replace(",", ""));
						inst.setDifference(data[3].replace("\"", "").replace(",", ""));
//System.out.println(inst.getTradingDate()+" "+inst.getItem()+" "+inst.getTotalBuy()+" "+inst.getTotalSell()+" "+inst.getDifference());
						alInst.add(inst);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
