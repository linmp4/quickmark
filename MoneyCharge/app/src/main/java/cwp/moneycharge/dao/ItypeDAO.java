package cwp.moneycharge.dao;

import cwp.moneycharge.model.Datapicker;
import cwp.moneycharge.model.Tb_itype; 
import cwp.moneycharge.dao.DBOpenHelper;
import java.util.List;
import java.util.ArrayList;

import com.cwp.cmoneycharge.R;

import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.Cursor;


/**
 * @author cwpcc
 *
 */ 
public class ItypeDAO {
//(_id INTEGER  NOT NULL PRIMARY KEY,no not null int ,typename varchar(50) 
	private DBOpenHelper helper;// 创建DBOpenHelper对象
	private SQLiteDatabase db;// 创建SQLiteDatabase对象

	int[] imageId = new int[] { R.drawable.icon_zysr_gzsr,
			R.drawable.icon_qtsr_zjsr, R.drawable.icon_zysr_tzsr,
			R.drawable.icon_qtsr, R.drawable.icon_qtzx,
			R.drawable.icon_jrbx_xfss, R.drawable.icon_zysr_lxsr,
			R.drawable.icon_zysr_jzsr, R.drawable.icon_zysr_jjsr,
			R.drawable.icon_lyyp_hwzb, R.drawable.icon_qtsr_jysd,
			R.drawable.icon_qtsr_jysd, R.drawable.icon_qtsr_jysd,
			R.drawable.icon_qtsr, R.drawable.icon_qtsr_ljsr, R.drawable.yysb };
	
	public ItypeDAO(Context context) {
		// TODO Auto-generated constructor stub
		helper = new DBOpenHelper(context);// 初始化DBOpenHelper对象
	}

/**	
 * 新增收入类型
 * @param Tb_itype
*/
	public void add(Tb_itype tb_itype){
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		db.execSQL("insert into tb_itype (_id,no,typename) values (?,?,?)", new Object[] {
				tb_itype.get_id(),tb_itype.getNo(),tb_itype.getTypename()});// 执行添加收入类型操作
		
	}
	/**	
	 * 修改收入类型
	 * @param Tb_itype
	*/	
		public void modify(Tb_itype tb_itype){
			db=helper.getWritableDatabase();// 初始化SQLiteDatabase对象
			db.execSQL("update tb_itype set typename = ? where _id = ? and no=?", new Object[] {
					tb_itype.get_id(), tb_itype.getNo(),tb_itype.getTypename() });// 执行修改收入类型操作
		}
	/**	
	 * 删除收入类型
	 * @param  ids
	*/	
		public void delete(Integer... ids){
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
				db.execSQL("delete from tb_itype where _id in (?) and no in (" + sb + ")",
						(Object[]) ids);
			}
		}

		public void deleteByName(int id,String typename){
			db = helper.getWritableDatabase();// 创建SQLiteDatabase对象
			// 执行删除便签信息操作
			db.execSQL("delete from tb_itype where _id =? and typename=?", new Object[] { id, typename});
		}

		public void deleteById(int id){
			db = helper.getWritableDatabase();// 创建SQLiteDatabase对象
			// 执行删除便签信息操作
			db.execSQL("delete from tb_itype where _id =? ", new Object[] { id});
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
		public List<Tb_itype> getScrollData(int id,int start, int count) {
			List<Tb_itype> lisTb_itype = new ArrayList<Tb_itype>();// 创建集合对象
			db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
			// 获取所有便签信息
			Cursor cursor = db.rawQuery("select * from tb_itype where _id=? order by no limit ?,?",
					new String[] { String.valueOf(id),  String.valueOf(start), String.valueOf(count) });
			while (cursor.moveToNext())// 遍历所有的便签信息
			{
				// 将遍历到的便签信息添加到集合中
				lisTb_itype.add(new Tb_itype(
						cursor.getInt(cursor.getColumnIndex("_id")),
						cursor.getInt(cursor.getColumnIndex("no")),
						cursor.getString(cursor.getColumnIndex("typename"))));
			}
			return lisTb_itype;// 返回集合
		}
		
		/**
		 * 获取总记录数
		 * 
		 * @return
		 */
		public long getCount() {
			db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
			Cursor cursor = db.rawQuery("select count(no) from tb_itype", null);// 获取收入类型的记录数
			if (cursor.moveToNext())// 判断Cursor中是否有数据
			{
				return cursor.getLong(0);// 返回总记录数
			}
			return 0;// 如果没有数据，则返回0
		}
		public long getCount(int id) {
			db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
			Cursor cursor = db
					.rawQuery("select count(no) from tb_itype where _id=?", new String[]{String.valueOf(id)});// 获取收入信息的记录数
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
			Cursor cursor = db.rawQuery("select max(no) from tb_itype", null);// 获取收入类型表中的最大编号
			while (cursor.moveToLast()) {// 访问Cursor中的最后一条数据
				return cursor.getInt(0);// 获取访问到的数据，即最大编号
			}
			return 0;// 如果没有数据，则返回0
		}
		/**
		 * 获取类型名数组
		 * param id
		 * @return
		 * */
		public List<String> getItypeName(int id){
			List<String> lisString = new ArrayList<String>();// 创建集合对象
			db = helper.getWritableDatabase();
			Cursor cursor = db.rawQuery("select typename from tb_itype where _id=?",new String[]{String.valueOf(id) } );
			while (cursor.moveToNext()) {// 访问Cursor中的最后一条数据
				lisString.add(cursor.getString(cursor.getColumnIndex("typename")));
				
			}
			return lisString;
			
		}
		public String getOneName(int id,int no){
			db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
			Cursor cursor = db.rawQuery("select typename from tb_itype where _id=? and no=?",new String[]{String.valueOf(id),String.valueOf(no) } ); 
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
		
		//获取指定时间内数据库数据
		
		public List<Datapicker> getDataOnDay(int id,String date1,String date2){
			db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象 
			List<Datapicker> datapicker = new ArrayList<Datapicker>();// 创建集合对象
			String sql= "'"+date1+"' and '"+date2+"'";		
			Cursor cursor = db.rawQuery("select no, time,sum(money) as tmoney from tb_pay where time between "+sql+" and _id =? group BY time order by time",new String[]{String.valueOf(id)} );// 获取支出信息表中的最大编号
			while (cursor.moveToNext())// 遍历所有的支出信息
			{		
			 
				// 将遍历到的支出信息添加到集合中
					datapicker.add(new Datapicker( 
						cursor.getInt(cursor.getColumnIndex("no")),
						cursor.getDouble(cursor.getColumnIndex("tmoney")),
						cursor.getString(cursor.getColumnIndex("time"))));
			}
			return datapicker;// 返回集合
				// 将遍历到的支出信息添加到集合中
				 
			} // 返回集合
		 
		public void modifyByName(int id,String old,String now){
			db = helper.getWritableDatabase();
			db.execSQL("update tb_itype set typename = ? where _id = ? and typename=?", new Object[] {
					id, now,old});// 执行修改收入类型操作
		}
		
		public void initData(int id) { 

			db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象    
			
			// 便签信息表  
			db.execSQL("delete from tb_itype where _id=?",new String[]{String.valueOf(id)}); // 确保无该id
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"1","工资"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"2","中奖"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"3","股票"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"4","还款"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"5","基金"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"6","分红"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"7","利息"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"8","兼职"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"9","奖金"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"10","租金"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"11","销售款"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"12","应收款"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"13","报销款"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"14","其他"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"15","礼金"});
			db.execSQL("insert into tb_itype(_id,no,typename) values(?,?,?)",new String[]{String.valueOf(id),"16","语音识别"});
		}
}
