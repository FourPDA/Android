package four.pda.ui.article.list;

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

import four.pda.EventBus;
import four.pda.Preferences_;
import four.pda.R;
import four.pda.client.CategoryType;
import four.pda.ui.CategoryTitleMap;
import four.pda.ui.DrawerFragment;
import four.pda.ui.article.ShowArticleEvent;
import four.pda.ui.article.ShowCommentsEvent;
import four.pda.ui.article.comments.CommentsFragment_;
import four.pda.ui.article.one.ArticleFragment_;

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
			String categoryName = getString(CategoryTitleMap.get(category));
			L.debug("Start list activity with category {}", categoryName);

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
								.date(event.getDate())
								.title(event.getTitle())
								.image(event.getImage())
								.build())
				.addToBackStack(null)
				.commit();
	}

	public void onEvent(ShowCommentsEvent event) {
		L.debug("Show comments for article id {}", event.getId());
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.item_container,
						CommentsFragment_.builder()
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
