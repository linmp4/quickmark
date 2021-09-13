package com.cwp.cmoneycharge;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cwp.chart.CustomMultiChoiceDialog;
import com.cwp.chart.MyAdspter;
import com.cwp.chart.SystemBarTintManager;

import cwp.moneycharge.dao.IncomeDAO;
import cwp.moneycharge.dao.ItypeDAO;
import cwp.moneycharge.dao.PayDAO;
import cwp.moneycharge.dao.PtypeDAO;
import cwp.moneycharge.model.Tb_income;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SearchActivity extends Activity {
	private String arrs[] = { "自定义时间段", "今天", "昨天", "本周", "上周", "本月", "上月",
			"本季", "上季", "本年" };
	private boolean boos[] = { false, false, false, false, false, false, false,
			false, false, false };
	private CustomMultiChoiceDialog.Builder multiChoiceDialogBuilder;

	public String contact_name;

	private CustomMultiChoiceDialog multiChoiceDialog;
	protected static final int DATE_DIALOG_ID = 0;// 创建日期对话框常量
	EditText query_dialog;
	ListView mListView;
	LinearLayout mlayout;
	private int userid;

	IncomeDAO incomeDAO = new IncomeDAO(this);
	PtypeDAO ptypeDAO = new PtypeDAO(this);
	ItypeDAO itypeDAO = new ItypeDAO(this);
	PayDAO payDAO = new PayDAO(this);// 创建PayDAO对象
	private String[] strInfos;
	private TextView searchincome;
	private TextView searchpay;
	private TextView seachbalance;
	private ArrayAdapter<String> arrayAdapter;
	private Editable text;
	private MyAdspter adapter;
	private TextView search_quit;
	private RelativeLayout search_more;
	private LinearLayout search_more_list;
	private RelativeLayout search_list_time;
	private TextView search_list_time_text;
	private RelativeLayout search_list_starttime;
	private static TextView search_starttime;
	private RelativeLayout search_list_endtime;
	private static TextView search_endtime;
	private RelativeLayout search_list_paytype;
	private TextView search_paytype;
	private RelativeLayout search_list_incometype;
	private TextView search_incometype;
	private List<String> spdatalistpay;
	private List<String> spdatalistincome;
	private String[] spdatapay;
	private String[] spdataincome;
	private boolean[] paychoice;
	private boolean[] incomechoice;
	protected String searchtype;
	public String[] resultpay;
	public String[] resultincome;
	private int mYear;
	private int mMonth;
	private int mDay;
	protected String timetype;
	private LinearLayout search_list_timeall;
	private RelativeLayout search_more_close;
	public int defaultMonth;
	public String dmonth;
	public int defaultDay;
	public String dday;
	private Calendar c;
	protected String searchstate = "quit";
	public boolean timeselect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_activity);

		SystemBarTintManager mTintManager;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		mTintManager = new SystemBarTintManager(this);
		mTintManager.setStatusBarTintEnabled(true);
		mTintManager.setStatusBarTintResource(R.color.statusbar_bg);

		init();

	}

	public void init() {
		Intent intentr = getIntent();
		userid = intentr.getIntExtra("cwp.id", 100000001);

		mlayout = (LinearLayout) findViewById(R.id.mlayout);// 获取金额文本框
		search_list_timeall = (LinearLayout) findViewById(R.id.search_list_timeall);// 获取金额文本框
		search_more_list = (LinearLayout) findViewById(R.id.search_more_list);// 获取金额文本框
		search_more = (RelativeLayout) findViewById(R.id.search_more);// 获取金额文本框
		search_more_close = (RelativeLayout) findViewById(R.id.search_more_close);// 获取金额文本框

		search_list_time = (RelativeLayout) findViewById(R.id.search_list_time);// 获取金额文本框
		search_list_starttime = (RelativeLayout) findViewById(R.id.search_list_starttime);// 获取金额文本框
		search_list_endtime = (RelativeLayout) findViewById(R.id.search_list_endtime);// 获取金额文本框
		search_list_paytype = (RelativeLayout) findViewById(R.id.search_list_paytype);// 获取金额文本框
		search_list_incometype = (RelativeLayout) findViewById(R.id.search_list_incometype);// 获取金额文本框

		mListView = (ListView) findViewById(R.id.mListView);// 获取金额文本框
		query_dialog = (EditText) findViewById(R.id.query_dialog);// 获取金额文本框
		searchincome = (TextView) findViewById(R.id.searchincome);// 获取金额文本框
		seachbalance = (TextView) findViewById(R.id.seachbalance);// 获取金额文本框
		searchpay = (TextView) findViewById(R.id.searchpay);// 获取金额文本框
		search_quit = (TextView) findViewById(R.id.search_quit);// 获取金额文本框

		search_list_time_text = (TextView) findViewById(R.id.search_list_time_text);// 获取金额文本框
		search_starttime = (TextView) findViewById(R.id.search_starttime);// 获取金额文本框
		search_endtime = (TextView) findViewById(R.id.search_endtime);// 获取金额文本框
		search_paytype = (TextView) findViewById(R.id.search_paytype);// 获取金额文本框
		search_incometype = (TextView) findViewById(R.id.search_incometype);// 获取金额文本框

		c = Calendar.getInstance();// 获取当前系统日期
		mYear = c.get(Calendar.YEAR);// 获取年份
		mMonth = c.get(Calendar.MONTH) + 1;// 获取月份
		mDay = c.get(Calendar.DAY_OF_MONTH);// 获取天数

		search_list_endtime.setOnTouchListener(new OnTouchListener() { // 为时间文本框设置单击监听事件
					@Override
					public boolean onTouch(View v, MotionEvent event) {

						showDialog(DATE_DIALOG_ID);// 显示日期选择对话框
						timetype = "end";
						return false;
					}
				});

		search_list_starttime.setOnTouchListener(new OnTouchListener() { // 为时间文本框设置单击监听事件
					@Override
					public boolean onTouch(View v, MotionEvent event) {

						showDialog(DATE_DIALOG_ID);// 显示日期选择对话框
						timetype = "start";
						return false;
					}
				});

		spdatalistpay = ptypeDAO.getPtypeName(userid);
		spdatalistincome = itypeDAO.getItypeName(userid);
		spdatapay = spdatalistpay.toArray(new String[spdatalistpay.size()]);// 在tb_itype中按用户id读取
		spdataincome = spdatalistincome.toArray(new String[spdatalistincome
				.size()]);// 在tb_itype中按用户id读取

		search_list_time.setOnClickListener(new OnClickListener() { // 高级搜索时间列表

					@Override
					public void onClick(View v) {
						showMultiChoiceDialog(v, null);
					}
				});

		search_list_paytype.setOnClickListener(new OnClickListener() {// 高级搜索支出列表

					@Override
					public void onClick(View v) {
						showMultiChoiceDialog(v, "pay");
						searchtype = "pay";
					}
				});

		search_list_incometype.setOnClickListener(new OnClickListener() {// 高级搜索收入列表

					@Override
					public void onClick(View v) {
						showMultiChoiceDialog(v, "income");
						searchtype = "income";
					}
				});

		search_more_close.setOnClickListener(new OnClickListener() { // 按了更多选项
					@Override
					public void onClick(View v) {
						closemore();
					}
				});

		search_more.setOnClickListener(new OnClickListener() { // 按了更多选项
					@Override
					public void onClick(View v) {
						InputMethodManager imm = (InputMethodManager) getApplicationContext()
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(v.getWindowToken(), 0); // 强制关闭软键盘
						search_more.setVisibility(View.GONE);
						mlayout.setVisibility(View.GONE);
						search_more_list.setVisibility(View.VISIBLE);
						search_quit.setText("开始查询");
						searchstate = "search";
						search_starttime.setText(mYear + "-01-01");
						search_endtime.setText(mYear + "-12-31");
						search_paytype.setText("");
						search_incometype.setText("");
						resultincome = null;
						resultpay = null;
						paychoice = null;
						incomechoice = null;

					}
				});

		class OnClickListenernormal implements OnClickListener { // 执行查询
			private String String;

			@Override
			public void onClick(View v) {
				if (searchstate == "quit") {
					SearchActivity.this.finish();
				} else {
					if (timeselect) {
						try {
							DateCompare(search_starttime.getText(),
									search_endtime.getText());

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// System.out.println("timeselect "
						// + search_starttime.getText() + " "
						// + search_endtime.getText());
					} else {
						// System.out.println("2 " + search_starttime.getText()
						// + " " + search_endtime.getText());
					}
					if (resultincome == null) {
						resultincome = new String[1];
						resultincome[0] = (String) search_incometype.getText();
					}
					if (resultpay == null) {
						resultpay = new String[1];
						resultpay[0] = (String) search_paytype.getText();
					}
					// for (int i = 0; i < resultincome.length; i++) {
					// System.out.println("收入 " + resultincome[i]);
					// }
					// for (int i = 0; i < resultpay.length; i++) {
					// System.out.println("支出 " + resultpay[i]);
					// }
					if (text == null) {
						String = "";
					} else {
						String = text.toString();
					}
					List<Tb_income> listinfosall = incomeDAO.searchALL(userid,
							search_starttime.getText() + "",
							search_endtime.getText() + "", resultpay,
							resultincome, String);
					closemore();
					dispay(listinfosall);
				}
			}
		}

		search_quit.setOnClickListener(new OnClickListenernormal());

		mListView.setOnItemClickListener(new OnItemClickListener()// 为ListView添加项单击事件
				{
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						TextView txno = (TextView) view.findViewById(R.id.no);
						TextView txkind = (TextView) view
								.findViewById(R.id.kind);
						String strtype = ((String) txkind.getText()).substring(
								1, ((String) txkind.getText()).indexOf(']'))
								.trim();// 从收入信息中截取收支类型
						String strno = (String) txno.getText(); // 从信息中截取收支编号
						Intent intent = new Intent(SearchActivity.this,
								AddPay.class);// 创建Intent对象
						if (strtype.equals("收入")) {
							intent.putExtra("cwp.message", new String[] {
									strno, "btnininfo" });// 设置传递数据
						}
						if (strtype.equals("支出")) {
							intent.putExtra("cwp.message", new String[] {
									strno, "btnoutinfo" });// 设置传递数据
						}
						intent.putExtra("cwp.id", userid);
						intent.putExtra("cwp.search", "search");
						startActivityForResult(intent, 101);// 执行Intent操作
					}
				});
		query_dialog.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// text 输入框中改变前的字符串信息
				// start 输入框中改变前的字符串的起始位置
				// count 输入框中改变前后的字符串改变数量一般为0
				// after 输入框中改变后的字符串与起始位置的偏移量

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// text 输入框中改变后的字符串信息
				// start 输入框中改变后的字符串的起始位置
				// before 输入框中改变前的字符串的位置 默认为0
				// count 输入框中改变后的一共输入字符串的数量
			}

			@Override
			public void afterTextChanged(Editable s) {
				// edit 输入结束呈现在输入框中的信息
				text = s;
				update(text.toString());
			}
		});
	}

	protected void closemore() {
		InputMethodManager imm = (InputMethodManager) getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		search_more.setVisibility(View.VISIBLE);
		search_more_list.setVisibility(View.GONE);
		search_quit.setText("取消");
		searchstate = "quit";
		search_more.setBackgroundColor(Color.parseColor("#00000000"));
	}

	int getQuarterInMonth(int month, boolean isQuarterStart) {
		int months[] = { 1, 4, 7, 10 };
		if (!isQuarterStart) {
			months = new int[] { 3, 6, 9, 12 };
		}
		if (month >= 2 && month <= 4)
			return months[0];
		else if (month >= 5 && month <= 7)
			return months[1];
		else if (month >= 8 && month <= 10)
			return months[2];
		else
			return months[3];
	}

	public void showMultiChoiceDialog(View view, String type) { // 类型选择
		paychoice = new boolean[spdatalistpay.size()];
		incomechoice = new boolean[spdatalistincome.size()];
		for (int i = 0; i < paychoice.length; i++) {
			paychoice[i] = false;
		}
		for (int i = 0; i < incomechoice.length; i++) {
			incomechoice[i] = false;
		}
		multiChoiceDialogBuilder = new CustomMultiChoiceDialog.Builder(this);
		if (type == "pay") {
			multiChoiceDialog = multiChoiceDialogBuilder
					.setTitle("收入类别")
					.setMultiChoiceItems(true, spdatapay, paychoice, null, true)
					.setPositiveButton("确定", new PositiveClickListener())
					.setNegativeButton("取消", null).create();
		} else if (type == "income") {
			multiChoiceDialog = multiChoiceDialogBuilder
					.setTitle("支出类别")
					.setMultiChoiceItems(true, spdataincome, incomechoice,
							null, true)
					.setPositiveButton("确定", new PositiveClickListener())
					.setNegativeButton("取消", null).create();
		} else {
			multiChoiceDialog = multiChoiceDialogBuilder.setTitle("时间范围")
					.setMultiChoiceItems(false, arrs, boos, new onitem(), true)
					.setPositiveButton(null, null)
					.setNegativeButton(null, null).create();
		}
		multiChoiceDialog.getWindow().setBackgroundDrawable(
				new BitmapDrawable());
		multiChoiceDialog.show();
	}

	class PositiveClickListener implements DialogInterface.OnClickListener { // 点确定按钮
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (CustomMultiChoiceDialog.Builder.getisMultiChoice()) {
				if (searchtype == "pay") {
					boos = multiChoiceDialogBuilder.getCheckedItems();
					int j1 = 0, j2 = 0;
					for (int i = 0; i < boos.length; i++) {
						if (boos[i]) {
							j1++;
						}
					}
					resultpay = new String[j1];
					for (int i = 0; i < boos.length; i++) {
						if (boos[i]) {
							resultpay[j2] = spdatapay[i];
							j2++;
						}
					}
					if (j1 > 1) {
						search_paytype.setText(resultpay[0] + ","
								+ resultpay[1] + "...");
					} else {
						if (resultpay.length != 0) {
							search_paytype.setText(resultpay[0]);
						} else {
							search_paytype.setText("早餐");
						}
					}
				} else if (searchtype == "income") {
					boos = multiChoiceDialogBuilder.getCheckedItems();
					int j1 = 0, j2 = 0;
					for (int i = 0; i < boos.length; i++) {
						if (boos[i]) {
							j1++;
						}
					}
					resultincome = new String[j1];
					for (int i = 0; i < boos.length; i++) {
						if (boos[i]) {
							resultincome[j2] = spdataincome[i];
							j2++;
						}
					}
					if (j1 > 1) {
						search_incometype.setText(resultincome[0] + ","
								+ resultincome[1] + "...");
					} else {
						if (resultincome.length != 0) {
							search_incometype.setText(resultincome[0]);
						} else {
							search_incometype.setText("早餐");
						}
					}
				}

			} else {
				String s = CustomMultiChoiceDialog.Builder.getcontact_name();
				// alert(MainActivity.this, s);
			}

		}
	}

	class onitem implements OnItemClickListener { // 时间范围响应事件

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			TextView cn = (TextView) view.findViewById(R.id.contact_name);
			search_list_time_text.setText(cn.getText());
			switch (position) {
			case 0: // 自定义时间
				search_starttime.setText(mYear + "-01-01");
				search_endtime.setText(mYear + "-12-31");
				search_list_timeall.setVisibility(view.VISIBLE);
				timeselect = true;
				break;
			case 1:// 今天
				search_list_timeall.setVisibility(view.GONE);
				timeselect = false;
				search_starttime.setText(incomeDAO.gettime(mYear, mMonth, mDay,
						false, false));
				search_endtime.setText(incomeDAO.gettime(mYear, mMonth, mDay,
						false, false));
				break;
			case 2:// 昨天
				search_list_timeall.setVisibility(view.GONE);
				timeselect = false;
				if (mDay == 1) {
					search_starttime.setText(incomeDAO.gettime(mYear,
							mMonth - 1, mDay, false, true));
					search_endtime.setText(incomeDAO.gettime(mYear, mMonth - 1,
							mDay, false, true));
				} else {
					search_starttime.setText(incomeDAO.gettime(mYear, mMonth,
							mDay - 1, false, false));
					search_endtime.setText(incomeDAO.gettime(mYear, mMonth,
							mDay - 1, false, false));
				}
				// System.out.println(search_starttime.getText() + "  "
				// + search_endtime.getText());
				break;
			case 3:// 本周
				search_list_timeall.setVisibility(view.GONE);
				timeselect = false;
				int n = 0;
				// n为推迟的周数，0本周，-1向前推迟一周，1下周，依次类推
				c.add(Calendar.DATE, n * 7);
				// 想周几，这里就传几Calendar.MONDAY（TUESDAY...）
				c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				search_starttime.setText(incomeDAO.gettime(
						c.get(Calendar.YEAR), (c.get(Calendar.MONTH) + 1),
						c.get(Calendar.DAY_OF_MONTH), false, false));
				c.add(Calendar.DATE, 6);
				search_endtime.setText(incomeDAO.gettime(c.get(Calendar.YEAR),
						(c.get(Calendar.MONTH) + 1),
						c.get(Calendar.DAY_OF_MONTH), false, false));
				break;
			case 4:// 上周
				timeselect = false;
				search_list_timeall.setVisibility(view.GONE);
				// n为推迟的周数，0本周，-1向前推迟一周，1下周，依次类推
				c.add(Calendar.DATE, (-1) * 7);
				// 想周几，这里就传几Calendar.MONDAY（TUESDAY...）
				c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				search_starttime.setText(incomeDAO.gettime(
						c.get(Calendar.YEAR), (c.get(Calendar.MONTH) + 1),
						c.get(Calendar.DAY_OF_MONTH), false, false));
				c.add(Calendar.DATE, 6);
				search_endtime.setText(incomeDAO.gettime(c.get(Calendar.YEAR),
						(c.get(Calendar.MONTH) + 1),
						c.get(Calendar.DAY_OF_MONTH), false, false));
				break;
			case 5:// 本月
				timeselect = false;
				search_list_timeall.setVisibility(view.GONE);
				search_starttime.setText(incomeDAO.gettime(mYear, mMonth, mDay,
						true, false));
				search_endtime.setText(incomeDAO.gettime(mYear, mMonth, mDay,
						false, true));
				break;
			case 6:// 上月
				timeselect = false;
				search_list_timeall.setVisibility(view.GONE);
				search_starttime.setText(incomeDAO.gettime(mYear, mMonth - 1,
						mDay, true, false));
				search_endtime.setText(incomeDAO.gettime(mYear, mMonth - 1,
						mDay, false, true));
				break;
			case 7:// 本季
				timeselect = false;
				search_list_timeall.setVisibility(view.GONE);
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(new Date());
				int month = getQuarterInMonth(calendar.get(Calendar.MONTH) + 1,
						true);
				calendar.set(Calendar.MONTH, month);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				search_starttime.setText(incomeDAO.gettime(
						calendar.get(Calendar.YEAR),
						(calendar.get(Calendar.MONTH) + 1),
						calendar.get(Calendar.DAY_OF_MONTH), false, false));
				// 季度末
				calendar.setTime(new Date());
				month = getQuarterInMonth(calendar.get(Calendar.MONTH) + 1,
						false);
				calendar.set(Calendar.MONTH, month + 1);
				calendar.set(Calendar.DAY_OF_MONTH, 0);
				search_endtime.setText(incomeDAO.gettime(
						calendar.get(Calendar.YEAR),
						(calendar.get(Calendar.MONTH) + 1),
						calendar.get(Calendar.DAY_OF_MONTH), false, false));
				break;
			case 8:// 上季
				timeselect = false;
				search_list_timeall.setVisibility(view.GONE);
				Calendar calendar2 = new GregorianCalendar();
				calendar2.setTime(new Date());
				int month2 = getQuarterInMonth(
						calendar2.get(calendar2.MONTH) + 1, true);
				calendar2.set(calendar2.MONTH, month2 - 3);
				calendar2.set(calendar2.DAY_OF_MONTH, 1);
				search_starttime.setText(incomeDAO.gettime(
						calendar2.get(calendar2.YEAR),
						(calendar2.get(calendar2.MONTH) + 1),
						calendar2.get(calendar2.DAY_OF_MONTH), false, false));
				calendar2.setTime(new Date());
				month2 = getQuarterInMonth(calendar2.get(calendar2.MONTH) + 1,
						false);
				calendar2.set(calendar2.MONTH, month2 - 2);
				calendar2.set(calendar2.DAY_OF_MONTH, 0);
				search_endtime.setText(incomeDAO.gettime(
						calendar2.get(calendar2.YEAR),
						(calendar2.get(calendar2.MONTH) + 1),
						calendar2.get(calendar2.DAY_OF_MONTH), false, false));
				break;
			case 9:// 上年
				timeselect = false;
				search_list_timeall.setVisibility(view.GONE);
				search_starttime.setText(incomeDAO.gettime(mYear - 1, 1, 1,
						false, false));
				search_endtime.setText(incomeDAO.gettime(mYear - 1, 12, 31,
						false, false));
				break;
			}
			multiChoiceDialog.dismiss();
		}
	}

	protected void update(String string) {
		List<Tb_income> listinfos2 = incomeDAO.search(userid, string);
		dispay(listinfos2);
	}

	private void dispay(List<Tb_income> listinfos2) {
		mlayout.setVisibility(View.VISIBLE);
		search_more.setBackgroundColor(Color.parseColor("#e8e8e8"));
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int m = 0, income = 0, pay = 0;// 定义一个开始标识
		strInfos = new String[listinfos2.size()];
		if (listinfos2.size() == 0) { // 处理listview
			seachbalance.setText("￥ 0.0");
			searchpay.setText("￥ 0.0");
			searchincome.setText("￥ 0.0");
			mListView.setVisibility(View.GONE);
		} else {
			mListView.setVisibility(View.VISIBLE);
			for (Tb_income tb_income : listinfos2) {// 遍历List泛型集合
				// 将收入相关信息组合成一个字符串，存储到字符串数组的相应位置
				if (tb_income.getKind().equals("收入")) { // 收入
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("img",
							itypeDAO.getOneImg(userid, tb_income.getType()));
					map.put("no", tb_income.getNo() + "");
					map.put("kind", "[" + tb_income.getKind() + "]");
					map.put("address", tb_income.getHandler());
					map.put("money", "￥ " + tb_income.getMoney2() + "元");
					map.put("title",
							itypeDAO.getOneName(userid, tb_income.getType()));
					map.put("info", tb_income.getTime());
					map.put("date",
							FragmentPage3.gofordate(tb_income.getTime()));
					list.add(map);
					income += tb_income.getMoney();
					m++;// 标识加1
				} else { // 支出
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("img",
							ptypeDAO.getOneImg(userid, tb_income.getType()));
					map.put("no", tb_income.getNo() + "");
					map.put("kind", "[" + tb_income.getKind() + "]");
					map.put("address", tb_income.getHandler());
					map.put("money", "￥ " + tb_income.getMoney2() + "元");
					map.put("title",
							ptypeDAO.getOneName(userid, tb_income.getType()));
					map.put("info", tb_income.getTime());
					map.put("date",
							FragmentPage3.gofordate(tb_income.getTime()));
					list.add(map);
					pay += tb_income.getMoney();
					m++;// 标识加1
				}
			}
			seachbalance.setText("￥ " + String.valueOf(income - pay));
			searchpay.setText("￥ " + String.valueOf(-pay));
			searchincome.setText("￥ " + String.valueOf(income));
			adapter = new MyAdspter(this, list, true);
			mListView.setAdapter(adapter);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 3) {
			if (text != null) {
				update(text.toString());
			}
		}
	}

	DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;// 为年份赋值
			mMonth = monthOfYear;// 为月份赋值
			mDay = dayOfMonth;// 为天赋值
			updateDisplay();// 显示设置的日期
		}
	};

	@Override
	protected Dialog onCreateDialog(int id)// 重写onCreateDialog方法
	{
		switch (id) {
		case DATE_DIALOG_ID:// 弹出日期选择对话框
			return new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
					mDateSetListener, mYear, mMonth, mDay);
		}
		return null;
	}

	private void updateDisplay() {
		if (timetype == "start") {
			search_starttime.setText(incomeDAO.gettime(mYear, mMonth + 1, mDay,
					false, false));
		} else {
			search_endtime.setText(incomeDAO.gettime(mYear, mMonth + 1, mDay,
					false, false));
		}
	}

	public static void DateCompare(CharSequence s1, CharSequence s2)
			throws Exception {
		// 设定时间的模板
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 得到指定模范的时间
		Date d1 = sdf.parse(s1 + "");
		Date d2 = sdf.parse(s2 + "");
		// 比较
		if (d1.getTime() - d2.getTime() > 0) {
			CharSequence a = search_starttime.getText();
			search_starttime.setText(search_endtime.getText());
			search_endtime.setText(a);
		}
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

}
