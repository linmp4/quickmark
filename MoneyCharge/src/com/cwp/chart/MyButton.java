package com.cwp.chart;

import com.cwp.cmoneycharge.R;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;

import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.FontMetrics;

import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MyButton extends View implements View.OnTouchListener {

	private String text = ">";

	private Paint backgroundPaint;
	private MyButtonClickListener listener;
	private Bitmap bitmap;

	private float width = 0;

	private float height = 0;

	public void setWidth(float width) {
		this.width = width;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public void setMyButtonClickListener(MyButtonClickListener listener) {
		this.listener = listener;
	}

	public void setBackgroundPaintColor(int color) {
		backgroundPaint.setColor(color);
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		if (width != 0) {
			width = (float) PiewController.measureWidth(widthMeasureSpec);
		}
		if (height != 0) {
			height = (float) PiewController.measureHeight(heightMeasureSpec);
		}

		setMeasuredDimension(100, 100);
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub

		super.draw(canvas);
		float Radius = getRadius();
		bitmap = createBitmap(bitmap, Radius, Radius);

		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		canvas.drawCircle(Radius, Radius, Radius, paint);

		canvas.drawCircle(Radius, Radius, Radius * 4 / 5, backgroundPaint);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setTextSize(Radius * 5 / 3);
	 
		canvas.drawText(text, (Radius+Radius * 1 / 10),(Radius+Radius * 1 / 2), paint);
		canvas.drawBitmap(bitmap, 0, 0, null);

	}

	public MyButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MyButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyButton(Context context) {
		super(context);
		init();
	}

	private void init() {
		this.setOnTouchListener(this);
		backgroundPaint = new Paint();
		backgroundPaint.setAntiAlias(true);
		backgroundPaint.setColor(Color.RED);

		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.common_btn_pressed);

	}

	private float getRadius() {
		int height = getHeight();
		int width = getWidth();
		if (height < width) {
			return height / 2;
		}
		return width / 2;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {

//			bitmap = BitmapFactory.decodeResource(getResources(),
//					R.drawable.common_shine_btn_bg);

			invalidate();

		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
//			bitmap = BitmapFactory.decodeResource(getResources(),
//					R.drawable.common_btn_pressed);

			invalidate();
			if (listener != null) {
				listener.onclick();
			}
		}
		return true;
	}

	public Bitmap createBitmap(Bitmap b, float x, float y) {
		int w = b.getWidth();
		int h = b.getHeight();

		float sx = (float) x / w;
		float sy = (float) y / h;
		Matrix matrix = new Matrix();
		matrix.postScale(sx, sy);
		Bitmap resizeBmp = Bitmap.createBitmap(b, 0, 0, w, h, matrix, true);
		return resizeBmp;
	}

}
