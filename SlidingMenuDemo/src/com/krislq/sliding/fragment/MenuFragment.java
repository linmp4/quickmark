package com.krislq.sliding.fragment;

import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

import com.krislq.sliding.MainActivity;
import com.krislq.sliding.R;

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
//		findPreference("a").setOnPreferenceClickListener(this);
//		findPreference("b").setOnPreferenceClickListener(this);
//		findPreference("n").setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		String key = preference.getKey();
		if ("a".equals(key)) {
			// if the content view is that we need to show . show directly
			if (index == 0) {
				((MainActivity) getActivity()).getSlidingMenu().toggle();
				return true;
			}
			// otherwise , replace the content view via a new Content fragment
			index = 0;
			FragmentManager fragmentManager = ((MainActivity) getActivity())
					.getFragmentManager();
			ContentFragment contentFragment = (ContentFragment) fragmentManager
					.findFragmentByTag("A");
			fragmentManager
					.beginTransaction()
					.replace(
							R.id.content,
							contentFragment == null ? new ContentFragment(
									"This is A Menu") : contentFragment, "A")
					.commit();
		} else if ("b".equals(key)) {
			if (index == 1) {
				((MainActivity) getActivity()).getSlidingMenu().toggle();
				return true;
			}
			index = 1;
			FragmentManager fragmentManager = ((MainActivity) getActivity())
					.getFragmentManager();
			ContentFragment contentFragment = (ContentFragment) fragmentManager
					.findFragmentByTag("B");
			fragmentManager
					.beginTransaction()
					.replace(
							R.id.content,
							contentFragment == null ? new ContentFragment(
									"This is B Menu") : contentFragment, "B")
					.commit();
		} else if ("n".equals(key)) {

			if (index == 2) {
				((MainActivity) getActivity()).getSlidingMenu().toggle();
				return true;
			}
			index = 2;
			FragmentManager fragmentManager = ((MainActivity) getActivity())
					.getFragmentManager();
			ContentFragment contentFragment = (ContentFragment) fragmentManager
					.findFragmentByTag("N");
			fragmentManager
					.beginTransaction()
					.replace(
							R.id.content,
							contentFragment == null ? new ContentFragment(
									"This is N Menu") : contentFragment, "C")
					.commit();
		}
		// anyway , show the sliding menu
		((MainActivity) getActivity()).getSlidingMenu().toggle();
		return false;
	}
}
