package cwp.moneycharge.model;

public class Tb_itype { 
	//_id INTEGER  NOT NULL PRIMARY KEY,no not null integer AUTOINCREMENT ,typename varchar(50) 
	private int  _id;//用户id
	private int no;//收入类型id
	private String typename;//收入类型名称
	
	public Tb_itype() {
		// TODO Auto-generated constructor stub
		super();
	}
	public Tb_itype(int id,int no,String typename) {
		// TODO Auto-generated constructor stub
		super();
		this._id=id;
		this.no=no;
		this.typename=typename;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	
	public int getNo() {
		return no;
	} 
	public void set_No(int no) {
		this.no = no;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	
	
}
