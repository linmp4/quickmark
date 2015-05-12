package com.cwp.pattern;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.cwp.cmoneycharge.R;
import com.zhy.view.RoundProgressBarWidthNumber;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author coolszy
 * @date 2012-4-26
 * @blog http://blog.92coding.com
 */

public class UpdateManager extends Service {
	/* 下载丿 */
	private static final int DOWNLOAD = 1;
	private static final int DOWNLOAD2 = 4;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	public static final int IS_FINISH = 0;
	public static final int DOWNLOAD_FILERR = 3;
	/* 保存解析的XML信息 */
	HashMap<String, String> mHashMap;
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数釿 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private static final int down_step = 3;

	private Context mContext;
	/* 更新进度板 */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;
	public InputStream inputStream;
	private String show;

	private RemoteViews contentView;
	private NotificationManager notificationManager;
	protected Notification notification;
	public int updateCount = 0;
	private TextView update_text;

	private RoundProgressBarWidthNumber mRoundProgressBar;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 正在下载
			case DOWNLOAD:
				mRoundProgressBar.setProgress(progress);
				// 设置进度条位罿
				break;
			case DOWNLOAD_FINISH:
				// 安装文件
				installApk();
				break;
			case DOWNLOAD_FILERR:
				// 无下载文件
				Toast.makeText(mContext, "下载文件出错，请重新下载", Toast.LENGTH_LONG)
						.show();
				break;
			default:
				break;
			}
		};
	};

	public Handler handler = new Handler() {
		// 在Handler中获取消息，重写handleMessage()方法
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// 成功建立连接,但无更新
				if (show.equals("show")) {
					Toast.makeText(mContext, "暂无更新", Toast.LENGTH_LONG).show();
				}
				break;
			case 2:
				// 有更新
				showNoticeDialog();
				break;
			case -1:
				// 卡住
				if (show.equals("show")) {
					Toast.makeText(mContext, "网络出了点状况", Toast.LENGTH_LONG)
							.show();
					break;
				}
			}
		}
	};

	public UpdateManager(Context context) {
		this.mContext = context;
	}

	/**
	 * 棿?软件更新
	 */
	public void checkUpdate(String show) {
		this.show = show;
		if (isWifi(mContext)) {
			isUpdate();
		} else {
			if (show.equals("show")) {
				Toast.makeText(mContext, "当前为非WIFI网络！", Toast.LENGTH_LONG)
						.show();
			}
		}
	}

	/**
	 * 棿?软件是否有更新版朿
	 */
	public void isUpdate() {
		// 获取当前软件版本
		// 把version.xml放到网络上，然后获取文件信息
		// InputStream inStream = ParseXmlService.class.getClassLoader()
		// .getResourceAsStream("version.xml");
		new Thread() {
			@Override
			public void run() {
				try {
					double versionCode = getVersionCode(mContext);
					URL url = new URL(
							"http://linmp6-wordpress.stor.sinaapp.com/version.xml");
					// 建立网络连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setConnectTimeout(1 * 1000);
					conn.setReadTimeout(1 * 1000); // 必须设置
					inputStream = conn.getInputStream();
					// 获取图片数据
					ParseXmlService service = new ParseXmlService();
					// 解析XML文件?由于XML文件比较小，因此使用DOM方式进行解析
					mHashMap = service.parseXml(inputStream);
					Message message = new Message();
					message.what = 1;
					if (null != mHashMap) {
						Double serviceCode = Double.valueOf(mHashMap
								.get("version"));
						// 版本判断
						if ((serviceCode - versionCode) > 0) {
							message.what = 2;
						}
					}
					inputStream.close();
					// 发送消息到消息队列中
					handler.sendMessage(message);
				} catch (Exception e) {
					Message message = new Message();
					message.what = -1;
					handler.sendMessage(message);
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * 获取软件版本叿
	 * 
	 * @param context
	 * @return
	 */
	public double getVersionCode(Context context) {
		double versionCode = 0;
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = Double.valueOf(
					context.getPackageManager().getPackageInfo(
							context.getPackageName(), 0).versionName)
					.doubleValue();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 显示软件更新对话桿
	 */
	private void showNoticeDialog() {
		// 构??话桿
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("软件更新");
		builder.setMessage("检测到新版本" + mHashMap.get("version") + "，立即更新吗?\n\n"
				+ mHashMap.get("text").replace("\\n", "\n"));
		// 更新
		builder.setPositiveButton("更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 显示下载对话桿
				showDownloadDialog();
			}
		});
		// 稍后更新
		builder.setNegativeButton("稍后更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * 显示软件下载对话桿
	 */
	private void showDownloadDialog() {
		// 构??件下载对话桿
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("正在更新");
		// 给下载对话框增加进度板
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mRoundProgressBar = (RoundProgressBarWidthNumber) v
				.findViewById(R.id.id_progress02);
		// mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		// update_text = (TextView) v.findViewById(R.id.update_text);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton("取消", new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 设置取消状濍
				cancelUpdate = true;

				notification.flags = Notification.FLAG_AUTO_CANCEL;
				notification.setLatestEventInfo(mContext, mHashMap.get("name"),
						"用户已取消下载", null);
				notificationManager.notify(R.layout.notification_item,
						notification);
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 现在文件
		downloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk() {
		// 启动新线程下载软仿
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 * 
	 * @author coolszy
	 * @date 2012-4-26
	 * @blog http://blog.92coding.com
	 */
	private class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory()
							+ "/";
					mSavePath = sdpath + "download";
					URL url = new URL(mHashMap.get("url"));
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入浿
					if (length > -1) {
						createNotification();
						InputStream is = conn.getInputStream();
						File file = new File(mSavePath);
						// 判断文件目录是否存在
						if (!file.exists()) {
							file.mkdir();
						}
						File apkFile = new File(mSavePath, mHashMap.get("name"));
						FileOutputStream fos = new FileOutputStream(apkFile);
						int count = 0;
						// 缓存
						byte buf[] = new byte[1024];
						// 写入到文件中
						do {
							int numread = is.read(buf);
							count += numread;
							progress = (int) (((float) count / length) * 100);
							// 更新进度
							mHandler.sendEmptyMessage(DOWNLOAD);
							if (numread <= 0) {
								// 下载完成
								mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
								break;
							}
							// 写入文件
							fos.write(buf, 0, numread);
						} while (!cancelUpdate);// 点击取消就停止下轿
						fos.close();
						is.close();
					} else {
						mHandler.sendEmptyMessage(DOWNLOAD_FILERR);
					}

				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 取消下载对话框显礿
			mDownloadDialog.dismiss();
		}
	};

	/**
	 * make true current connect service is wifi
	 * 
	 * @param mContext
	 * @return
	 */
	private static boolean isWifi(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 安装APK文件
	 */
	@SuppressWarnings("deprecation")
	private void installApk() {
		File apkfile = new File(mSavePath, mHashMap.get("name"));
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);

		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, i,
				0);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(mContext, mHashMap.get("name"), "下载成功",
				pendingIntent);
		notificationManager.notify(R.layout.notification_item, notification);
		/*** stop service *****/
		stopSelf();
	}

	/**
	 * 方法描述：createNotification方法
	 * 
	 * @param
	 * @return
	 * @see UpdateService
	 */
	@SuppressWarnings("deprecation")
	public void createNotification() {

		String app_name = null;
		// notification = new Notification(R.drawable.dot_enable,app_name +
		// getString(R.string.is_downing) ,System.currentTimeMillis());
		notification = new Notification(
		// R.drawable.video_player,//应用的图标
				R.drawable.ic_launcher,// 应用的图标
				mHashMap.get("name") + "正在下载", System.currentTimeMillis());
		notification.flags = Notification.FLAG_ONGOING_EVENT;

		contentView = new RemoteViews(mContext.getPackageName(),
				R.layout.notification_item);
		contentView.setTextViewText(R.id.notificationTitle,
				mHashMap.get("name") + "正在下载");
		contentView.setTextViewText(R.id.notificationPercent, "下载中");
		contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);
		notification.contentView = contentView;

		// updateIntent = new Intent(this, AboutActivity.class);
		// updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// //updateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);
		// notification.contentIntent = pendingIntent;

		notificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(R.layout.notification_item, notification);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
