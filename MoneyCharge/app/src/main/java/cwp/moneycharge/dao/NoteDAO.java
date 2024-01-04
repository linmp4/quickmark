/**
 * 
 */
package cwp.moneycharge.dao;

/**
 * @author cwpcc
 *
 */
import java.util.ArrayList;
import java.util.List;

import cwp.moneycharge.dao.DBOpenHelper;
import cwp.moneycharge.model.Tb_note;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NoteDAO {
//_id integer primary key,no integer autoincrement,note varchar(200))
	/**
	 * 
	 */
	private DBOpenHelper helper;// 创建DBOpenHelper对象
	private SQLiteDatabase db;// 创建SQLiteDatabase对象

	 
	public NoteDAO(Context context) {
		// TODO Auto-generated constructor stub
		helper = new DBOpenHelper(context);// 初始化DBOpenHelper对象
	}
	/**
	 * 添加便签信息
	 * 
	 * @param tb_note
	 */
	public void add(Tb_note tb_note) {
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		db.execSQL("insert into tb_note (_id,no,note) values (?,?,?)", new Object[] {
				tb_note.get_id(), tb_note.getNo(),tb_note.getNote() });// 执行添加便签信息操作
	}

	/**
	 * 更新便签信息
	 * 
	 * @param tb_note
	 */
	public void update(Tb_note tb_note) {
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		db.execSQL("update tb_note set note = ? where _id = ? and no=?", new Object[] {
				tb_note.getNote(), tb_note.get_id(),tb_note.getNo() });// 执行修改便签信息操作
	}

	/**
	 * 查找便签信息
	 * 
	 * @param id no
	 * @return
	 */
	public Tb_note find(int id,int no) {
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery(
				"select _id,no,note from tb_note where _id = ? and no=?",
				new String[] { String.valueOf(id) ,String.valueOf(no)});// 根据编号查找便签信息，并存储到Cursor类中
		if (cursor.moveToNext())// 遍历查找到的便签信息
		{
			// 将遍历到的便签信息存储到Tb_flag类中
			return new Tb_note(cursor.getInt(cursor.getColumnIndex("_id")),
					cursor.getInt(cursor.getColumnIndex("no")),
					cursor.getString(cursor.getColumnIndex("note")));
		}
		return null;// 如果没有信息，则返回null
	}

	/**
	 * 刪除便签信息
	 * 
	 * @param ids
	 */
	public void detele(Integer... ids) {
		if (ids.length > 0)// 判断是否存在要删除的id
		{
			StringBuffer sb = new StringBuffer();// 创建StringBuffer对象
			for (int i = 0; i < ids.length-1; i++)// 遍历要删除的id集合
			{
				sb.append('?').append(',');// 将删除条件添加到StringBuffer对象中
			}
			sb.deleteCharAt(sb.length() - 1);// 去掉最后一个“,“字符
			db = helper.getWritableDatabase();// 创建SQLiteDatabase对象
			// 执行删除便签信息操作
			db.execSQL("delete from tb_note where _id in (?) and no in (" + sb + ")",
					(Object[]) ids);
		}
	}

	/**
	 * 获取便签信息
	 * 
	 * @param start
	 *            起始位置
	 * @param count
	 *            每页显示数量
	 * @return
	 */
	public List<Tb_note> getScrollData(int id,int start, int count) {
		List<Tb_note> lisTb_note = new ArrayList<Tb_note>();// 创建集合对象
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		// 获取所有便签信息
		Cursor cursor = db.rawQuery("select _id,no,note from tb_note where _id=? order by no limit ?,?",
				new String[] { String.valueOf(id),  String.valueOf(start), String.valueOf(count) });
		while (cursor.moveToNext())// 遍历所有的便签信息
		{
			// 将遍历到的便签信息添加到集合中
			lisTb_note.add(new Tb_note(
					cursor.getInt(cursor.getColumnIndex("_id")),
					cursor.getInt(cursor.getColumnIndex("no")),
					cursor.getString(cursor.getColumnIndex("note"))));
		}
		return lisTb_note;// 返回集合
	}

	/**
	 * 获取总记录数
	 * 
	 * @return
	 */
	public long getCount(int id) {
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery("select count(no) from tb_note where _id=?",new String[]{String.valueOf(id)});// 获取便签信息的记录数
		if (cursor.moveToNext())// 判断Cursor中是否有数据
		{
			return cursor.getLong(0);// 返回总记录数
		}
		return 0;// 如果没有数据，则返回0
	}

	/**
	 * 获取便签最大编号
	 * 
	 * @return
	 */
	public int getMaxNo(int id) {
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery("select max(no) from tb_note where _id=?",new String[]{String.valueOf(id)});// 获取便签信息表中的最大编号
		while (cursor.moveToLast()) {// 访问Cursor中的最后一条数据
			return cursor.getInt(0);// 获取访问到的数据，即最大编号
		}
		return 0;// 如果没有数据，则返回0
	}
	public void deleteUserData(int id){//清空用户数据
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象 
		db.execSQL("delete from tb_note where _id=?",new Object[]{id});
	}
}
