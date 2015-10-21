package ru4pda.news.ui.article.list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

import ru4pda.news.EventBus;
import ru4pda.news.Preferences_;
import ru4pda.news.R;
import ru4pda.news.ui.CategoryType;
import ru4pda.news.ui.DrawerFragment;
import ru4pda.news.ui.article.ShowArticleEvent;
import ru4pda.news.ui.article.one.ArticleFragment_;

/**
 * Created by asavinova on 10/04/15.
 */
@EActivity(R.layout.activity_articles)
public class ListActivity extends FragmentActivity implements DrawerFragment.ChangeCategoryListener {

	private static final Logger L = LoggerFactory.getLogger(ListActivity.class);

	@FragmentById DrawerFragment drawer;

	@ViewById DrawerLayout drawerLayout;

	@InstanceState CategoryType category;
	@Pref Preferences_ preferences;
	@Bean EventBus eventBus;

	private Timer timer;
	private Toast toast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (category == null) {
			category = CategoryType.ALL;
		}

		if (savedInstanceState == null) {
			L.debug("Start list activity with category {}", getString(category.getTitle()));

			getSupportFragmentManager().beginTransaction()
					.add(R.id.list_container, ListFragment_.builder().category(category).build())
					.addToBackStack(null)
					.commit();
		}
	}

	@AfterViews
	void afterViews() {
		if (preferences.isFirstRun().get()) {
			if (drawerLayout != null) drawerLayout.openDrawer(Gravity.START);
			preferences.isFirstRun().put(false);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		drawer.addListener(this);
		eventBus.register(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		drawer.removeListener(this);
		eventBus.unregister(this);
		if (timer != null) timer.cancel();
	}

	@Override
	public void onChange(CategoryType newCategory) {
		L.debug("Category type changed on {} type", newCategory.name());
		if (drawerLayout != null) drawerLayout.closeDrawer(Gravity.START);

		Fragment itemFragment = getSupportFragmentManager().findFragmentById(R.id.item_container);
		if (category == newCategory) {
			if (itemFragment != null) {
				getSupportFragmentManager().beginTransaction()
						.remove(itemFragment)
						.addToBackStack(null)
						.commit();
			}
			return;
		}

		category = newCategory;
		if (itemFragment == null) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.list_container,
							ListFragment_.builder()
									.category(category)
									.build())
					.addToBackStack(null)
					.commit();
		} else {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.list_container,
							ListFragment_.builder()
									.category(category)
									.build())
					.remove(itemFragment)
					.addToBackStack(null)
					.commit();
		}
	}

	public void onEvent(ShowArticleEvent event) {
		L.debug("Show article id {}", event.getId());
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.item_container,
						ArticleFragment_.builder()
								.id(event.getId())
								.build())
				.addToBackStack(null)
				.commit();
	}

	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
			if (timer != null) {
				if (toast != null) toast.cancel();
				finish();
			} else {

				toast = Toast.makeText(this, R.string.exit_message, Toast.LENGTH_SHORT);
				toast.show();

				timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						timer = null;
					}
				}, 2000);

			}
		} else {
			super.onBackPressed();
		}
	}
}
