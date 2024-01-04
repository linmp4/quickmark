package com.cwp.cmoneycharge;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.umeng.fb.example.CustomActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
 * 
 * @author way
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
	StringBuffer sb;
	String result;
	private static final String TAG = "CrashHandler";
	private static final int MODE_WORLD_READABLE = 0x0001;
	private Thread.UncaughtExceptionHandler mDefaultHandler;// 系统默认的UncaughtException处理类
	private static CrashHandler INSTANCE = new CrashHandler();// CrashHandler实例
	private Context mContext;// 程序的Context对象
	private Map<String, String> info = new HashMap<String, String>();// 用来存储设备信息和异常信息
	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ss");// 用于格式化日期,作为日志文件名的一部分

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {

	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		ContextWrapper a = (ContextWrapper) mContext.getApplicationContext();
		SharedPreferences sp = a.getSharedPreferences("preferences",
				MODE_WORLD_READABLE);
		if (sp.getString("sendlog", "").equals("开")
				|| sp.getString("sendlog", "").equals("")) {
			mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();// 获取系统默认的UncaughtException处理器
			Thread.setDefaultUncaughtExceptionHandler(this);// 设置该CrashHandler为程序的默认处理器
		}
	}

	/**
	 * 当UncaughtException发生时会转入该重写的方法来处理
	 */
	@SuppressWarnings("static-access")
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果自定义的没有处理则让系统默认的异常处理器来处理

			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				thread.sleep(3000);// 如果处理了，让程序继续运行3秒再退出，保证文件保存并上传到服务器
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 *            异常信息
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	public boolean handleException(Throwable ex) {
		if (ex == null || mContext == null)
			return false;

		new Thread() {
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出", 0).show();
				Toast.makeText(mContext, "请把错误日志反馈给开发者", Toast.LENGTH_LONG)
						.show();
				Looper.loop();
			}
		}.start();
		// 收集设备参数信息
		collectDeviceInfo(mContext);
		// 保存日志文件
		final File file = saveCrashInfo2File(ex);
		TimerTask task = new TimerTask() {
			public void run() {
				// sendAppCrashReport(mContext, file);
				sendreport(mContext);
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, 4000);
		return true;
	}

	protected void sendreport(Context mContext) {
		Intent intent = new Intent(mContext, CustomActivity.class);
		intent.putExtra("cwp.md", result);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
		SysApplication.getInstance().exit();
	}

	public void sendAppCrashReport(Context mContext, File file) { // 通过系统分享发送文件
		try {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 设置intent的Action属性
			intent.setAction(Intent.ACTION_VIEW);// 获取文件file的MIME类型
			String type = "text/plain";// 设置intent的data和Type属性。
			intent.setDataAndType(Uri.fromFile(file), type);// 跳转
			mContext.startActivity(intent);
			// Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出", 0).show();
		} catch (Exception e) {

		} finally {
			SysApplication.getInstance().exit();
		}

	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param context
	 */
	public void collectDeviceInfo(Context context) {
		try {
			PackageManager pm = context.getPackageManager();// 获得包管理器
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES);// 得到该应用的信息，即主Activity
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				info.put("versionName", versionName);
				info.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		Field[] fields = Build.class.getDeclaredFields();// 反射机制
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				info.put(field.getName(), field.get("").toString());
				Log.d(TAG, field.getName() + ":" + field.get(""));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private File saveCrashInfo2File(Throwable ex) {
		sb = new StringBuffer();
		for (Map.Entry<String, String> entry : info.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\r\n");
		}
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		ex.printStackTrace(pw);
		Throwable cause = ex.getCause();
		// 循环着把所有的异常信息写入writer中
		while (cause != null) {
			cause.printStackTrace(pw);
			cause = cause.getCause();
		}
		pw.close();// 记得关闭
		result = writer.toString();
		sb.append(result);
		// 保存文件
		long timetamp = System.currentTimeMillis();
		String time = format.format(new Date());
		String fileName = "crash-" + time + "-" + timetamp + ".log";
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			try {
				File dir = new File(Environment.getExternalStorageDirectory()
						+ "/crash/");
				if (!dir.exists())
					dir.mkdir();
				File file = new File(dir, fileName);
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(sb.toString().getBytes());
				fos.close();
				return file;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
