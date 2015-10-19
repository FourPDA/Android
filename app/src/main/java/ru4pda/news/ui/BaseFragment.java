package ru4pda.news.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by asavinova on 19/10/15.
 */
public class BaseFragment extends Fragment {

	protected Logger L;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		L = LoggerFactory.getLogger(getClass());
	}

	@Override
	public void onResume() {
		super.onResume();
		L.debug("On resume");
	}

	@Override
	public void onPause() {
		super.onPause();
		L.debug("On pause");
	}

}
