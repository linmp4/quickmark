package com.cwp.cmoneycharge;

import java.util.Calendar;
import java.util.List;
  
import com.cwp.cmoneycharge.R; 

import cwp.moneycharge.dao.IncomeDAO; 
import cwp.moneycharge.dao.ItypeDAO; 
import cwp.moneycharge.dao.PtypeDAO;
import cwp.moneycharge.model.ActivityManager;
import cwp.moneycharge.model.Tb_income;
import cwp.moneycharge.model.Tb_pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import cwp.moneycharge.dao.IncomeDAO;
import cwp.moneycharge.dao.PayDAO;

public class ModifyInP extends Activity{
	protected static final int DATE_DIALOG_ID = 0;// 创建日期对话框常量
	TextView tvtitle, textView;// 创建两个TextView对象
	EditText txtMoney, txtTime, txtHA, txtMark;// 创建4个EditText对象
	Spinner spType;// 创建Spinner对象
	Button btnEdit, btnDel;// 创建两个Button对象
	String[] strInfos;// 定义字符串数组
	String strno, strType;// 定义两个字符串变量，分别用来记录信息编号和管理类型
	int userid;
	ItypeDAO itypeDAO=new ItypeDAO(ModifyInP.this);
	PtypeDAO ptypeDAO=new PtypeDAO(ModifyInP.this);
	List<String> spdatalist;

	private int mYear;// 年
	private int mMonth;// 月
	private int mDay;// 日

    private ArrayAdapter<String> adapter;
    private String[] spdata;
	PayDAO payDAO= new PayDAO(ModifyInP.this );// 创建PayDAO对象
	IncomeDAO incomeDAO  = new IncomeDAO(ModifyInP.this);// 创建IncomeDAO对象
	public ModifyInP() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modifyinp);// 设置布局文件 
		ActivityManager.getInstance().addActivity(this); //管理Activity,退出按钮点击时调用
		tvtitle = (TextView) findViewById(R.id.inouttitle);// 获取标题标签对象
		textView = (TextView) findViewById(R.id.tvInOut);// 获取地点/付款方标签对象
		txtMoney = (EditText) findViewById(R.id.txtInOutMoney);// 获取金额文本框
		txtTime = (EditText) findViewById(R.id.txtInOutTime);// 获取时间文本框
		spType = (Spinner) findViewById(R.id.spInOutType);// 获取类别下拉列表
		txtHA = (EditText) findViewById(R.id.txtInOut);// 获取地点/付款方文本框
		txtMark = (EditText) findViewById(R.id.txtInOutMark);// 获取备注文本框
		btnEdit = (Button) findViewById(R.id.btnInOutEdit);// 获取修改按钮
		btnDel = (Button) findViewById(R.id.btnInOutDelete);// 获取删除按钮
	}
	@Override
	protected void onStart(){
		super.onStart();
		
		Intent intent = getIntent();// 创建Intent对象
		Bundle bundle = intent.getExtras();// 获取传入的数据，并使用Bundle记录
		strInfos = bundle.getStringArray("cwp.message");// 获取Bundle中记录的信息
		strno = strInfos[0];// 记录id
		strType = strInfos[1];// 记录类型
		userid=intent.getIntExtra("cwp.id",100000001);
		if (strType.equals("btnoutinfo"))// 如果类型是btnoutinfo
		{
			//选择列表初始化
			spdatalist=ptypeDAO.getPtypeName(userid);
			spdata=spdatalist.toArray(new String[spdatalist.size()]);//在tb_itype中按用户id读取 
			adapter =new ArrayAdapter<String>(ModifyInP.this,android.R.layout.simple_spinner_item,spdata); //动态生成收入类型列表
			spType.setAdapter(adapter);
			
			tvtitle.setText("支出管理");// 设置标题为“支出管理”
			textView.setText("地  点：");// 设置“地点/付款方”标签文本为“地 点：”
			// 根据编号查找支出信息，并存储到Tb_pay对象中
			Tb_pay tb_pay = payDAO.find(userid,Integer.parseInt(strno));
			txtMoney.setText(String.valueOf(tb_pay.getMoney()));// 显示金额
			txtTime.setText(tb_pay.getTime());// 显示时间
			spType.setSelection(tb_pay.getType()-1);// 显示类别
			txtHA.setText(tb_pay.getAddress());// 显示地点
			txtMark.setText(tb_pay.getMark());// 显示备注
		} else if (strType.equals("btnininfo"))// 如果类型是btnininfo
		{
			//选择列表初始化
			spdatalist=itypeDAO.getItypeName(userid);
			spdata=spdatalist.toArray(new String[spdatalist.size()]);//在tb_itype中按用户id读取 
			adapter =new ArrayAdapter<String>(ModifyInP.this,android.R.layout.simple_spinner_item,spdata); //动态生成收入类型列表
			spType.setAdapter(adapter);
			
			
			tvtitle.setText("收入管理");// 设置标题为“收入管理”
			textView.setText("付款方：");// 设置“地点/付款方”标签文本为“付款方：”
			// 根据编号查找收入信息，并存储到Tb_pay对象中
			Tb_income tb_income = incomeDAO.find(userid,Integer.parseInt(strno));
			txtMoney.setText(String.valueOf(tb_income.getMoney()));// 显示金额
			txtTime.setText(tb_income.getTime());// 显示时间	
			spType.setSelection(tb_income.getType()-1);// 显示类别
			txtHA.setText(tb_income.getHandler());// 显示付款方
			txtMark.setText(tb_income.getMark());// 显示备注
		}

		txtTime.setOnClickListener(new OnClickListener() {// 为时间文本框设置单击监听事件
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);// 显示日期选择对话框
			}
		});

		btnEdit.setOnClickListener(new OnClickListener() {// 为修改按钮设置监听事件
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (strType.equals("btnoutinfo"))// 判断类型如果是btnoutinfo
				{
					Tb_pay tb_pay = new Tb_pay();// 创建Tb_pay对象
					tb_pay.set_id(userid);// 设置userid
					tb_pay.setNo(Integer.parseInt(strno));// 设置编号
					tb_pay.setMoney(Double.parseDouble(txtMoney.getText().toString()));// 设置金额
					tb_pay.setTime(setTimeFormat(txtTime.getText().toString()));// 设置时间
					tb_pay.setType(spType.getSelectedItemPosition()+1);// 设置类别
					tb_pay.setAddress(txtHA.getText().toString());// 设置地点
					tb_pay.setMark(txtMark.getText().toString());// 设置备注
					payDAO.update(tb_pay);// 更新支出信息
					Toast.makeText(ModifyInP.this, "〖数据〗修改成功！", Toast.LENGTH_SHORT).show();
					gotoback();
				} else if (strType.equals("btnininfo"))// 判断类型如果是btnininfo
				{
					Tb_income tb_income = new Tb_income();// 创建Tb_income对象
					tb_income.set_id(userid);// 设置编号
					tb_income.setNo(Integer.parseInt(strno));// 设置编号
					tb_income.setMoney(Double.parseDouble(txtMoney.getText().toString()));// 设置金额
					tb_income.setTime(setTimeFormat(txtTime.getText().toString()));// 设置时间
					tb_income.setType(spType.getSelectedItemPosition()+1);// 设置类别
					tb_income.setHandler(txtHA.getText().toString());// 设置付款方
					tb_income.setMark(txtMark.getText().toString());// 设置备注
					incomeDAO.update(tb_income);// 更新收入信息
					Toast.makeText(ModifyInP.this, "〖数据〗修改成功！", Toast.LENGTH_SHORT).show();
					gotoback();
				}
				// 弹出信息提示
				 
			}
		});

		btnDel.setOnClickListener(new OnClickListener() {// 为删除按钮设置监听事件
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (strType.equals("btnoutinfo"))// 判断类型如果是btnoutinfo
				{
					payDAO.detele(userid,Integer.parseInt(strno));// 根据编号删除支出信息
					gotoback();
				} else if (strType.equals("btnininfo"))// 判断类型如果是btnininfo
				{
					incomeDAO.detele(userid,Integer.parseInt(strno));// 根据编号删除收入信息
					gotoback();
				}
				Toast.makeText(ModifyInP.this, "〖数据〗删除成功！", Toast.LENGTH_SHORT)
						.show();
			}
		});

		final Calendar c = Calendar.getInstance();// 获取当前系统日期
		mYear = c.get(Calendar.YEAR);// 获取年份
		mMonth = c.get(Calendar.MONTH);// 获取月份
		mDay = c.get(Calendar.DAY_OF_MONTH);// 获取天数
		updateDisplay();// 显示当前系统时间
	}

	@Override
	protected Dialog onCreateDialog(int id)// 重写onCreateDialog方法
	{
		switch (id) {
		case DATE_DIALOG_ID:// 弹出日期选择对话框
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;// 为年份赋值
			mMonth = monthOfYear;// 为月份赋值
			mDay = dayOfMonth;// 为天赋值
			updateDisplay();// 显示设置的日期
		}
	};

	private void updateDisplay() {
		// 显示设置的时间
		txtTime.setText(new StringBuilder().append(mYear).append("-")
				.append(mMonth + 1).append("-").append(mDay));
	}

	private void gotoback(){
		 
		Intent intent=new Intent(ModifyInP.this,MainActivity.class);
		intent.putExtra("cwp.id",userid);
		startActivity(intent);
	}
	
	private String setTimeFormat(String txtTime){
		String date=txtTime;

		int y,m,d;
		String sm,sd;
		int i=0,j=0,k=0; 
		
		for (i = 0; i < date.length(); i++)   
		  {   
		   if (date.substring(i, i + 1).equals("-") && j==0)   
			    j=i;
		   else if(date.substring(i, i + 1).equals("-"))
			    k=i;
		  } 
		y=Integer.valueOf(date.substring(0,j));
		m=Integer.valueOf(date.substring(j+1,k));
		d=Integer.valueOf(date.substring(k+1));
		if(m<10){
			sm="0"+String.valueOf(m);
		}
		else
			sm=String.valueOf(m);
		if(d<10){
			sd="0"+String.valueOf(d);
		}
		else
			sd=String.valueOf(d);
 
		return String.valueOf(y)+"-"+sm+"-"+sd;
		
	}

}
