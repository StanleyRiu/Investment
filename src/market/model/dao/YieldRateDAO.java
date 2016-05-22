package market.model.dao;

public class YieldRateDAO {
	private String id;
	private String name;
	private String quoteDate;
	private float price;
	private float cash;
	private float stock;
	private float cashYieldRate;
	private float stockYieldRate;
	private float yieldRate;
	
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
	public String getQuoteDate() {
		return quoteDate;
	}
	public void setQuoteDate(String quoteDate) {
		this.quoteDate = quoteDate;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
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
	public float getCashYieldRate() {
		return cashYieldRate;
	}
	public void setCashYieldRate(float cashYieldRate) {
		this.cashYieldRate = cashYieldRate;
	}
	public float getStockYieldRate() {
		return stockYieldRate;
	}
	public void setStockYieldRate(float stockYieldRate) {
		this.stockYieldRate = stockYieldRate;
	}
	public float getYieldRate() {
		return yieldRate;
	}
	public void setYieldRate(float yieldRate) {
		this.yieldRate = yieldRate;
	}

}
