package four.pda.ui;

import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import four.pda.R;

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

	protected void showMenuIcon() {
		final View view = getActivity().findViewById(R.id.drawer_layout);
		if (view == null) return;

		if (view instanceof DrawerLayout) {

			Toolbar toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
			if (toolbar == null) return;

			toolbar.setNavigationIcon(R.mipmap.ic_menu_white_24dp);
			toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					((DrawerLayout) view).openDrawer(GravityCompat.START);
				}
			});

		}
	}

}
