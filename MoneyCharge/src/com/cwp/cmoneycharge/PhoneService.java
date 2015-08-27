package com.cwp.cmoneycharge;

import com.cwp.chart.Util;
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

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;

/**
 * 服务是activity的叔 没有界面后台运行，没有用户可见失去焦点概念。
 * 
 * @author Administrator
 * 
 */
public class PhoneService extends Service implements DataApi.DataListener,
		MessageApi.MessageListener, NodeApi.NodeListener, ConnectionCallbacks,
		OnConnectionFailedListener {

	private static String VoiceDefault;
	private MobvoiApiClient mMobvoiApiClient;
	private String type = "";
	private SharedPreferences sp;
	private String REULT_OK_PATH = "/start/resultok";
	private String REULT_OK_CONFIRM_PATH = "/start/resultokconfirm";
	private static final String GETTOTAL_PATH = "/start/gettotal";
	private static final String GETTOTAL_REULT_PATH = "/start/gettotalresult";
	private static final String START_ACTIVITY_PATH = "/start/MainActivity";
	private static final String JUDGE_PATH = "/start/judge";
	private static final String REULT_PATH = "/start/result";
	private static final String REULT_WRONG_PATH = "/start/resultwrong";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		mMobvoiApiClient = new MobvoiApiClient.Builder(this)
				.addApi(Wearable.API).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).build();

		sp = this.getSharedPreferences("preferences", MODE_WORLD_READABLE);

		super.onCreate();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// System.out.println("onStartCommand");
		mMobvoiApiClient.connect();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		System.out.println("onDestroy");
		Wearable.DataApi.removeListener(mMobvoiApiClient, this);
		Wearable.MessageApi.removeListener(mMobvoiApiClient, this);
		Wearable.NodeApi.removeListener(mMobvoiApiClient, this);
		mMobvoiApiClient.disconnect();
		super.onDestroy();
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {

	}

	@Override
	public void onConnected(Bundle arg0) {
		System.out.println("onConnected");
		Wearable.DataApi.addListener(mMobvoiApiClient, this);
		Wearable.MessageApi.addListener(mMobvoiApiClient, this);
		Wearable.NodeApi.addListener(mMobvoiApiClient, this);
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

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
	public void onMessageReceived(MessageEvent messageEvent) {
		if (messageEvent.getPath().equals(START_ACTIVITY_PATH)) {
			final String text = new String(messageEvent.getData());
			// System.out.println("onMessageReceived " + text);
			String result = Util.Recognition(text, getApplication(), 100000001);
			sendtext(result, START_ACTIVITY_PATH);
			type = result.substring(0, result.indexOf(" ")).trim();
			if (type.equals("OK") || type.equals("notype")) {
				sendtext(result, REULT_OK_PATH);
			}
		} else if (messageEvent.getPath().equals(JUDGE_PATH)) {
			Util.type = new String(messageEvent.getData());
			saveresult();
		} else if (messageEvent.getPath().equals(REULT_OK_CONFIRM_PATH)) {
			saveresult();
		} else if (messageEvent.getPath().equals(GETTOTAL_PATH)) {
			// System.out.println("GETTOTAL_PATH=");
			int cicrl = Integer.parseInt(sp.getString("aw_cicrl", "3000"));
			String result = Util.gettotal(getApplication(), cicrl);
			sendtext(result, GETTOTAL_REULT_PATH);
		}
	}

	private void saveresult() {
		// System.out.println(" Util.type " + Util.type + " type " + type
		// + " Util.VoiceSave[5] " + Util.VoiceSave[5]
		// + " Util.VoiceSave[0] " + Util.VoiceSave[0]
		// + " Util.VoiceSave[1] " + Util.VoiceSave[1]
		// + " Util.VoiceSave[4] " + Util.VoiceSave[4]
		// + " Util.VoiceSave[2] " + Util.VoiceSave[2]
		// + " Util.VoiceSave[3] " + Util.VoiceSave[3]);
		int mt = -1;
		if (Util.type.equals("pay")) {
			mt = 0;
		} else {
			mt = 4;
		}
		if (Util.VoiceSave[5] != null) {
			mt = 5;
		}
		int a = Util.save(getApplication(), Util.type, 100000001,
				Util.VoiceSave[1], Integer.parseInt(Util.VoiceSave[mt]) + 1,
				"", Util.VoiceSave[2], "");
		if (a == 1 || a == 2) {
			sendtext("ok", REULT_PATH);
		} else {
			sendtext("wrong", REULT_WRONG_PATH);
		}
	}

	public void sendtext(String result, String parh) {
		String nodeId = null;
		Wearable.MessageApi.sendMessage(mMobvoiApiClient, nodeId, parh,
				result.getBytes()).setResultCallback(
				new ResultCallback<SendMessageResult>() {
					@Override
					public void onResult(SendMessageResult sendMessageResult) {
						if (!sendMessageResult.getStatus().isSuccess()) {
							System.out
									.println("Failed to send message with status code: "
											+ sendMessageResult.getStatus()
													.getStatusCode());
						}
					}
				});
	}

	@Override
	public void onDataChanged(DataEventBuffer arg0) {

	}
}
