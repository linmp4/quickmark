package com.cwp.pattern;

import com.cwp.chart.SystemBarTintManager;
import com.cwp.cmoneycharge.About;
import com.cwp.cmoneycharge.CrashApplication;
import com.cwp.cmoneycharge.R;
import com.cwp.cmoneycharge.SysApplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class GuideGesturePasswordActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gesturepassword_guide);

		SysApplication.getInstance().addActivity(this); // 在销毁队列中添加this

		SystemBarTintManager mTintManager;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		mTintManager = new SystemBarTintManager(this);
		mTintManager.setStatusBarTintEnabled(true);
		mTintManager.setStatusBarTintResource(R.color.statusbar_bg);

		findViewById(R.id.gesturepwd_guide_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						CrashApplication.getInstance().getLockPatternUtils()
								.clearLock();
						Intent intent = new Intent(
								GuideGesturePasswordActivity.this,
								CreateGesturePasswordActivity.class);
						// 打开新的Activity
						startActivityForResult(intent, 1);
					}
				});
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

//		System.out.println("asdrequestCode" + requestCode + "  " + resultCode);
		if (requestCode == 1) {
			if (resultCode == 3) {
				this.setResult(3);
				this.finish();
			}
		}
	}

}
