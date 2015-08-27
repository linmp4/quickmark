package com.db.circularmeterdemo;

import com.cwp.cmoneycharge.MainActivity;
import com.cwp.cmoneycharge.R;
import com.mobvoi.android.common.ConnectionResult;
import com.mobvoi.android.common.api.MobvoiApiClient;
import com.mobvoi.android.common.api.ResultCallback;
import com.mobvoi.android.common.api.MobvoiApiClient.ConnectionCallbacks;
import com.mobvoi.android.common.api.MobvoiApiClient.OnConnectionFailedListener;
import com.mobvoi.android.wearable.DataApi;
import com.mobvoi.android.wearable.DataEventBuffer;
import com.mobvoi.android.wearable.MessageApi;
import com.mobvoi.android.wearable.MessageEvent;
import com.mobvoi.android.wearable.Node;
import com.mobvoi.android.wearable.NodeApi;
import com.mobvoi.android.wearable.Wearable;
import com.mobvoi.android.wearable.MessageApi.SendMessageResult;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

public class SecondActivity extends Activity implements ConnectionCallbacks,
		OnConnectionFailedListener, DataApi.DataListener,
		MessageApi.MessageListener, NodeApi.NodeListener {

	private static final String GETTOTAL_PATH = "/start/gettotal";
	private static final String GETTOTAL_REULT_PATH = "/start/gettotalresult";
	private CircularCounter meter;
	private String[] colors;
	private Handler handler;
	private Runnable r;
	private MobvoiApiClient mMobvoiApiClient;
	private Boolean firstflag = true;
	private Boolean secondflag = true;
	private Boolean thirdflag = true;
	int currv1 = 0;
	int currv2 = 0;
	int currv3 = 0;
	private String text;
	protected boolean connectflag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);

		mMobvoiApiClient = new MobvoiApiClient.Builder(this)
				.addApi(Wearable.API).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).build();
		handler = new Handler();

		initCircular();
		startCircular();

		initData();
	}

	public void next(View view) {
		String nodeId = null;
		Toast.makeText(this, text, 0).show();
		Toast.makeText(this,
				"currv1 " + currv1 + " currv2 " + currv2 + " currv3 " + currv3,
				0).show();
		// Wearable.MessageApi.sendMessage(mMobvoiApiClient, nodeId,
		// GETTOTAL_PATH, "0".getBytes()).setResultCallback(
		// new ResultCallback<SendMessageResult>() {
		// @Override
		// public void onResult(SendMessageResult sendMessageResult) {
		// if (!sendMessageResult.getStatus().isSuccess()) {
		// Toast.makeText(SecondActivity.this, "请检查链接", 0)
		// .show();
		// }
		// }
		// });

	}

	@Override
	public void onMessageReceived(MessageEvent messageEvent) {
		connectflag = false;
		if (messageEvent.getPath().equals(GETTOTAL_REULT_PATH)) {
			text = new String(messageEvent.getData());
			final String[] sourceStrArray = text.split("\\|");
			// for (int i = 0; i < sourceStrArray.length; i++) {
			// System.out.println(sourceStrArray[i]);
			// }
			currv1 = Integer.parseInt(sourceStrArray[2]);
			currv2 = Integer.parseInt(sourceStrArray[3]);
			currv3 = Integer.parseInt(sourceStrArray[4]);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					meter.setMetricText(sourceStrArray[0] + "月结余").setmText(
							sourceStrArray[1] + "元");
					if (sourceStrArray[1].substring(0, 1).equals("-")) {
						meter.setTextColor(Color.parseColor("#5ea98d"));
					} else {
						meter.setTextColor(Color.parseColor("#ffff0000"));
					}
				}
			});
		}
	}

	private void initData() {
		new Thread() {
			public void run() {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Wearable.MessageApi.sendMessage(mMobvoiApiClient, null,
						GETTOTAL_PATH, "0".getBytes()).setResultCallback(
						new ResultCallback<SendMessageResult>() {
							@Override
							public void onResult(
									SendMessageResult sendMessageResult) {
								if (!sendMessageResult.getStatus().isSuccess()) {
									Toast.makeText(SecondActivity.this,
											"请检查链接", 0).show();
								}
							}
						});
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
							Toast.makeText(SecondActivity.this, "请检查手机端是否运行中",
									0).show();
						}
					});
				}
			};
		}.start();
	}

	private void startCircular() {

		r = new Runnable() {
			int c1 = 0;
			int c2 = 0;
			int c3 = 0;

			public void run() {
				boolean go = true;
				boolean go2 = true;
				boolean go3 = true;

				if (c1 == currv1 && go)
					go = false;
				else if (c1 == -currv1 && go)
					go = false;

				if (c2 == currv2 && go2)
					go2 = false;
				else if (c2 == -currv2 && go2)
					go2 = false;

				if (c3 == currv3 && go3)
					go3 = false;
				else if (c3 == -currv3 && go3)
					go3 = false;

				if (go) {
					if (currv1 < 0) {
						c1--;
					} else {
						c1++;
					}
				} else {
					if (go2) {
						if (currv2 < 0) {
							c2--;
						} else {
							c2++;
						}
					} else {
						if (go3)
							if (currv3 < 0) {
								c3--;
							} else {
								c3++;
							}
					}
				}
				// else
				// currV--;

				meter.setValues(c1, c2, c3);
				handler.postDelayed(this, 30);
			}
		};
	}

	private void initCircular() {
		colors = getResources().getStringArray(R.array.colors);

		meter = (CircularCounter) findViewById(R.id.meter);
		meter.setFirst(firstflag)
				.setFirstWidth(getResources().getDimension(R.dimen.first))
				.setFirstColor(Color.RED)

				.setSecond(secondflag)
				.setSecondWidth(getResources().getDimension(R.dimen.second))
				.setSecondColor(Color.YELLOW)

				.setThird(thirdflag)
				.setThirdWidth(getResources().getDimension(R.dimen.third))
				.setThirdColor(Color.GREEN)

				.setBackgroundColor(-14606047);
	}

	@Override
	protected void onResume() {
		super.onResume();
		handler.postDelayed(r, 500);
		mMobvoiApiClient.connect();
	}

	@Override
	protected void onPause() {
		super.onPause();
		handler.removeCallbacks(r);
	}

	@Override
	public void onPeerConnected(Node arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPeerDisconnected(Node arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDataChanged(DataEventBuffer arg0) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		System.out.println("s2 onConnected");
		Wearable.DataApi.addListener(mMobvoiApiClient, this);
		Wearable.MessageApi.addListener(mMobvoiApiClient, this);
		Wearable.NodeApi.addListener(mMobvoiApiClient, this);
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		System.out.println("s2 onDestroy");
		Wearable.DataApi.removeListener(mMobvoiApiClient, this);
		Wearable.MessageApi.removeListener(mMobvoiApiClient, this);
		Wearable.NodeApi.removeListener(mMobvoiApiClient, this);
		mMobvoiApiClient.disconnect();
		super.onDestroy();
	}

}
