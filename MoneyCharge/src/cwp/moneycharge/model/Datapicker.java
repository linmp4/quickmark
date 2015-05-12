package cwp.moneycharge.model;

public class Datapicker {
	private int no;
	private String time;
	private Double money;
	public Datapicker(int no,Double money,String time) {
		// TODO Auto-generated constructor stub
		this.no=no;
		this.time=time;
		this.money=money;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	
}
