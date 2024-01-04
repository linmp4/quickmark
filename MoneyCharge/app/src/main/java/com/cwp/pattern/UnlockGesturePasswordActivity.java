package com.cwp.pattern;

import java.util.List;

import com.cwp.chart.SystemBarTintManager;
import com.cwp.cmoneycharge.About;
import com.cwp.cmoneycharge.CrashApplication;
import com.cwp.cmoneycharge.Description;
import com.cwp.cmoneycharge.DialogShowUtil;
import com.cwp.cmoneycharge.R;
import com.cwp.cmoneycharge.SysApplication;
import com.cwp.pattern.LockPatternView.Cell;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

public class UnlockGesturePasswordActivity extends Activity {
	private LockPatternView mLockPatternView;
	private int mFailedPatternAttemptsSinceLastTimeout = 0;
	private CountDownTimer mCountdownTimer = null;
	private Handler mHandler = new Handler();
	private TextView mHeadTextView, mforgetTextView;
	private Animation mShakeAnim;
	String flag = null;

	private Toast mToast;

	private void showToast(CharSequence message) {
		if (null == mToast) {
			mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
			mToast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			mToast.setText(message);
		}

		mToast.show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gesturepassword_unlock);

		SystemBarTintManager mTintManager;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		mTintManager = new SystemBarTintManager(this);
		mTintManager.setStatusBarTintEnabled(true);
		mTintManager.setStatusBarTintResource(R.color.statusbar_bg);

		SysApplication.getInstance().addActivity(this); // 在销毁队列中添加this

		mLockPatternView = (LockPatternView) this
				.findViewById(R.id.gesturepwd_unlock_lockview);
		mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
		mLockPatternView.setTactileFeedbackEnabled(true);
		mHeadTextView = (TextView) findViewById(R.id.gesturepwd_unlock_text);
		mforgetTextView = (TextView) findViewById(R.id.gesturepwd_unlock_forget);
		mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);

		Intent intentr = getIntent();
		Bundle bundle = intentr.getExtras();// 获取传入的数据，并使用Bundle记录
		if (bundle != null && bundle.containsKey("cwp.pwclear")) { // 清除密码
			flag = "clear";
			mHeadTextView.setText("请绘制原密码");
			mforgetTextView.setVisibility(View.GONE);
		}
		if (bundle != null && bundle.containsKey("cwp.md")) { // 修改密码
			mHeadTextView.setText("请绘制原密码");
			flag = "md";
			mforgetTextView.setVisibility(View.GONE);
		}
		if (bundle != null && bundle.containsKey("cwp.pwenable")) { // 第一次开启
			flag = "enable";
			mforgetTextView.setVisibility(View.GONE);

		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!CrashApplication.getInstance().getLockPatternUtils()
				.savedPatternExists()) {
			Intent intent = new Intent(this, GuideGesturePasswordActivity.class);
			startActivityForResult(intent, 1);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1) {
			if (resultCode == 3) {
				this.setResult(3);
				this.finish();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mCountdownTimer != null)
			mCountdownTimer.cancel();
	}

	private Runnable mClearPatternRunnable = new Runnable() {
		public void run() {
			mLockPatternView.clearPattern();
		}
	};

	protected LockPatternView.OnPatternListener mChooseNewLockPatternListener = new LockPatternView.OnPatternListener() {

		public void onPatternStart() {
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
			patternInProgress();
		}

		public void onPatternCleared() {
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
		}

		public void onPatternDetected(List<LockPatternView.Cell> pattern) {
			if (pattern == null)
				return;
			if (CrashApplication.getInstance().getLockPatternUtils()
					.checkPattern(pattern)) {
				if (flag == "clear") {
					CrashApplication.getInstance().getLockPatternUtils()
							.clearLock();
					Intent intent = new Intent();
					setResult(2, intent);
					showToast("清除成功");
					finish();// 结束当前的activity的生命周期
				} else if (flag == "md") {
					Intent intent = new Intent(
							UnlockGesturePasswordActivity.this,
							GuideGesturePasswordActivity.class);
					startActivity(intent);
					showToast("开始重置密码");
					finish();// 结束当前的activity的生命周期
				} else {
					mLockPatternView
							.setDisplayMode(LockPatternView.DisplayMode.Correct);
					CrashApplication myApplaction = (CrashApplication) getApplication();
					myApplaction.isLocked = false;
					finish();
				}
			} else {
				mLockPatternView
						.setDisplayMode(LockPatternView.DisplayMode.Wrong);
				if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {
					mFailedPatternAttemptsSinceLastTimeout++;
					int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT
							- mFailedPatternAttemptsSinceLastTimeout;
					if (retry >= 0) {
						if (retry == 0)
							showToast("您已5次输错密码，60秒后再试");
						mHeadTextView.setText("密码错误，还可以再输入" + retry + "次");
						mHeadTextView.setTextColor(Color.RED);
						mHeadTextView.startAnimation(mShakeAnim);
					}

				} else {
					showToast("输入长度不够，请重试");
				}

				if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) {
					mHandler.postDelayed(attemptLockout, 2000);
				} else {
					mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
				}
			}
		}

		private void patternInProgress() {
		}

		@Override
		public void onPatternCellAdded(List<Cell> pattern) {
			// TODO Auto-generated method stub

		}
	};
	Runnable attemptLockout = new Runnable() {

		@Override
		public void run() {
			mLockPatternView.clearPattern();
			mLockPatternView.setEnabled(false);
			mCountdownTimer = new CountDownTimer(
					LockPatternUtils.FAILED_ATTEMPT_TIMEOUT_MS + 1, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
					if (secondsRemaining > 0) {
						mHeadTextView.setText(secondsRemaining + " 秒后重试");
					} else {
						mHeadTextView.setText("请绘制手势密码");
						mHeadTextView.setTextColor(Color.WHITE);
					}

				}

				@Override
				public void onFinish() {
					mLockPatternView.setEnabled(true);
					mFailedPatternAttemptsSinceLastTimeout = 0;
				}
			}.start();
		}
	};

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

	DialogShowUtil dialogShowUtil = new DialogShowUtil(this, this, null, null,
			null);

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
			if ((flag == "clear") || (flag == "md") || (flag == "enable")) {
				finish();
			} else {
				dialogShowUtil.dialogShow("shake", "quit", "", "");
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
