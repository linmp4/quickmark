package com.cwp.chart;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

public class ChartProp {

	private int mId = 0;
	private float mPercent = 1.0f;
	private View mParent = null;
	private int mColor = Color.WHITE;
	private float mSweepAngle = 0f;
	private String mName = "";
	private String mName2 = "";

	private float mStartAngle = 0f;
	private float mEndAngle = 0f;

	public ChartProp(View chartView) {
		mParent = chartView;
	}

	public void setId(int id) {
		mId = id;

	}

	public int getId() {
		return mId;
	}

	/**
	 * the percent ,such as 0.5f,0.05f, NEIGHTER 50% NOR 50
	 * 
	 * @param percent
	 */
	public void setPercent(float percent) {

		// Log.e("caizh", "percent="+percent);
		mPercent = percent;
		mSweepAngle = percent * 360;
		// invalidate();
	}

	public float getPercent() {
		return mPercent;
	}

	public float getSweepAngle() {
		return mSweepAngle;
	}

	public void setColor(int color) {
		mColor = color;
		// invalidate();
	}

	public int getColor() {
		return mColor;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
		// invalidate();
	}
	
	public String getName2() {
		return mName2;
	}

	public void setName2(String name) {
		this.mName2 = name;
		// invalidate();
	}

	private void invalidate() {
		if (mParent != null) {
			mParent.invalidate();
		}
	}

	public float getStartAngle() {
		return mStartAngle;
	}

	public void setStartAngle(float startAngle) {
		this.mStartAngle = (startAngle);
	}

	public float getEndAngle() {
		return mEndAngle;
	}

	public void setEndAngle(float endAngle) {

		this.mEndAngle = (endAngle);
	}

}
