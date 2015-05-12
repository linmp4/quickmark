package com.cwp.cmoneycharge;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cwp.chart.FoldingLayout;
import com.cwp.chart.PullToRefreshView;
import com.cwp.chart.PullToRefreshView.OnFooterRefreshListener;
import com.cwp.chart.PullToRefreshView.OnHeaderRefreshListener;
import com.cwp.chart.RiseNumberTextView;

import cwp.moneycharge.dao.IncomeDAO;
import cwp.moneycharge.dao.ItypeDAO;
import cwp.moneycharge.dao.PayDAO;
import cwp.moneycharge.dao.PtypeDAO;
import cwp.moneycharge.model.KindData;
import cwp.moneycharge.model.Tb_income;
import cwp.moneycharge.model.Tb_pay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("InflateParams")
public class FragmentPage3 extends Fragment implements OnHeaderRefreshListener,
		OnFooterRefreshListener {

	TextView sum_pay, sum_income, sum_title, February_date;
	ListView scrollview;
	ArrayAdapter<String> arrayAdapter = null;// 创建ArrayAdapter对象
	String[] strInfos = null;// 定义字符串数组，用来存储收入信息
	Integer[] newStr = null;
	private PullToRefreshView mPullToRefreshView;

	PayDAO payDAO;
	IncomeDAO incomeDAO;
	List<Tb_pay> list_pay;
	List<Tb_income> list_income;
	List<KindData> KindDatap;
	PtypeDAO ptypeDAO;
	ItypeDAO itypeDAO;
	static int defaultYear;
	int defaultMonth;
	int defaultDay;
	int userid, height;
	String dmonth, dday, date1, date2;
	List<Integer> list2;
	Animation pushup, pushout;
	RelativeLayout searchButton;
	LinearLayout frag3, fragall;
	private ExpandableListView elv;
	private ArrayList<Map<String, String>> groups;
	private ArrayList<List<Map<String, String>>> childs;
	private ArrayList<Map<String, String>> child1;
	private RiseNumberTextView sum_total;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_3, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		final Calendar c = Calendar.getInstance();// 获取当前系统日期
		defaultYear = c.get(Calendar.YEAR);// 获取年份
		defaultMonth = c.get(Calendar.MONTH) + 1;// 获取月份

		findViews();

		initData(0);

		elv.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View view,
					int groupPosition, int childPosition, long id) {
				TextView txno = (TextView) view.findViewById(R.id.no);
				TextView txkind = (TextView) view.findViewById(R.id.kind);
				String strtype = ((String) txkind.getText()).substring(1,
						((String) txkind.getText()).indexOf(']')).trim();// 从收入信息中截取收支类型
				String strno = (String) txno.getText(); // 从信息中截取收支编号
				Intent intent = new Intent(getActivity(), AddPay.class);// 创建Intent对象
				if (strtype.equals("收入")) {
					intent.putExtra("cwp.message", new String[] { strno,
							"btnininfo" });// 设置传递数据
				}
				if (strtype.equals("支出")) {
					intent.putExtra("cwp.message", new String[] { strno,
							"btnoutinfo" });// 设置传递数据
				}
				intent.putExtra("cwp.id", userid);
				intent.putExtra("cwp.frament3", "3");
				startActivity(intent);// 执行Intent操作
				return false;
			}

		});
	}

	private void initData(int newyear) {
		int pay_sum = 0;
		int income_sum = 0;

		payDAO = new PayDAO(getActivity());
		ptypeDAO = new PtypeDAO(getActivity());
		incomeDAO = new IncomeDAO(getActivity());
		itypeDAO = new ItypeDAO(getActivity());

		switch (newyear) {
		case 1:
			defaultYear++;
			break;
		case -1:
			defaultYear--;
			break;
		}

		Intent intentr = getActivity().getIntent();
		userid = intentr.getIntExtra("cwp.id", 100000001);

		sum_title.setText(Integer.toString(defaultYear) + "年结余");

		list_income = incomeDAO.getScrollData(userid, 0, // 取每年的收入数据
				(int) incomeDAO.getCount(userid), Integer.toString(defaultYear)
						+ "-01-01", Integer.toString(defaultYear) + "-12-31");

		list_pay = payDAO.getScrollData(userid, 0, // 取每年的支出数据
				(int) payDAO.getCount(userid), Integer.toString(defaultYear)
						+ "-01-01", Integer.toString(defaultYear) + "-12-31");
		Integer[] str = new Integer[list_income.size() + list_pay.size()];

		groups = new ArrayList<Map<String, String>>();
		childs = new ArrayList<List<Map<String, String>>>();
		if (list_income.size() == 0) { // 处理listview
			sum_income.setText("￥ 0.0");
		}
		if (list_pay.size() == 0) {
			sum_pay.setText("￥ 0.0");
		}
		if ((list_income.size() == 0) && (list_pay.size() == 0)) {
			sum_total.setText("￥ 0.0");
			Map<String, String> group1 = new HashMap<String, String>();
			group1.put("isnodata", "true");
			groups.add(group1);
		} else {

			int n = 0;
			for (Tb_pay tb_pay : list_pay) {// 遍历List泛型集合
				// 将收入相关信息组合成一个字符串，存储到字符串数组的相应位置
				pay_sum += tb_pay.getMoney();
				str[n] = Integer.parseInt(tb_pay.getTime().substring(5, 7));
				n++;
			}
			for (Tb_income tb_income : list_income) {// 遍历List泛型集合
				// 将收入相关信息组合成一个字符串，存储到字符串数组的相应位置
				income_sum += tb_income.getMoney();
				str[n] = Integer.parseInt(tb_income.getTime().substring(5, 7));
				n++;
			}
			sum_pay.setText("￥ " + String.valueOf(pay_sum));
			sum_income.setText("￥ " + String.valueOf(income_sum));
			final int ip_sum = income_sum - pay_sum;
			sum_total.withNumber(ip_sum);
			sum_total.start();
			list2 = new ArrayList<Integer>();
			for (int i = 0; i < str.length; i++) {
				if (!list2.contains(str[i])) {// 如果数组 list 不包含当前项，则增加该项到数组中
					list2.add(str[i]);
				}
			}
			newStr = list2.toArray(new Integer[1]);
			Arrays.sort(newStr);

			for (int i = newStr.length; i > 0; i--) { // 循环获取数据
				int sum = 0;

				if (newStr[i - 1] < 10) {
					dmonth = "0" + Integer.toString(newStr[i - 1]);
				} else {
					dmonth = Integer.toString(newStr[i - 1]);
				}
				date1 = Integer.toString(defaultYear) + "-" + dmonth + "-01";
				date2 = Integer.toString(defaultYear) + "-" + dmonth + "-31";

				List<Tb_income> listinfos2 = incomeDAO.getScrollDataTotal(
						userid, 0, // 取每个月的数据
						(int) incomeDAO.getCount(userid), date1, date2);
				if (listinfos2.size() != 0) {
					child1 = new ArrayList<Map<String, String>>();
					for (Tb_income tb_income : listinfos2) {// 遍历List泛型集合
						// 将收入相关信息组合成一个字符串，存储到字符串数组的相应位置
						if (tb_income.getKind().equals("收入")) { // 收入
							// 准备第一个一级列表中的二级列表数据:两个二级列表,分别显示"childData1"和"childData2"
							Map<String, String> Data1 = new HashMap<String, String>();
							Data1.put("img", String.valueOf(itypeDAO.getOneImg(
									userid, tb_income.getType())));
							Data1.put("no", tb_income.getNo() + "");
							Data1.put("kind", "[" + tb_income.getKind() + "]");
							Data1.put("money", "￥ " + tb_income.getMoney2()
									+ "元");
							Data1.put(
									"title",
									itypeDAO.getOneName(userid,
											tb_income.getType()));
							Data1.put("info", tb_income.getTime());
							Data1.put("date", gofordate(tb_income.getTime()));
							child1.add(Data1);
							sum += tb_income.getMoney();
						} else { // 支出
							Map<String, String> Data1 = new HashMap<String, String>();
							Data1.put("img", String.valueOf(ptypeDAO.getOneImg(
									userid, tb_income.getType())));
							Data1.put("no", tb_income.getNo() + "");
							Data1.put("kind", "[" + tb_income.getKind() + "]");
							Data1.put("money", "￥ " + tb_income.getMoney2()
									+ "元");
							Data1.put(
									"title",
									ptypeDAO.getOneName(userid,
											tb_income.getType()));
							Data1.put("info", tb_income.getTime());
							Data1.put("date", gofordate(tb_income.getTime()));
							child1.add(Data1);
							sum -= tb_income.getMoney();
						}
					}
					// 准备一级列表中显示的数据:2个一级列表,分别显示"group1"和"group2"
					Map<String, String> group1 = new HashMap<String, String>();
					group1.put("day", (newStr[i - 1]) + "月");
					group1.put(
							"time",
							incomeDAO.gettime(defaultYear, (newStr[i - 1]), 0,
									true, false).substring(5, 10)
									+ " ~ "
									+ incomeDAO.gettime(defaultYear,
											(newStr[i - 1]), 0, false, true)
											.substring(5, 10));
					group1.put("money", String.valueOf(sum));
					groups.add(group1);

					// 用一个list对象保存所有的二级列表数据
					childs.add(child1);
				}
			}
		}
		ExpandableAdapter viewAdapter = new ExpandableAdapter(getActivity(),
				groups, childs);
		elv.setGroupIndicator(null);
		elv.setAdapter(viewAdapter);
	}

	static String gofordate(String s) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(s);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		System.out.println("date" + date + " weekDays " + weekDays[w]);
		return weekDays[w];
	}

	private void findViews() {
		elv = (ExpandableListView) getActivity().findViewById(
				R.id.expandableListView);

		sum_title = (TextView) getActivity().findViewById(R.id.sum_title);
		sum_total = (RiseNumberTextView) getActivity().findViewById(
				R.id.sum_total);
		// 设置动画播放时间
		sum_total.setDuration(1000);
		// 开始播放动画

		sum_pay = (TextView) getActivity().findViewById(R.id.sum_pay);
		sum_income = (TextView) getActivity().findViewById(R.id.sum_income);

		mPullToRefreshView = (PullToRefreshView) getActivity().findViewById(
				R.id.main_pull_refresh_view);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);

		pushup = AnimationUtils.loadAnimation(getActivity(), R.anim.push_up_in);
		pushout = AnimationUtils.loadAnimation(getActivity(),
				R.anim.push_up_out);

		searchButton = (RelativeLayout) getActivity().findViewById(
				R.id.search_button); // 搜索控件
		frag3 = (LinearLayout) getActivity().findViewById(R.id.frag3); // 搜索控件
		fragall = (LinearLayout) getActivity().findViewById(R.id.fragall); // 搜索控件
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				height = frag3.getHeight();
				TranslateAnimation animation = new TranslateAnimation(0, 0, 0,
						-height);
				animation.setDuration(500);
				animation.setFillAfter(true);
				animation.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						Intent intent = new Intent();
						intent.setClass(getActivity(), SearchActivity.class);
						startActivityForResult(intent, 100);
						getActivity().overridePendingTransition(
								R.anim.animation_2, R.anim.animation_1);
					}

				});
				fragall.startAnimation(animation);
			}
		});
	}

	// 自定义的ExpandListAdapter
	class ExpandableAdapter extends BaseExpandableListAdapter {
		private Context context;
		List<Map<String, String>> groups;
		List<List<Map<String, String>>> childs;

		/*
		 * 构造函数: 参数1:context对象 参数2:一级列表数据源 参数3:二级列表数据源
		 */
		public ExpandableAdapter(Context context,
				List<Map<String, String>> groups,
				List<List<Map<String, String>>> childs) {
			this.groups = groups;
			this.childs = childs;
			this.context = context;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return childs.get(groupPosition).get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		// 获取二级列表的View对象
		@SuppressWarnings("unchecked")
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// 获取二级列表对应的布局文件, 并将其各元素设置相应的属性
			LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(
					R.layout.list, null);

			RelativeLayout search_img2 = (RelativeLayout) linearLayout
					.findViewById(R.id.search_img2);
			RelativeLayout titlebar = (RelativeLayout) linearLayout
					.findViewById(R.id.titlebar);
			ImageView search_img = (ImageView) linearLayout
					.findViewById(R.id.search_img);
			TextView search_date = (TextView) linearLayout
					.findViewById(R.id.search_date);
			TextView search_date2 = (TextView) linearLayout
					.findViewById(R.id.search_date2);
			TextView no = (TextView) linearLayout.findViewById(R.id.no);
			TextView kind = (TextView) linearLayout.findViewById(R.id.kind);
			TextView title = (TextView) linearLayout.findViewById(R.id.title);
			TextView money = (TextView) linearLayout.findViewById(R.id.money);
			titlebar.setVisibility(View.GONE);
			search_img2.setVisibility(View.VISIBLE);

			title.setText(((Map<String, String>) getChild(groupPosition,
					childPosition)).get("title"));
			no.setText(((Map<String, String>) getChild(groupPosition,
					childPosition)).get("no"));
			kind.setText(((Map<String, String>) getChild(groupPosition,
					childPosition)).get("kind"));
			money.setText(((Map<String, String>) getChild(groupPosition,
					childPosition)).get("money"));
			search_date2.setText(((Map<String, String>) getChild(groupPosition,
					childPosition)).get("date"));
			search_date.setText(((Map<String, String>) getChild(groupPosition,
					childPosition)).get("info").substring(8, 10));
			search_img.setImageResource(Integer
					.parseInt(((Map<String, String>) getChild(groupPosition,
							childPosition)).get("img")));
			if (((Map<String, String>) getChild(groupPosition, childPosition))
					.get("kind").equals("[收入]")) {
				money.setTextColor(Color.parseColor("#ffff0000"));
			} else {
				money.setTextColor(Color.parseColor("#5ea98d"));
			}
			return linearLayout;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			if (childs.isEmpty()) {
				return 0;
			} else {
				return childs.get(groupPosition).size();
			}
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groups.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return groups.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		// 获取一级列表View对象
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			LinearLayout lv2;
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (groups.get(groupPosition).get("isnodata") == "true") {
				lv2 = (LinearLayout) layoutInflater.inflate(R.layout.child,
						null);
			} else {
				// 获取一级列表布局文件,设置相应元素属性
				lv2 = (LinearLayout) layoutInflater.inflate(R.layout.group,
						null);
				TextView day = (TextView) lv2.findViewById(R.id.day);
				TextView time = (TextView) lv2.findViewById(R.id.time);
				TextView money = (TextView) lv2.findViewById(R.id.money);
				ImageView service_arrow = (ImageView) lv2
						.findViewById(R.id.service_arrow);
				day.setText(groups.get(groupPosition).get("day"));
				time.setText(groups.get(groupPosition).get("time"));
				money.setText("￥" + groups.get(groupPosition).get("money"));

				if (Integer.parseInt(groups.get(groupPosition).get("money")) > 0) {
					money.setTextColor(Color.parseColor("#ffff0000"));
				} else if (Integer.parseInt(groups.get(groupPosition).get(
						"money")) < 0) {
					money.setTextColor(Color.parseColor("#5ea98d"));
				}
				if (isExpanded) {
					service_arrow
							.setBackgroundResource(R.drawable.service_arrow_up);
				} else {
					service_arrow
							.setBackgroundResource(R.drawable.service_arrow_down);
				}
			}
			return lv2;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true; // 必须改过来
		}

	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				mHandler.sendEmptyMessage(2);
				mPullToRefreshView.onFooterRefreshComplete();
			}
		}, 1000);
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				mHandler.sendEmptyMessage(1);
				mPullToRefreshView.onHeaderRefreshComplete();
			}
		}, 1000);

	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// groups = new ArrayList<Map<String, String>>();
			// childs = new ArrayList<List<Map<String, String>>>();
			switch (msg.what) {
			case 1:
				initData(1);
				sum_title.startAnimation(pushup);
				break;
			case 2:
				initData(-1);
				sum_title.startAnimation(pushout);
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		TranslateAnimation animation = new TranslateAnimation(0, 0, -height, 0);
		animation.setDuration(500);
		animation.setFillAfter(true);
		fragall.startAnimation(animation);
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static int getyear() {
		return defaultYear;
	}

}
