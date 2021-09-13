package cwp.moneycharge.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cwp.moneycharge.model.*;

public class AccountDAO {
	private DBOpenHelper helper;// 创建DBOpenHelper对象

	private SQLiteDatabase db;// 创建SQLiteDatabase对象

	public AccountDAO(Context context)// 定义构造函数
	{
		helper = new DBOpenHelper(context);// 初始化DBOpenHelper对象

	}

	/**
	 * 添加密码信息
	 * 
	 * @param tb_account
	 */
	public int add(Tb_account tb_account) {

		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		// 执行添加密码操作
		if (tb_account.get_id() == 0) {
			db.execSQL(
					"insert into tb_account (username,pwd) values (?,?)",
					new Object[] { tb_account.getUsername(),tb_account.getPwd() });
		} else {
			db.execSQL(
					"insert into tb_account (_id,username,pwd) values (?,?,?)",
					new Object[] { tb_account.get_id(),tb_account.getUsername(), tb_account.getPwd() });
		}
		Cursor cursor = db.rawQuery(
				"select _id from tb_account where username=? and pwd=?",
				new String[] { tb_account.getUsername(), tb_account.getPwd() });
		if (cursor.moveToNext())// 遍历查找到的密码信息
		{
			// 将密码存储到Tb_account类中
			return cursor.getInt(cursor.getColumnIndex("_id"));
		} else
			return 100000001;
	}

	/**
	 * 设置密码信息
	 * 
	 * @param tb_account
	 */
	public void update(int id, String username, String pwd) {
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		// 执行修改密码操作
		db.execSQL("update tb_account set username=? , pwd = ? where _id=?  ",
				new String[] { username, pwd, String.valueOf(id) });
	}

	public void deleteById(int id) {
		db = helper.getWritableDatabase();// 创建SQLiteDatabase对象
		// 执行删除便签信息操作
		db.execSQL("delete from tb_account where _id =? ", new Object[] { id });
	}

	/**
	 * 查找密码信息
	 * 
	 * @return
	 */
	public Tb_account find(String username, String pwd) {
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		// 查找密码并存储到Cursor类中
		Cursor cursor = db
				.rawQuery(
						"select _id, username, pwd from tb_account where pwd=? and username=? ",
						new String[] { pwd, username });
		if (cursor.moveToNext())// 遍历查找到的密码信息
		{
			// 将密码存储到Tb_account类中
			return new Tb_account(cursor.getInt(cursor.getColumnIndex("_id")),
					cursor.getString(cursor.getColumnIndex("username")),
					cursor.getString(cursor.getColumnIndex("pwd")));
		}
		return null;// 如果没有信息，则返回null
	}

	public String find(int id) {
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		// 查找密码并存储到Cursor类中
		Cursor cursor = db.rawQuery(
				"select username from tb_account where _id=? ",
				new String[] { String.valueOf(id) });
		if (cursor.moveToNext())// 遍历查找到的密码信息
		{
			// 将密码存储到Tb_account类中
			return cursor.getString(cursor.getColumnIndex("username"));

		} else
			return "无名氏";// 如果没有信息，则返回null
	}

	public long getCount() {
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery("select count(_id) from tb_account ", null);// 获取密码信息的记录数
		if (cursor.moveToNext())// 判断Cursor中是否有数据
		{
			return cursor.getLong(0);// 返回总记录数
		}
		return 0;// 如果没有数据，则返回0
	}

}