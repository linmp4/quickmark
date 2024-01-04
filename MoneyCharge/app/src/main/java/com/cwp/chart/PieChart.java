package com.cwp.chart;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class PieChart extends ViewGroup {

	public PieChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public PieChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PieChart(Context context) {
		super(context);
		init();
	}

	private void init() {

	}

	private int m_measureWidth = 0;
	private int m_measureHeight = 0;

	/**
	 * 计算控件的大尿
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		m_measureWidth = PiewController.measureWidth(widthMeasureSpec);
		Log.e("caizh", "m_measureWidth=" + m_measureWidth);
		m_measureHeight = PiewController.measureHeight(heightMeasureSpec);
		// 计算自定义的ViewGroup中所有子控件的大尿
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		// 设置自定义的控件MyViewGroup的大尿
		setMeasuredDimension(m_measureWidth, m_measureHeight);
	}

	/**
	 * 覆写onLayout，其目的是为了指定视图的显示位置，方法执行的前后顺序是在onMeasure之后，因为视图肯定是只有知道大小的情况下＿
	 * 才能确定怎么摆放
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// 记录总高庿
		Log.e("caizh", "l=" + l);
		// 遍历承?子视囿
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View childView = getChildAt(i);

			// 获取在onMeasure中计算的视图尺寸
			int measureHeight = childView.getMeasuredHeight();
			int measuredWidth = childView.getMeasuredWidth();
			if (childView.getTag().equals("pieButton")) {
				childView.layout(
						(m_measureWidth * 6 / 7 - m_measureWidth / 25),
						(int) ((m_measureWidth * 19 / 16)) - m_measureWidth
								/ 25,
						(m_measureWidth * 6 / 7 + m_measureWidth / 25),
						(int) ((m_measureWidth * 19 / 16)) + m_measureWidth
								/ 25);

			} else if (childView.getTag().equals("pieText")) {
				TextView textView = (TextView) childView;
				if (m_measureWidth == 680) {
					textView.setTextSize(m_measureWidth / 30);
				} else if (m_measureWidth == 1020) {
					textView.setTextSize(m_measureWidth / 40);
				} else {
					textView.setTextSize(m_measureWidth / 30);
				}

				childView.layout((int) ((m_measureWidth - measuredWidth) / 2),
						(int) ((m_measureWidth * 19 / 16)) - measureHeight / 2,
						(int) ((m_measureWidth + measuredWidth) / 2),
						(int) ((m_measureWidth * 19 / 16)) + measureHeight / 2);
			} else {
				// Log.e("caizh", "measuredWidth=" + measuredWidth);
				childView.layout(0, 0, measuredWidth, measureHeight);
			}

		}
	}

}
