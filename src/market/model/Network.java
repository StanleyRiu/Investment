package market.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.CharBuffer;
import java.util.Enumeration;

public class Network {
	HttpURLConnection hUrlc = null;

	public Network() {
	}

	public HttpURLConnection getHttpURLConnection(String targetUrl) {
		InetAddress ia = null;
		byte[] proxyIp = { 10, (byte) 160, 3, 88 };
		HttpURLConnection hUrlc = null;
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
					if (bIntranet)
						break;
				}
				if (bIntranet)
					break;
				/*
				 * List<InterfaceAddress> lia =
				 * ni.nextElement().getInterfaceAddresses();
				 * Iterator<InterfaceAddress> it = lia.iterator(); while
				 * (it.hasNext())
				 * System.out.println(it.next().getAddress().getHostAddress());
				 */
			}

			if (bIntranet) {
				ia = InetAddress.getByAddress(proxyIp);
				InetSocketAddress isa = new InetSocketAddress(ia, 8080);
				Proxy proxy = new Proxy(Proxy.Type.HTTP, isa);
				hUrlc = (HttpURLConnection) url.openConnection(proxy);
			} else {
				hUrlc = (HttpURLConnection) url.openConnection();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return hUrlc;
	}

	public InputStreamReader fetchHttpURL(String targetUrl) {
		HttpURLConnection hUrlc = getHttpURLConnection(targetUrl);

		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(hUrlc.getInputStream(), "BIG5");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return isr;
	}

	public StringBuilder doPost(String url, String content) {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		hUrlc = getHttpURLConnection(url);
		try {
			DataOutputStream dos = null;
			hUrlc.setRequestMethod("POST");
			// Let the run-time system (RTS) know that we want input.
			hUrlc.setDoInput(true);
			// Let the RTS know that we want to do output.
			hUrlc.setDoOutput(true);
			// No caching, we want the real thing.
			hUrlc.setUseCaches(false);
			// Specify the content type.
			hUrlc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// Send POST output.
			dos = new DataOutputStream(hUrlc.getOutputStream());
			dos.writeBytes(content);
			dos.flush();
			dos.close();
			// Get response data.
			is = hUrlc.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			String line = null;
			int count = 0;
			while ((line = br.readLine()) != null) {
				count++;
				sb.append(line).append(System.getProperty("line.separator"));
				//System.out.println(line);
			}
			//System.out.println("total lines = "+ count);
			br.close();
			isr.close();
			is.close();
			hUrlc.disconnect();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
		}
		return sb;
	}
}
