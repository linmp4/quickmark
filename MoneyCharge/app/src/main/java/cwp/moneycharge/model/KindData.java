package cwp.moneycharge.model;

public class KindData {
	private int kindname;
	private double amount;
	public KindData(int kindname,double amount) {
		// TODO Auto-generated constructor stub
		this.kindname=kindname;
		this.amount=amount;
	}
	public int getKindname() {
		return kindname;
	}
	public void setKindname(int kindname) {
		this.kindname = kindname;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}
