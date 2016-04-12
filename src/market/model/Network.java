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

public class Network {
	private Calendar cal = Calendar.getInstance();
	private Calendar rightNow = Calendar.getInstance();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	public Network() {
	}
	
	private URLConnection getURLConnection(String targetUrl) {
		InetAddress ia = null;
		byte[] proxyIp = { 10, (byte) 160, 3, 88 };
		URLConnection urlc = null;
		URL url = null;

		try {
			url = new URL(targetUrl);
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
	
	public InputStreamReader fetchURL(String targetUrl) {
		URLConnection urlc = getURLConnection(targetUrl);
		
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(urlc.getInputStream(), "BIG5");
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(isr);
		
		return isr;
	}
}
