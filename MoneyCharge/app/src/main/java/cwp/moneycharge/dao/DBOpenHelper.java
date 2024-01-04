package cwp.moneycharge.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final int VERSION=2;	//已经做出更新
	private static final String DBNAME="cmoneycharge.db";
	
	public DBOpenHelper(Context context){
		super(context,DBNAME,null,VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//用户表
		db.execSQL("create  table tb_account (_id INTEGER  PRIMARY KEY AUTOINCREMENT  NOT NULL,username varchar(20) ,pwd VARCHAR(50) DEFAULT '000000' NOT NULL)");
		//支出类型表
		db.execSQL("create  table tb_ptype(_id INTEGER   NOT NULL ,no integer not null,typename varchar(50) )");
		//收入类型表
		db.execSQL("create   table tb_itype(_id INTEGER   NOT NULL,no integer  not null ,typename varchar(50) )");
		// 支出信息表
		db.execSQL("create   table tb_pay (_id INTEGER  NOT NULL,no INTEGER  NOT NULL ,money decimal,time varchar(10),"
				+ "type integer,address varchar(100),mark varchar(200),photo varchar(200),kind varchar(10))");
		
		// 收入信息表
		db.execSQL("create   table tb_income (_id INTEGER   NOT NULL,no INTEGER  NOT NULL  ,money decimal,time varchar(10),"
				+ "type integer ,handler varchar(100),mark varchar(200),photo varchar(200),kind varchar(10))");
		
		// 便签信息表
		db.execSQL("create  table tb_note (_id integer  ,no integer ,note varchar(200))");
		db.execSQL("insert into tb_account(_id,username,pwd) values(100000001,\"默认用户\",\"000000\")");
		
		//初始化数据 收入类型表格
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"1","工资"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"2","还款"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"3","股票"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"4","还款"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"5","基金"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"6","分红"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"7","利息"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"8","兼职"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"9","奖金"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"10","租金"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"11","销售款"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"12","应收款"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"13","报销款"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"14","其他"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"15","礼金"});
		db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"16","语音识别"});
		//初始化数据 支出类型表格
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"1","早餐"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"2","午餐"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"3","晚餐"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"4","夜宵"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"5","生活用品"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"6","工作用品"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"7","衣服"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"8","应酬"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"9","电子产品"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"10","食品"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"11","租金"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"12","股票"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"13","打的"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"14","基金"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"15","其他"});
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(100000001),"16","语音识别"});
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 if(oldVersion < 2) {	//更新数据库
		        db.execSQL("ALTER TABLE tb_income ADD photo varchar(200)");
		        db.execSQL("ALTER TABLE tb_pay ADD photo varchar(200)");
		    }
	}
	
	public void droptable(SQLiteDatabase db){
		db.execSQL("drop table tb_itype");
		db.execSQL("drop table tb_ptype");
		db.execSQL("drop table tb_account");
		db.execSQL("drop table tb_income");
		db.execSQL("drop table tb_pay");
		db.execSQL("drop table tb_note");
	}
}
