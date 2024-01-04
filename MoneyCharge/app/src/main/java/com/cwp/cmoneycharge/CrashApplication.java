package com.cwp.cmoneycharge;

import com.cwp.pattern.LockPatternUtils;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class CrashApplication extends Application {
	public boolean isLocked = true;
	private static CrashApplication mInstance;
	private LockPatternUtils mLockPatternUtils;
	
	LockScreenReceiver receiver ;
	IntentFilter filter ;

	public static CrashApplication getInstance() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		mLockPatternUtils = new LockPatternUtils(this);
		receiver = new LockScreenReceiver();
        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        this.registerReceiver(receiver, filter);
		
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);
	}

	public LockPatternUtils getLockPatternUtils() {
		return mLockPatternUtils;
	}
	
	class LockScreenReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			/* 在这里处理广播 */
			if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
				isLocked  = true;
			}
		}
	}
	
}
