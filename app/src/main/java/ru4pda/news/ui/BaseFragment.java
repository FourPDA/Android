package ru4pda.news.ui;

import android.support.v4.app.Fragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Anna Savinova
 * @since 1.0.0
 */
public class BaseFragment extends Fragment {

	protected Logger L = LoggerFactory.getLogger(getClass());

	@Override
	public void onResume() {
		super.onResume();
		L.debug("Resume");
	}

	@Override
	public void onPause() {
		super.onPause();
		L.debug("Pause");
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		L.warn("Low memory");
	}

}
