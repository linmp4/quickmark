package com.cwp.cmoneycharge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public abstract class BaseFrament extends Fragment implements OnTouchListener {

	GestureDetector gesture;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		gesture = new GestureDetector(getActivity(),
				new GestureDetector.SimpleOnGestureListener() {

					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						// 向右划
						if ((e2.getRawX() - e1.getRawX()) > 200) {
							filngtonpre();
						}
						// 向左划
						if ((e1.getRawX() - e2.getRawX()) > 200) {
							filngtonext();
						}
						return super.onFling(e1, e2, velocityX, velocityY);
					}

				});

	}

	/**
	 * 向右划动方法
	 */
	public abstract void filngtonext();

	/**
	 * 向左划动方法
	 */
	public abstract void filngtonpre();

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		view.setOnTouchListener(this);
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		gesture.onTouchEvent(event);
		return true;
	}

}
