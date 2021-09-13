package com.cwp.cmoneycharge;

import com.cwp.cmoneycharge.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

public class FragmentPage2 extends Fragment implements OnClickListener {
	static int userid;
	private static FragmentPage1 fragmentPage1;
	private FragmentPage2 fragmentPage2;
	private static FragmentPage3 fragmentPage3;
	private static FragmentPage4 fragmentPage4;
	// 定义布局对象
	private static FrameLayout friendfeedFl;
	private static FrameLayout myfeedFl;
	private static FrameLayout homeFl;
	private static FrameLayout moreFl;

	// 定义图片组件对象
	private static ImageView friendfeedIv;
	private static ImageView myfeedIv;
	private static ImageView homeIv;
	private static ImageView moreIv;

	// 定义按钮图片组件
	private ImageView toggleImageView, plusImageView;

	// 定义PopupWindow
	private PopupWindow popWindow;
	private LinearLayout popWinLayout;

	// 定义pop组件
	private LinearLayout pop_voiceView;
	private LinearLayout pop_quickView;

	int value = 0;
	private LinearLayout pop_photoView;

	static FragmentActivity act;

	public FragmentPage2(FragmentActivity activity) {
		act = activity;
	}

	public FragmentPage2() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_2, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Intent intentr = getActivity().getIntent();
		userid = intentr.getIntExtra("cwp.id", 100000001);

		initView();
		initData();

		switch (value) {
		case 0:
			clickFriendfeedBtn();
			break;
		case 1:
			clickFriendfeedBtn();
			break;
		case 2:
			clickMyfeedBtn();
			break;
		case 3:
			clickHomeBtn();
			break;
		case 4:
			clickMoreBtn();
			break;
		}
	}

	/**
	 * 初始化组件
	 */
	public void initView() {
		value = MainActivity.getValueFM();
		// 实例化布局对象
		friendfeedFl = (FrameLayout) getActivity().findViewById(
				R.id.layout_friendfeed);
		myfeedFl = (FrameLayout) getActivity().findViewById(R.id.layout_myfeed);
		homeFl = (FrameLayout) getActivity().findViewById(R.id.layout_home);
		moreFl = (FrameLayout) getActivity().findViewById(R.id.layout_more);

		// 实例化图片组件对象
		friendfeedIv = (ImageView) getActivity().findViewById(
				R.id.image_friendfeed);
		myfeedIv = (ImageView) getActivity().findViewById(R.id.image_myfeed);
		homeIv = (ImageView) getActivity().findViewById(R.id.image_home);
		moreIv = (ImageView) getActivity().findViewById(R.id.image_more);

		// 实例化按钮图片组件
		toggleImageView = (ImageView) getActivity().findViewById(
				R.id.toggle_btn);
		plusImageView = (ImageView) getActivity().findViewById(R.id.plus_btn);
	}

	private void initData() {
		// 给布局对象设置监听
		friendfeedFl.setOnClickListener(this);
		myfeedFl.setOnClickListener(this);
		homeFl.setOnClickListener(this);
		moreFl.setOnClickListener(this);

		// 给按钮图片设置监听
		toggleImageView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// System.out.println("我按了" + v.getId());
		switch (v.getId()) {
		// 点击动态按钮
		case R.id.layout_friendfeed:
			clickFriendfeedBtn();
			break;
		// 点击与我相关按钮
		case R.id.layout_myfeed:
			clickMyfeedBtn();
			break;
		// 点击我的空间按钮
		case R.id.layout_home:
			clickHomeBtn();
			break;
		// 点击更多按钮
		case R.id.layout_more:
			clickMoreBtn();
			break;
		// 点击中间按钮
		case R.id.toggle_btn:
			clickToggleBtn();
			break;
		// 点击中间按钮
		case R.id.pop_voice:
			clickPop_voiceBtn();
			break;
		case R.id.pop_quick:
			clickPop_quickBtn();
			break;
		case R.id.pop_photo:
			clickPop_photoViewBtn();
			break;
		}
	}

	private void clickPop_photoViewBtn() {
		Intent intent = new Intent(getActivity(), AddPay.class);// 创建Intent对象
		intent.putExtra("cwp.id", userid);
		intent.putExtra("cwp.photo", "");// 设置传递数据
		startActivity(intent);
	}

	/**
	 * 显示PopupWindow弹出菜单
	 */
	private void showPopupWindow(View parent) {
		DisplayMetrics dm = parent.getResources().getDisplayMetrics();
		int w_screen = dm.widthPixels;
		int h_screen = dm.heightPixels;
		// System.out.println("你的设备w_screen：" + w_screen + " h_screen：" +
		// h_screen);
		if (popWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View view = layoutInflater.inflate(R.layout.popwindow_layout, null);
			popWinLayout = (LinearLayout) view.findViewById(R.id.popwindow);
			// 创建一个PopuWidow对象
			float radiowith = w_screen / 480.0f;
			float radioheight = h_screen / 800.0f;
			popWindow = new PopupWindow(view,
					(int) (popWinLayout.getLayoutParams().width), h_screen / 4);
		}
		// 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
		popWindow.setFocusable(true);

		pop_voiceView = (LinearLayout) popWinLayout
				.findViewById(R.id.pop_voice);
		pop_quickView = (LinearLayout) popWinLayout
				.findViewById(R.id.pop_quick);
		pop_photoView = (LinearLayout) popWinLayout
				.findViewById(R.id.pop_photo);
		pop_voiceView.setOnClickListener(this);
		pop_quickView.setOnClickListener(this);
		pop_photoView.setOnClickListener(this);

		// 设置允许在外点击消失
		popWindow.setOutsideTouchable(true);
		// 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		// 设置菜单显示的位置

		int xPos = (w_screen - popWinLayout.getLayoutParams().width) / 2;
		popWindow.showAsDropDown(parent, xPos, 12);
		// popWindow.showAsDropDown(parent, Gravity.CENTER, 0);

		// 监听菜单的关闭事件
		popWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				// 改变显示的按钮图片为正常状态
				changeButtonImage();
			}
		});

		// 监听触屏事件
		popWindow.setTouchInterceptor(new OnTouchListener() {
			public boolean onTouch(View view, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					// 改变显示的按钮图片为正常状态
					changeButtonImage();
				}

				return false;
			}
		});
	}

	/**
	 * 点击了“动态”按钮
	 */
	public static void clickFriendfeedBtn() {
		// 实例化Fragment页面
		fragmentPage1 = new FragmentPage1();
		// 得到Fragment事务管理器
		FragmentTransaction fragmentTransaction;
		fragmentTransaction = act.getSupportFragmentManager()
				.beginTransaction();

		// 替换当前的页面
		fragmentTransaction.replace(R.id.frame_content, fragmentPage1);
		// 事务管理提交
		fragmentTransaction.commit();

		friendfeedFl.setSelected(true);
		friendfeedIv.setSelected(true);

		myfeedFl.setSelected(false);
		myfeedIv.setSelected(false);

		homeFl.setSelected(false);
		homeIv.setSelected(false);

		moreFl.setSelected(false);
		moreIv.setSelected(false);
	}

	/**
	 * 点击了“与我相关”按钮
	 */
	public static void clickMyfeedBtn() {

		// Intent intentr = new Intent(getActivity(), PayData.class);
		Intent intentr = new Intent(act, PayChart.class);
		intentr.putExtra("cwp.id", userid);
		intentr.putExtra("type", 0);
		act.startActivity(intentr);
		act.overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);

	}

	/**
	 * 点击了“我的空间”按钮
	 */
	public static void clickHomeBtn() {
		// 实例化Fragment页面
		fragmentPage3 = new FragmentPage3();
		// 得到Fragment事务管理器
		FragmentTransaction fragmentTransaction = act
				.getSupportFragmentManager().beginTransaction();
		// 替换当前的页面
		fragmentTransaction.replace(R.id.frame_content, fragmentPage3);
		// 事务管理提交
		fragmentTransaction.commit();

		friendfeedFl.setSelected(false);
		friendfeedIv.setSelected(false);

		myfeedFl.setSelected(false);
		myfeedIv.setSelected(false);

		homeFl.setSelected(true);
		homeIv.setSelected(true);

		moreFl.setSelected(false);
		moreIv.setSelected(false);
	}

	/**
	 * 点击了“更多”按钮
	 */
	public static void clickMoreBtn() {
		// 实例化Fragment页面
		fragmentPage4 = new FragmentPage4();
		// 得到Fragment事务管理器
		FragmentTransaction fragmentTransaction = act
				.getSupportFragmentManager().beginTransaction();
		// 替换当前的页面
		fragmentTransaction.replace(R.id.frame_content, fragmentPage4);
		// 事务管理提交
		fragmentTransaction.commit();

		friendfeedFl.setSelected(false);
		friendfeedIv.setSelected(false);

		myfeedFl.setSelected(false);
		myfeedIv.setSelected(false);

		homeFl.setSelected(false);
		homeIv.setSelected(false);

		moreFl.setSelected(true);
		moreIv.setSelected(true);
	}

	private void clickPop_voiceBtn() {

		Intent intent = new Intent(getActivity(), AddPay.class);// 创建Intent对象
		intent.putExtra("cwp.id", userid);
		intent.putExtra("cwp.voice", "");// 设置传递数据
		startActivity(intent);
	}

	private void clickPop_quickBtn() {

		Intent intent = new Intent(getActivity(), AddPay.class);// 创建Intent对象
		intent.putExtra("cwp.id", userid);
		intent.putExtra("keyboard", "true");
		startActivity(intent);
	}

	/**
	 * 点击了中间按钮
	 */
	private void clickToggleBtn() {
		showPopupWindow(plusImageView);
		// 改变按钮显示的图片为按下时的状态
		plusImageView.setImageResource(R.drawable.toolbar_plusback);
		toggleImageView.setImageResource(R.drawable.toolbar_btn_pressed);
	}

	/**
	 * 改变显示的按钮图片为正常状态
	 */
	private void changeButtonImage() {
		plusImageView.setImageResource(R.drawable.toolbar_plus);
		toggleImageView.setImageResource(R.drawable.toolbar_btn_normal);
	}

}
