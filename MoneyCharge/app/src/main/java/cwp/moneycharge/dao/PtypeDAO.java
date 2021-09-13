package cwp.moneycharge.dao;

import cwp.moneycharge.model.Tb_ptype;
import cwp.moneycharge.dao.DBOpenHelper;
import java.util.List;
import java.util.ArrayList;

import com.cwp.cmoneycharge.R;

import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.Cursor;

public class PtypeDAO {
	// (_id INTEGER NOT NULL PRIMARY KEY,no not null integer ,typename
	// varchar(50)
	private DBOpenHelper helper;// 创建DBOpenHelper对象
	private SQLiteDatabase db;// 创建SQLiteDatabase对象

	int[] imageId = new int[] { R.drawable.icon_spjs_zwwc,
			R.drawable.icon_spjs_zwwc, R.drawable.icon_spjs_zwwc,
			R.drawable.icon_spjs_zwwc, R.drawable.icon_jjwy_rcyp,
			R.drawable.icon_xxyl_wg, R.drawable.icon_yfsp,
			R.drawable.icon_rqwl_slqk, R.drawable.icon_jltx_sjf,
			R.drawable.icon_spjs, R.drawable.icon_jrbx_ajhk,
			R.drawable.icon_jrbx, R.drawable.icon_xcjt_dczc,
			R.drawable.icon_qtzx, R.drawable.icon_jrbx_lxzc, R.drawable.yysb };

	public PtypeDAO(Context context) {
		// TODO Auto-generated constructor stub
		helper = new DBOpenHelper(context);// 初始化DBOpenHelper对象
	}

	/**
	 * 新增收入类型
	 * 
	 * @param tb_ptype
	 */
	public void add(Tb_ptype tb_ptype) {
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		db.execSQL(
				"insert into tb_ptype (_id,no,typename) values (?,?,?)",
				new Object[] { tb_ptype.get_id(), tb_ptype.getNo(),
						tb_ptype.getTypename() });// 执行添加支出类型操作

	}

	/**
	 * 修改收入类型
	 * 
	 * @param tb_ptype
	 */
	public void modify(Tb_ptype tb_ptype) {
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		db.execSQL(
				"update Tb_ptype set typename = ? where _id = ? and no=?",
				new Object[] { tb_ptype.get_id(), tb_ptype.getNo(),
						tb_ptype.getTypename() });// 执行修改支出类型操作
	}

	public void modifyByName(int id, String old, String now) {
		db = helper.getWritableDatabase();
		db.execSQL(
				"update Tb_ptype set typename = ? where _id = ? and typename=?",
				new Object[] { id, now, old });// 执行修改收入类型操作
	}

	/**
	 * 删除收入类型
	 * 
	 * @param ids
	 */
	public void delete(Integer... ids) {
		if (ids.length > 0)// 判断是否存在要删除的id
		{
			StringBuffer sb = new StringBuffer();// 创建StringBuffer对象
			for (int i = 0; i < ids.length - 1; i++)// 遍历要删除的id集合
			{
				sb.append('?').append(',');// 将删除条件添加到StringBuffer对象中
			}
			sb.deleteCharAt(sb.length() - 1);// 去掉最后一个“,“字符
			db = helper.getWritableDatabase();// 创建SQLiteDatabase对象
			// 执行删除便签信息操作
			db.execSQL("delete from tb_ptype where _id in (?) and no in (" + sb
					+ ")", (Object[]) ids);
		}
	}

	public void deleteByName(int id, String typename) {
		db = helper.getWritableDatabase();// 创建SQLiteDatabase对象
		// 执行删除便签信息操作
		db.execSQL("delete from tb_ptype where _id =? and typename=?",
				new Object[] { id, typename });
	}

	public void deleteById(int id) {
		db = helper.getWritableDatabase();// 创建SQLiteDatabase对象
		// 执行删除便签信息操作
		db.execSQL("delete from tb_ptype where _id =? ", new Object[] { id });
	}

	/**
	 * 获取收入类型信息
	 * 
	 * @param start
	 *            起始位置
	 * @param count
	 *            每页显示数量
	 * @return
	 */
	public List<Tb_ptype> getScrollData(int id, int start, int count) {
		List<Tb_ptype> lisTb_ptype = new ArrayList<Tb_ptype>();// 创建集合对象
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		// 获取所有便签信息
		Cursor cursor = db.rawQuery(
				"select * from tb_ptype where _id=? order by no limit ?,?",
				new String[] { String.valueOf(id), String.valueOf(start),
						String.valueOf(count) });
		while (cursor.moveToNext())// 遍历所有的便签信息
		{
			// 将遍历到的便签信息添加到集合中
			lisTb_ptype.add(new Tb_ptype(cursor.getInt(cursor
					.getColumnIndex("_id")), cursor.getInt(cursor
					.getColumnIndex("no")), cursor.getString(cursor
					.getColumnIndex("typename"))));
		}
		return lisTb_ptype;// 返回集合
	}

	/**
	 * 获取总记录数
	 * 
	 * @return
	 */
	public long getCount() {
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery("select count(no) from tb_ptype", null);// 获取支出类型的记录数
		if (cursor.moveToNext())// 判断Cursor中是否有数据
		{
			return cursor.getLong(0);// 返回总记录数
		}
		return 0;// 如果没有数据，则返回0
	}

	public long getCount(int id) {
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery(
				"select count(no) from tb_ptype where _id=?",
				new String[] { String.valueOf(id) });// 获取收入信息的记录数
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
	public int getMaxId() {
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery("select max(no) from tb_ptype", null);// 获取收入类型表中的最大编号
		while (cursor.moveToLast()) {// 访问Cursor中的最后一条数据
			return cursor.getInt(0);// 获取访问到的数据，即最大编号
		}
		return 0;// 如果没有数据，则返回0
	}

	/**
	 * 获取类型名数组 param id
	 * 
	 * @return
	 * */
	public List<String> getPtypeName(int id) {
		List<String> lisCharSequence = new ArrayList<String>();// 创建集合对象
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery(
				"select typename from tb_ptype where _id=?",
				new String[] { String.valueOf(id) });// 获取收入类型表中的最大编号
		while (cursor.moveToNext()) {// 访问Cursor中的最后一条数据
			lisCharSequence.add(cursor.getString(cursor
					.getColumnIndex("typename")));

		}
		return lisCharSequence;// 如果没有数据，则返回0

	}

	public String getOneName(int id, int no) {
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery(
				"select typename from tb_ptype where _id=? and no=?",
				new String[] { String.valueOf(id), String.valueOf(no) });
		if (cursor.moveToNext()) {
			return cursor.getString(cursor.getColumnIndex("typename"));
		}
		return "";
	}

	public int getOneImg(int id, int no) {
		if (imageId.length < no) {
			return imageId[14];
		}
		return imageId[no - 1];
	}

	public void initData(int id) {
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		db.execSQL("delete from tb_ptype where _id=?",
				new String[] { String.valueOf(id) }); // 确保无该id
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "1", "早餐" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "2", "午餐" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "3", "晚餐" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "4", "夜宵" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "5", "生活用品" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "6", "工作用品" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "7", "衣服" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "8", "应酬" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "9", "电子产品" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "10", "食品" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "11", "租金" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "12", "股票" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "13", "打的" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "14", "基金" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "15", "其他" });
		db.execSQL("insert into tb_ptype(_id,no,typename) values(?,?,?)",
				new String[] { String.valueOf(id), "16", "语音识别" });
	}
}
