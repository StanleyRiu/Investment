package market.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import market.model.dao.InstitutionDaily;
import market.model.db.dao.Table;

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
		Long date = Table.getLastFetchDate("institution");
		
		//StringBuilder sb = new StringBuilder(baseUrl);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(date));

		Calendar rightNow = Calendar.getInstance();

		while (cal.before(rightNow)) {
			cal.add(Calendar.DAY_OF_MONTH, 1);
			String tradingDay = sdf.format(cal.getTime());
			
			cal.add(Calendar.DAY_OF_MONTH, -1);
			String lastTradingDay = sdf.format(cal.getTime());
			
			baseUrl = baseUrl.replace("20160318", tradingDay);
			baseUrl = baseUrl.replace("20160317", lastTradingDay);
			//System.out.println(baseUrl);
			fetchURL();

			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
	}

	private URLConnection getURLConnection() {
		InetAddress ia = null;
		byte[] proxyIp = { 10, (byte) 160, 3, 88 };
		URLConnection urlc = null;
		URL url = null;

		try {
			url = new URL(baseUrl);
			boolean bIntranet = false;
			Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();
			while (ni.hasMoreElements()) {
				Enumeration<InetAddress> eia = ni.nextElement().getInetAddresses();
				while (eia.hasMoreElements()) {
					if (eia.nextElement().getHostAddress().startsWith("10.144")) {
						bIntranet = true;
					}
					if (bIntranet) break;
				}
				if (bIntranet) break;
				/*
				List<InterfaceAddress> lia = ni.nextElement().getInterfaceAddresses();
				Iterator<InterfaceAddress> it = lia.iterator();
				while (it.hasNext())
					System.out.println(it.next().getAddress().getHostAddress());
					*/
			}
			
			if (bIntranet) {
				ia = InetAddress.getByAddress(proxyIp);
				InetSocketAddress isa = new InetSocketAddress(ia, 8080);
				Proxy proxy = new Proxy(Proxy.Type.HTTP, isa);
				urlc = url.openConnection(proxy);
			} else {
				urlc = url.openConnection();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return urlc;
	}
	
	private boolean fetchURL() {
		URLConnection urlc = getURLConnection();
		
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
						data = content.split("\"?,?\"");
//System.out.println(content);
//for (String s : data) System.out.println(s.replace(",", ""));
						inst.setTradingDate(tradingDate);
						inst.setItem(data[0].replace(",", ""));
						inst.setTotalBuy(data[1].replace(",", ""));
						inst.setTotalSell(data[2].replace(",", ""));
						inst.setDifference(data[3].replace(",", ""));
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
