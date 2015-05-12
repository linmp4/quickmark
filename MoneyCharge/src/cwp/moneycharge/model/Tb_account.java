package cwp.moneycharge.model;

public class Tb_account {
//_id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT ,username varchar(20),pwd varchar(50)
	private int _id ;
	private String username;
	private String pwd ; 
	public Tb_account( int id ,String username, String pwd ) {
		// TODO Auto-generated constructor stub
		super();
		this._id=id;
		this.pwd=pwd;
		this.username=username;
	}
	public Tb_account(){
		super();
	}
	public int get_id() {
		return _id;
	} 
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	

}
