package market.model.dao;

public class InstitutionDaily {

	public InstitutionDaily() {
	}

	private String tradingDate;
	private String item;
	private String totalBuy;
	private String totalSell;
	private String difference;
	
	public String getTradingDate() {
		return tradingDate;
	}
	public void setTradingDate(String tradingDate) {
		this.tradingDate = tradingDate;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getTotalBuy() {
		return totalBuy;
	}
	public void setTotalBuy(String totalBuy) {
		this.totalBuy = totalBuy;
	}
	public String getTotalSell() {
		return totalSell;
	}
	public void setTotalSell(String totalSell) {
		this.totalSell = totalSell;
	}
	public String getDifference() {
		return difference;
	}
	public void setDifference(String difference) {
		this.difference = difference;
	}
	
}
