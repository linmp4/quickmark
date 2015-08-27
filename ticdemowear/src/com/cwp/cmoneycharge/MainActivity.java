package com.cwp.cmoneycharge;

import com.db.circularmeterdemo.SecondActivity;
import com.example.sportsdialogdemo.AnimatedView;
import com.example.sportsdialogdemo.AnimatorPlayer;
import com.example.sportsdialogdemo.HesitateInterpolator;
import com.example.sportsdialogdemo.ProgressLayout;
import com.mobvoi.android.common.ConnectionResult;
import com.mobvoi.android.common.api.MobvoiApiClient;
import com.mobvoi.android.common.api.PendingResult;
import com.mobvoi.android.common.api.ResultCallback;
import com.mobvoi.android.common.api.MobvoiApiClient.ConnectionCallbacks;
import com.mobvoi.android.common.api.MobvoiApiClient.OnConnectionFailedListener;
import com.mobvoi.android.common.api.Status;
import com.mobvoi.android.speech.SpeechRecognitionApi;
import com.mobvoi.android.wearable.DataApi;
import com.mobvoi.android.wearable.DataEventBuffer;
import com.mobvoi.android.wearable.MessageApi;
import com.mobvoi.android.wearable.MessageEvent;
import com.mobvoi.android.wearable.Node;
import com.mobvoi.android.wearable.NodeApi;
import com.mobvoi.android.wearable.Wearable;
import com.mobvoi.android.wearable.MessageApi.SendMessageResult;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.view.DelayedConfirmationView;
import android.support.wearable.view.DelayedConfirmationView.DelayedConfirmationListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SpeechRecognitionApi.SpeechRecogActivity
		implements ConnectionCallbacks, OnConnectionFailedListener,
		DataApi.DataListener, MessageApi.MessageListener, NodeApi.NodeListener,
		OnTouchListener {

	private String REULT_OK_PATH = "/start/resultok";
	private String REULT_OK_CONFIRM_PATH = "/start/resultokconfirm";
	private static final String START_ACTIVITY_PATH = "/start/MainActivity";
	private static final String JUDGE_PATH = "/start/judge";
	private static final String REULT_PATH = "/start/result";
	private static final String REULT_WRONG_PATH = "/start/resultwrong";
	private MobvoiApiClient mMobvoiApiClient;
	ProgressBar pb;
	private RelativeLayout rl2;
	private ImageView rl2_iv;
	private TextView rl2_tv;
	TextView txtRslt;
	protected boolean connectflag;

	private int size;
	private AnimatedView[] spots;
	private AnimatorPlayer animator;
	private ProgressLayout progress;
	private TextView rl3_tv2;
	private TextView rl3_tv;
	private RelativeLayout rl3;
	private DelayedConfirmationView delayedConfirmationView;
	private TextView rl3_tv_yes;
	private TextView rl3_tv_no;
	private DelayedConfirmationView delayedConfirmationView2;
	protected boolean click;
	private GestureDetector gesture;
	private FrameLayout mlayout;
	private String successtx;
	private static final int DELAY = 150;
	private static final int DURATION = 1500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		initProgress();

		mlayout = (FrameLayout) findViewById(R.id.layout);
		rl2 = (RelativeLayout) findViewById(R.id.rl2);
		rl2_iv = (ImageView) findViewById(R.id.rl2_iv);
		rl2_tv = (TextView) findViewById(R.id.rl2_tv);
		rl3 = (RelativeLayout) findViewById(R.id.rl3);
		rl3_tv2 = (TextView) findViewById(R.id.rl3_tv2);
		rl3_tv = (TextView) findViewById(R.id.rl3_tv);
		rl3_tv_yes = (TextView) findViewById(R.id.rl3_tv_yes);
		rl3_tv_no = (TextView) findViewById(R.id.rl3_tv_no);

		delayedConfirmationView = (DelayedConfirmationView) findViewById(R.id.delayed_confirmation);
		delayedConfirmationView.setTotalTimeMs(5000);
		delayedConfirmationView2 = (DelayedConfirmationView) findViewById(R.id.delayed_confirmation2);
		delayedConfirmationView2.setTotalTimeMs(5000);

		rl2_iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rl2.setVisibility(View.GONE);
			}
		});
		findViewById(R.id.test_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						startVoiceInput();
					}
				});

		gesture = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() {

					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						// if ((e2.getRawX() - e1.getRawX()) > 200) {
						// Toast.makeText(getApplicationContext(), "向右划", 0)
						// .show();
						// }
						if ((e1.getRawX() - e2.getRawX()) > 100) {
							// Toast.makeText(getApplicationContext(), "向左划", 0)
							// .show();
							next();
						}
						return super.onFling(e1, e2, velocityX, velocityY);
					}

				});
		mlayout.setOnTouchListener(this);
		mlayout.setLongClickable(true);

		mMobvoiApiClient = new MobvoiApiClient.Builder(this)
				.addApi(Wearable.API).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).build();
	}

	public void next() {
		Wearable.DataApi.removeListener(mMobvoiApiClient, this);
		Wearable.MessageApi.removeListener(mMobvoiApiClient, this);
		Wearable.NodeApi.removeListener(mMobvoiApiClient, this);
		mMobvoiApiClient.disconnect();
		Intent intent = new Intent(MainActivity.this, SecondActivity.class);// 创建Intent对象
		startActivity(intent);// 执行Intent操作
	}

	public void sendtext(String arg0, String parh) {
		String nodeId = null;
		pbshow();
		Wearable.MessageApi.sendMessage(mMobvoiApiClient, nodeId, parh,
				arg0.getBytes()).setResultCallback(
				new ResultCallback<SendMessageResult>() {
					@Override
					public void onResult(SendMessageResult sendMessageResult) {
						if (!sendMessageResult.getStatus().isSuccess()) {
							Toast.makeText(MainActivity.this, "请检查链接", 0)
									.show();
						}
					}
				});
		new Thread() {
			public void run() {
				connectflag = true;
				try {
					sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (connectflag) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							txtRslt.setText("请检查手机端是否运行中");
						}
					});
				}
			};
		}.start();
	}

	@Override
	public void onConnected(Bundle arg0) {
		System.out.println("onConnected");
		Wearable.DataApi.addListener(mMobvoiApiClient, this);
		Wearable.MessageApi.addListener(mMobvoiApiClient, this);
		Wearable.NodeApi.addListener(mMobvoiApiClient, this);
	}

	@Override
	protected void onDestroy() {
		System.out.println("s1 onDestroy");
		Wearable.DataApi.removeListener(mMobvoiApiClient, this);
		Wearable.MessageApi.removeListener(mMobvoiApiClient, this);
		Wearable.NodeApi.removeListener(mMobvoiApiClient, this);
		mMobvoiApiClient.disconnect();
		super.onDestroy();
	}

	// @Override
	// protected void onPause() {
	// super.onPause();
	// System.out.println("onResume");
	// Wearable.DataApi.removeListener(mMobvoiApiClient, this);
	// Wearable.MessageApi.removeListener(mMobvoiApiClient, this);
	// Wearable.NodeApi.removeListener(mMobvoiApiClient, this);
	// mMobvoiApiClient.disconnect();
	// }

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("onResume");
		mMobvoiApiClient.connect();
	}

	@Override
	public void onRecognitionFailed() {

	}

	@Override
	public void onRecognitionSuccess(String arg0) {
		txtRslt = (TextView) findViewById(R.id.speak_tip);
		successtx = arg0;
		txtRslt.setText("“" + arg0 + "”");
		sendtext(arg0, START_ACTIVITY_PATH);
	}

	@Override
	public void onPeerConnected(Node arg0) {

	}

	@Override
	public void onPeerDisconnected(Node arg0) {

	}

	@Override
	public void onMessageReceived(MessageEvent messageEvent) {
		connectflag = false;
		if (messageEvent.getPath().equals(START_ACTIVITY_PATH)) {
			final String text = new String(messageEvent.getData());

			final String type = text.substring(0, text.indexOf(" ")).trim();
			final String text2 = text.substring(text.indexOf(" ")).trim();
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					dialog(text2, type);
				}
			});
		}
		if (messageEvent.getPath().equals(REULT_OK_PATH)) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					dialog(successtx, "OK");
				}
			});
		} else if (messageEvent.getPath().equals(REULT_PATH)) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// pb.setVisibility(View.GONE);
					progress.setVisibility(View.GONE);
					rl2_iv.setImageResource(R.drawable.yes_normal);
					rl2_tv.setText("保存成功");
					rl2.setVisibility(View.VISIBLE);
				}
			});
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					rl2.setVisibility(View.GONE);
				}
			});
		} else if (messageEvent.getPath().equals(REULT_WRONG_PATH)) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					progress.setVisibility(View.GONE);
					rl2_iv.setImageResource(R.drawable.no_normal);
					rl2_tv.setText("保存失败");
					rl2.setVisibility(View.VISIBLE);
				}
			});
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					rl2.setVisibility(View.GONE);
				}
			});
		}
		// Intent startIntent = new Intent(this, SecondActivity.class);
		// startIntent.putExtra("cwp.watch", text);// 设置传递数据
		// startActivity(startIntent);
	}

	private void dialog(final String text, String type) {
		if (type.equals("OK") || type.equals("notype")) {
			progress.setVisibility(View.GONE);
			rl3.setVisibility(View.VISIBLE);
			delayedConfirmationView.start();
			rl3_tv.setText("识别成功");
			rl3_tv2.setText("你说了”" + text + "”，是否记录？");
			rl3_tv_yes.setText("是");
			rl3_tv_no.setText("否");
			mdialog(type);
		} else if (type.equals("judge")) {
			progress.setVisibility(View.GONE);
			rl3.setVisibility(View.VISIBLE);
			delayedConfirmationView2.start();
			rl3_tv.setText("请选择");
			rl3_tv2.setText(text);
			rl3_tv_yes.setText("收入");
			rl3_tv_no.setText("支出");
			mdialog(type);
		} else if (type.equals("wrong")) {
			// pb.setVisibility(View.GONE);
			progress.setVisibility(View.GONE);
			rl3.setVisibility(View.VISIBLE);
			delayedConfirmationView.start();
			rl3_tv.setText("识别失败");
			rl3_tv2.setText(text);
			rl3_tv_yes.setText("重录");
			rl3_tv_no.setText("取消");
			mdialog(type);
		}
	}

	private void mdialog(final String type) {
		click = true;
		delayedConfirmationView.setListener(new DelayedConfirmationListener() {

			@Override
			public void onTimerSelected(View view) {
				timeselect(view);
			}

			@Override
			public void onTimerFinished(View view) {
				timeselect(view);
			}

			private void timeselect(View view) {
				delayedConfirmationView.reset();
				if (click) {
					if (type.equals("wrong")) {
						startVoiceInput();
						rl3.setVisibility(View.GONE);
					} else if (type.equals("judge")) {
						sendtext("income", JUDGE_PATH);
						rl3.setVisibility(View.GONE);
					} else if (type.equals("OK")) {
						sendtext("income", REULT_OK_CONFIRM_PATH);
						rl3.setVisibility(View.GONE);
					}
					click = false;
				}
			}
		});
		delayedConfirmationView2.setListener(new DelayedConfirmationListener() {

			@Override
			public void onTimerSelected(View view) {
				timeselect(view);
			}

			@Override
			public void onTimerFinished(View view) {
				timeselect(view);
			}

			private void timeselect(View view) {
				delayedConfirmationView2.reset();
				if (click) {
					if (type.equals("wrong")) {
						rl3.setVisibility(View.GONE);
					} else if (type.equals("judge")) {
						sendtext("pay", JUDGE_PATH);
						rl3.setVisibility(View.GONE);
					} else if (type.equals("OK")) {
						rl3.setVisibility(View.GONE);
					}
					click = false;
				}
			}
		});
	}

	private void initProgress() {
		progress = (ProgressLayout) findViewById(R.id.endloadingbar);
		size = progress.getSpotsCount();

		spots = new AnimatedView[size];
		int size = this.getResources().getDimensionPixelSize(R.dimen.spot_size);
		int progressWidth = this.getResources().getDimensionPixelSize(
				R.dimen.progress_width);
		for (int i = 0; i < spots.length; i++) {
			AnimatedView v = new AnimatedView(this);
			v.setBackgroundResource(R.drawable.spot);
			v.setTarget(progressWidth);
			v.setXFactor(-1f);
			progress.addView(v, size, size);
			spots[i] = v;
		}
	}

	private Animator[] createAnimations() {
		Animator[] animators = new Animator[size];
		for (int i = 0; i < spots.length; i++) {
			Animator move = ObjectAnimator.ofFloat(spots[i], "xFactor", 0, 1);
			move.setDuration(DURATION);
			move.setInterpolator(new HesitateInterpolator());
			move.setStartDelay(DELAY * i);
			animators[i] = move;
		}
		return animators;
	}

	void pbshow() {
		progress.setVisibility(View.VISIBLE);
		animator = new AnimatorPlayer(createAnimations());
		animator.play();
	}

	@Override
	public void onDataChanged(DataEventBuffer arg0) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		System.out.println("onConnectionFailed");
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		System.out.println("onConnectionSuspended");
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		gesture.onTouchEvent(event);
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gesture.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

}
