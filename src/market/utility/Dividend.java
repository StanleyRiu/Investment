package market.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Dividend extends FileHandler {
	private int year = 105;
	private DividendDAO dividendDao;
	private List<DividendDAO> dividendList = new ArrayList<DividendDAO>();
	
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Usage: "+System.getProperty("program.name")+" filename.csv");
			System.exit(0);
		}
		
		Dividend dividend = new Dividend(args[0]);
		dividend.importCSV();
		
		
	}

	public Dividend(String filename) {
		super(filename);
	}

	public void importCSV() {
		dividendDao = new DividendDAO();
		String line = null;
		int lineNum = 0;
		try {
			while ((line = br.readLine()) != null) {
				if (lineNum++ == 0) continue;
				String[] elements = line.split(",");
				String[] corp = elements[0].split(" - ");
				float cash = Float.parseFloat(elements[1])+Float.parseFloat(elements[2]);
				float stock = Float.parseFloat(elements[3])+Float.parseFloat(elements[4]);
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

	private class DividendDAO {
		private String id;
		private String name;
		private float cash;
		private float stock;

		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public float getCash() {
			return cash;
		}
		public void setCash(float cash) {
			this.cash = cash;
		}
		public float getStock() {
			return stock;
		}
		public void setStock(float stock) {
			this.stock = stock;
		}
	}
}