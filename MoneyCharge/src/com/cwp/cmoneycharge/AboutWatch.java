package com.cwp.cmoneycharge;

import com.cwp.chart.SystemBarTintManager;
import com.cwp.pattern.GuideGesturePasswordActivity;
import com.cwp.pattern.UnlockGesturePasswordActivity;
import com.cwp.pattern.UpdateManager;
import com.umeng.fb.example.CustomActivity;

import cwp.moneycharge.dao.AccountDAO;
import cwp.moneycharge.dao.IncomeDAO;
import cwp.moneycharge.dao.ItypeDAO;
import cwp.moneycharge.dao.NoteDAO;
import cwp.moneycharge.dao.PayDAO;
import cwp.moneycharge.dao.PtypeDAO;
import cwp.moneycharge.model.ActivityManager;
import cwp.moneycharge.model.CustomDialog;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class AboutWatch extends Activity {
	TextView countuser;

	Intent intentr;
	int userid;
	static SharedPreferences sp;
	Editor edit;
	private final int REQUESTCODE = 1; // 返回的结果码
	private TableRow tr_1;

	public AboutWatch() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aboutwatch);

		SysApplication.getInstance().addActivity(this); // 在销毁队列中添加this

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			findViewById(R.id.about_top).setVisibility(View.VISIBLE);
		}
		SystemBarTintManager mTintManager = new SystemBarTintManager(this);
		mTintManager.setStatusBarTintEnabled(true);
		mTintManager.setStatusBarTintResource(R.color.statusbar_bg);

		tr_1 = (TableRow) findViewById(R.id.tr_1);
		countuser = (TextView) findViewById(R.id.countuser2);
		sp = this.getSharedPreferences("preferences", MODE_WORLD_READABLE);
		edit = sp.edit();
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

	@Override
	protected void onStart() {
		super.onStart();
		intentr = getIntent();
		userid = intentr.getIntExtra("cwp.id", 100000001);

		class OnClickListenermd implements OnClickListener { // 修改密码

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AboutWatch.this,
						UnlockGesturePasswordActivity.class);
				intent.putExtra("cwp.md", "md");
				startActivity(intent);
			}
		}

		class OnClickListenercicrl implements OnClickListener { // 修改密码

			@Override
			public void onClick(View v) {
				CustomDialog.Builder customBuilder = new CustomDialog.Builder(
						AboutWatch.this);
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				RelativeLayout layout = (RelativeLayout) inflater.inflate(
						R.layout.about_watch_edit, null);
				final EditText ed1 = (EditText) layout
						.findViewById(R.id.about_w_dt);
				customBuilder.setContentView(layout);
				customBuilder.setPositiveButton("开启",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								String ed_t1 = ed1.getText().toString();
								if (ed_t1.equals("")) {
									Toast.makeText(AboutWatch.this, "请输入数据", 0)
											.show();
								} else {
									if (isNumeric(ed_t1)) {
										int ed_t1_int = Integer.parseInt(ed_t1);
										if (0 < ed_t1_int
												&& ed_t1_int <= 100000000) {
											edit.putString("aw_cicrl", ed_t1);
											edit.commit();
											countuser.setText(ed_t1);
											dialog.dismiss();
										} else {
											Toast.makeText(AboutWatch.this,
													"数据过小或过大", 0).show();
										}
									} else {
										Toast.makeText(AboutWatch.this,
												"数据中包含非数字字符", 0).show();
									}
								}
							}
						}).setNegativeButton("关闭",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				Dialog dialog = customBuilder.create();// 创建对话框
				dialog.show(); // 显示对话框
			}
		}

		tr_1.setOnClickListener(new OnClickListenercicrl());
		// 设置圈数
		if (!sp.getString("aw_cicrl", "").equals("")) {
			countuser.setText(sp.getString("aw_cicrl", ""));
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static String getVersion(Context context)// 获取版本号
	{
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "beta 1.0";
		}
	}

	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			int chr = str.charAt(i);
			if (chr < 48 || chr > 57)
				return false;
		}
		return true;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
			Intent intent = new Intent(AboutWatch.this, MainActivity.class);
			intent.putExtra("cwp.id", userid);
			intent.putExtra("cwp.Fragment", "4");// 设置传递数据
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
