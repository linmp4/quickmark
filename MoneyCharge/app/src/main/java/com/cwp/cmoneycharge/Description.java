package com.cwp.cmoneycharge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import cwp.moneycharge.model.ActivityManager;

public class Description extends Activity{

	Intent intentr;
	int userid;
	public Description() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.description);
		ActivityManager.getInstance().addActivity(this);
	}	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();// 实现基类中的方法//  调用自定义方法显示收入信息
		Intent intentr=getIntent();
		userid=intentr.getIntExtra("cwp.id",100000001);}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
	    	Intent intent=new Intent(Description.this,About.class);
			intent.putExtra("cwp.id",userid);
			startActivity(intent);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
