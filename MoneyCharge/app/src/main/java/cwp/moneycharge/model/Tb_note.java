package cwp.moneycharge.model;

public class Tb_note {
//_id integer primary key,no integer autoincrement,flag varchar(200))
	private int _id;
	private int no;
	private String note;
	
	public Tb_note() {
		// TODO Auto-generated constructor stub
		super();
	}
	public Tb_note(int id,int no,String note) {
		 super();
		 this._id=id;
		 this.no=no;
		 this.note=note;
		 
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

	public void setNo(int no) {
		this.no = no;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
}
