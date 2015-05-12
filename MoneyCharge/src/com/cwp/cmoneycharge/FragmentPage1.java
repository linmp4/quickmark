package com.cwp.cmoneycharge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cwp.chart.MyAdspter;
import com.cwp.cmoneycharge.AddPay;
import com.cwp.cmoneycharge.ModifyInP;
import com.cwp.cmoneycharge.R;

import cwp.moneycharge.dao.IncomeDAO;
import cwp.moneycharge.dao.ItypeDAO;
import cwp.moneycharge.dao.PayDAO;
import cwp.moneycharge.dao.PtypeDAO;
import cwp.moneycharge.model.ActivityManager;
import cwp.moneycharge.model.Tb_income;
import cwp.moneycharge.model.Tb_pay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class FragmentPage1 extends Fragment {
	int userid;
	Button baddpay;
	TextView income_amount;
	TextView expense_amount;
	PtypeDAO ptypeDAO;
	ItypeDAO itypeDAO;
	String nodata = "		还木有数据，赶紧买买买吧";

	public void Pay() {
		// TODO Auto-generated constructor stub
	}

	ListView lvinfo;// 创建ListView对象
	String strType = "";// 创建字符串，记录管理类型
	private MyAdspter adapter;
	private int defaultYear;
	private int defaultMonth;
	private int defaultDay;
	private String dmonth;
	private String dday;
	private String date2;
	private String date1;
	private RelativeLayout lvnodata;
	private TextView lvtime;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_1, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		lvinfo = (ListView) getView().findViewById(R.id.lvoutaccountinfo);// 获取布局文件中的ListView组件
		baddpay = (Button) getView().findViewById(R.id.add_expense_quickly_btn);// 添加按钮
		income_amount = (TextView) getView().findViewById(R.id.income_amount);
		expense_amount = (TextView) getView().findViewById(R.id.expense_amount);
		lvtime = (TextView) getView().findViewById(R.id.lvtime);
		lvnodata = (RelativeLayout) getView().findViewById(R.id.lvnodata);
		ptypeDAO = new PtypeDAO(getActivity().getApplicationContext());
		itypeDAO = new ItypeDAO(getActivity().getApplicationContext());
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();// 实现基类中的方法// 调用自定义方法显示收入信息
		Intent intentr = getActivity().getIntent();
		userid = intentr.getIntExtra("cwp.id", 100000001);
		Showamount();
		ShowInfo(R.id.btnoutinfo);
		lvinfo.setOnItemClickListener(new OnItemClickListener()// 为ListView添加项单击事件
		{
			// 覆写onItemClick方法
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
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
				startActivity(intent);// 执行Intent操作
			}
		});
		baddpay.setOnClickListener(new OnClickListener() { // 记一笔
			@Override
			public void onClick(View arg0) {
				// Intent intent = new Intent(getActivity(), AddIncome.class);
				Intent intent = new Intent(getActivity(), AddPay.class);// 创建Intent对象
				intent.putExtra("cwp.id", userid);
				intent.putExtra("keyboard", "true");
				startActivity(intent);
			}
		});
	}

	private void Showamount() {
		PayDAO paydao = new PayDAO(getActivity().getApplicationContext());// 创建PayDAO对象
		IncomeDAO incomedao = new IncomeDAO(getActivity()
				.getApplicationContext());// 创建IncomeDAO对象
		// 获取所有收入信息，并存储到List泛型集合中
		Double incomeamount = incomedao.getamountData(userid);
		Double payamount = paydao.getamountData(userid);
		income_amount.setText("¥ " + incomeamount);
		expense_amount.setText("¥ " + payamount);
	}

	private void ShowInfo(int intType) {// 用来根据传入的管理类型，显示相应的信息
		strType = "btnoutinfo";// 为strType变量赋值
		PayDAO paydao = new PayDAO(getActivity().getApplicationContext());// 创建PayDAO对象
		// 获取所有收入信息，并存储到List泛型集合中
		List<Tb_pay> listinfos = paydao.getScrollData(userid, 0,
				(int) paydao.getCount(userid));

		IncomeDAO incomedao = new IncomeDAO(getActivity()
				.getApplicationContext());// 创建IncomeDAO对象
		final Calendar c = Calendar.getInstance();// 获取当前系统日期
		defaultYear = c.get(Calendar.YEAR);// 获取年份
		defaultMonth = c.get(Calendar.MONTH) + 1;// 获取月份
		defaultDay = c.get(Calendar.DAY_OF_MONTH);// 获取天数
		lvtime.setText("今天是" + defaultYear + "年" + defaultMonth + "月"
				+ defaultDay + "日");
		if (defaultMonth < 10) {
			dmonth = "0" + Integer.toString(defaultMonth);
		} else {
			dmonth = Integer.toString(defaultMonth);
		}
		if (defaultDay < 10) {
			dday = "0" + Integer.toString(defaultDay);
		} else {
			dday = Integer.toString(defaultDay);
		}
		date2 = Integer.toString(defaultYear) + "-" + dmonth + "-" + dday;
		date1 = Integer.toString(defaultYear) + "-" + dmonth + "-" + dday;
		List<Tb_income> listinfos2 = incomedao.getScrollDataTotal(userid, 0, 0,
				date1, date2);
		// System.out.println("listinfos2" + listinfos2.size());
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (listinfos2.size() == 0) {
			lvnodata.setVisibility(View.VISIBLE);
			lvinfo.setVisibility(View.GONE);
		} else {
			lvnodata.setVisibility(View.GONE);
			lvinfo.setVisibility(View.VISIBLE);
			for (Tb_income tb_income : listinfos2) {// 遍历List泛型集合
				// 将收入相关信息组合成一个字符串，存储到字符串数组的相应位置
				if (tb_income.getKind().equals("收入")) { // 收入
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("img",
							itypeDAO.getOneImg(userid, tb_income.getType()));
					map.put("no", tb_income.getNo() + "");
					map.put("kind", "[" + tb_income.getKind() + "]");
					map.put("money",
							"￥ " + tb_income.getMoney2() + "元");
					map.put("title",
							itypeDAO.getOneName(userid, tb_income.getType()));
					map.put("info", tb_income.getTime());
					list.add(map);
				} else { // 支出
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("img",
							ptypeDAO.getOneImg(userid, tb_income.getType()));
					map.put("no", tb_income.getNo() + "");
					map.put("kind", "[" + tb_income.getKind() + "]");
					map.put("money",
							"￥ " + tb_income.getMoney2() + "元");
					map.put("title",
							ptypeDAO.getOneName(userid, tb_income.getType()));
					map.put("info", tb_income.getTime());
					list.add(map);
				}
			}
			adapter = new MyAdspter(getActivity(), list, false);
			lvinfo.setAdapter(adapter);// 为ListView列表设置数据源
		}
	}
}
