package com.kobe.ubersplash;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.cwp.chart.SystemBarTintManager;
import com.cwp.cmoneycharge.MainActivity;
import com.cwp.cmoneycharge.R;
import com.cwp.cmoneycharge.SysApplication;

public class splash extends Activity implements OnClickListener {

	public static final String VIDEO_NAME = "welcome_video.mp4";

	private VideoView mVideoView;

	private Button buttonLeft;

	private ViewGroup contianer;

	private TextView appName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.splan);

		SysApplication.getInstance().addActivity(this); // 在销毁队列中添加this

		findView();

		initView();

		File videoFile = getFileStreamPath(VIDEO_NAME);
		if (!videoFile.exists()) {
			videoFile = copyVideoFile();
		}
		playVideo(videoFile);

		playAnim();
	}

	private void findView() {
		mVideoView = (VideoView) findViewById(R.id.videoView);
		buttonLeft = (Button) findViewById(R.id.buttonLeft);
		contianer = (ViewGroup) findViewById(R.id.container);
		appName = (TextView) findViewById(R.id.appName);
	}

	private void initView() {

		buttonLeft.setOnClickListener(this);
	}

	private void playVideo(File videoFile) {
		mVideoView.setVideoPath(videoFile.getPath());
		mVideoView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
		mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mediaPlayer) {
				mediaPlayer.setLooping(true);
				mediaPlayer.start();
			}
		});
	}

	private void playAnim() {
		ObjectAnimator anim = ObjectAnimator.ofFloat(appName, "alpha", 0, 1);
		anim.setDuration(4000);
		anim.setRepeatCount(1);
		anim.setRepeatMode(ObjectAnimator.REVERSE);
		anim.start();
		anim.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				appName.setVisibility(View.INVISIBLE);
			}
		});
	}

	@NonNull
	private File copyVideoFile() {
		File videoFile;
		try {
			FileOutputStream fos = openFileOutput(VIDEO_NAME, MODE_PRIVATE);
			InputStream in = getResources()
					.openRawResource(R.raw.welcome_video);
			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = in.read(buff)) != -1) {
				fos.write(buff, 0, len);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		videoFile = getFileStreamPath(VIDEO_NAME);
		if (!videoFile.exists())
			throw new RuntimeException(
					"video file has problem, are you sure you have welcome_video.mp4 in res/raw folder?");
		return videoFile;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mVideoView.stopPlayback();
	}

	@Override
	protected void onPause() {
		mVideoView.stopPlayback();
		super.onPause();
	}

	@Override
	protected void onResume() {
		appName.setVisibility(View.VISIBLE);
		playAnim();
		mVideoView.start();
		super.onResume();
	}

	@Override
	public void onClick(View view) {
		if (view == buttonLeft) {
			Intent intent = new Intent(splash.this, MainActivity.class);
			startActivity(intent);
			buttonLeft.setText(R.string.button_confirm_login);
		}
	}

}
