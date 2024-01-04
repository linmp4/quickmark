package com.cwp.chart;

import com.cwp.cmoneycharge.PayChart;
import com.cwp.cmoneycharge.PayData;
import com.cwp.cmoneycharge.R;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * menu fragment ，主要是用于显示menu菜单
 * 
 * @author <a href="mailto:kris@krislq.com">Kris.lee</a>
 * @since Mar 12, 2013
 * @version 1.0.0
 */
public class MenuFragment extends PreferenceFragment implements
		OnPreferenceClickListener {
	int index = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		// set the preference xml to the content view
		addPreferencesFromResource(R.xml.menu);
		// add listener
		findPreference("mpaychart").setOnPreferenceClickListener(this);
		findPreference("mincomechart").setOnPreferenceClickListener(this);
		findPreference("dpaychart").setOnPreferenceClickListener(this);
		findPreference("dincomehart").setOnPreferenceClickListener(this);
		// findPreference("comparechart").setOnPreferenceClickListener(this);
		// findPreference("mincomechart").setOnPreferenceClickListener(this);

		SystemBarTintManager mTintManager;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		mTintManager = new SystemBarTintManager(getActivity());
		mTintManager.setStatusBarTintEnabled(true);
		mTintManager.setStatusBarTintResource(R.color.statusbar_bg);

	}

	@TargetApi(19)
	protected void setTranslucentStatus(boolean on) {
		Window win = getActivity().getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		String key = preference.getKey();
		if ("mpaychart".equals(key)) {
			Intent intentl = new Intent(getActivity(), PayChart.class);
			intentl.putExtra("datatype", "pay");
			intentl.putExtra("type", 0);
			startActivity(intentl);

		} else if ("mincomechart".equals(key)) {

			Intent intentl = new Intent(getActivity(), PayChart.class);
			intentl.putExtra("datatype", "income");
			intentl.putExtra("type", 0);
			startActivity(intentl);

		} else if ("dpaychart".equals(key)) {
			Intent intentp = new Intent(getActivity(), PayChart.class);
			intentp.putExtra("datatype", "pay");
			intentp.putExtra("type", 1);
			startActivity(intentp);

		} else if ("dincomehart".equals(key)) {
			Intent intentp = new Intent(getActivity(), PayChart.class);
			intentp.putExtra("datatype", "income");
			intentp.putExtra("type", 1);
			startActivity(intentp);

		} else if ("comparechart".equals(key)) {
			// Intent intentp = new Intent(getActivity(), PIData.class);
			// intent.putExtra("cwp.id", userid);
			// intentp.putExtra("datatype", "income");
			// intentp.putExtra("type", 1);
			// startActivity(intentp);
		}
		// anyway , show the sliding menu
		// ((MainActivity) getActivity()).getSlidingMenu().toggle();
		return false;
	}

}
