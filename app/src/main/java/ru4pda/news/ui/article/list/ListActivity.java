package ru4pda.news.ui.article.list;

import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru4pda.news.R;
import ru4pda.news.ui.CategoryType;
import ru4pda.news.ui.DrawerFragment;

/**
 * Created by asavinova on 10/04/15.
 */
@EActivity(R.layout.activity_articles)
public class ListActivity extends FragmentActivity implements DrawerFragment.ChangeCategoryListener {

	private static final Logger L = LoggerFactory.getLogger(ListActivity.class);

	@FragmentById DrawerFragment drawer;

	@ViewById DrawerLayout drawerLayout;

	@AfterViews
	void afterViews() {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, ListFragment_.builder().build())
				.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		drawer.addListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		drawer.removeListener(this);
	}

	@Override
	public void onChange(CategoryType type) {
		L.trace("Category type changed on {} type", type.name());
		drawerLayout.closeDrawer(Gravity.START);
	}
}
