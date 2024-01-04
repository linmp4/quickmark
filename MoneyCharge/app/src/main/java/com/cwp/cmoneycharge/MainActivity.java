package com.cwp.cmoneycharge;

import java.util.Calendar;

import com.cwp.chart.SystemBarTintManager;
import com.cwp.cmoneycharge.AddPay;
import com.cwp.cmoneycharge.R;
import com.cwp.pattern.UnlockGesturePasswordActivity;
import com.cwp.pattern.UpdateManager;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

public class MainActivity extends FragmentActivity implements OnClickListener {
	// 定义Fragment页面
	private FragmentPage2 fragmentPage2;
	SharedPreferences sp;
	int userid;
	static int value = 0;
	DialogShowUtil dialogShowUtil = new DialogShowUtil(this, this, null, null,
			null);

	private Effectstype effect; // 自定义Dialog
	private FragmentPage3 fragmentPage3;
	private String updatedate;
	private Editor edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		sp = this.getSharedPreferences("preferences", MODE_WORLD_READABLE);
		edit = sp.edit();
		// initdefault();// 初始化数据

		SystemBarTintManager mTintManager;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		mTintManager = new SystemBarTintManager(this);
		mTintManager.setStatusBarTintEnabled(true);
		mTintManager.setStatusBarTintResource(R.color.statusbar_bg);

		fragmentPage2 = new FragmentPage2(this);
		// 得到Fragment事务管理器
		FragmentTransaction fragmentTransaction = this
				.getSupportFragmentManager().beginTransaction();
		// 替换当前的页面
		fragmentTransaction.replace(R.id.frame_foot, fragmentPage2);
		fragmentTransaction.commit();

		SysApplication.getInstance().addActivity(this); // 在销毁队列中添加this
		Intent intentr = getIntent();
		userid = intentr.getIntExtra("cwp.id", 100000001);
		if (intentr.getStringExtra("cwp.Fragment") != null) { // 取回跳转的目的页面
			value = Integer.parseInt(intentr.getStringExtra("cwp.Fragment"));
		}
		Calendar c = Calendar.getInstance();// 获取当前系统日期
		int mYear = c.get(Calendar.YEAR);// 获取年份
		int mMonth = c.get(Calendar.MONTH);// 获取月份
		int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取天数
		updatedate = mYear + "-" + mMonth + 1 + "-" + mDay;

		// StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		// .detectDiskReads().detectDiskWrites().detectNetwork()
		// .penaltyLog().build());
		// StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		// .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
		// .penaltyLog().penaltyDeath().build());
	}

	private void initdefault() { // 初始化数据
		edit.putString("sendlog", "开"); // 报log
		edit.putString("gesturepw", "开"); // 手势开
		edit.commit();
	}

	@Override
	public void onClick(View v) {

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
			dialogShowUtil.dialogShow("shake", "quit", "", "");
		}
		return super.onKeyDown(keyCode, event);
	}

	public static int getValueFM() {
		return value;
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

	protected void onResume() {
		super.onResume();
		CrashApplication myApplaction = (CrashApplication) getApplication();
		if ((myApplaction.isLocked)
				&& (sp.getString("gesturepw", "").equals("开"))) {// 判断是否需要跳转到密码界面
			Intent intent = new Intent(this,
					UnlockGesturePasswordActivity.class);
			startActivity(intent);
		}

		if (!updatedate.equals(sp.getString("updatedate", ""))) { // 今天已经检查过就不自动检查了
			UpdateManager manager = new UpdateManager(MainActivity.this);
			manager.checkUpdate("noshow");
			edit.putString("updatedate", updatedate); // 一天只检查一次
			edit.commit();
		}
	};
}
