package four.pda.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import four.pda.FourPdaClient;
import four.pda.Preferences_;
import four.pda.R;
import four.pda.analytics.Analytics;

/**
 * @author Anna Savinova
 * @author Pavel Savinov
 */
@EFragment(R.layout.drawer)
public class DrawerFragment extends Fragment {

	private static final Logger L = LoggerFactory.getLogger(DrawerFragment.class);

	private static final int LOGOUT_LOADER_ID = 0;

	@ViewById View allCategoryView;
	@ViewById View newsCategoryView;
	@ViewById View reviewsCategoryView;
	@ViewById View articlesCategoryView;
	@ViewById View softwareCategoryView;
	@ViewById View gamesCategoryView;
	@ViewById View loginView;
	@ViewById View logoutView;
	@ViewById View logoView;
	@ViewById View profilePanel;
	@ViewById ImageView profilePhotoView;
	@ViewById TextView profileLoginView;

	@Bean Analytics analytics;
	@Bean FourPdaClient client;
	@Pref Preferences_ preferences;

	private List<ChangeCategoryListener> listeners = new ArrayList<>();
    private Map<View, CategoryType> map = new HashMap<>();

    public void addListener(ChangeCategoryListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ChangeCategoryListener listener) {
        listeners.remove(listener);
    }

    @AfterViews
    void afterViews() {

        map.put(allCategoryView, CategoryType.ALL);
        map.put(newsCategoryView, CategoryType.NEWS);
        map.put(gamesCategoryView, CategoryType.GAMES);
        map.put(reviewsCategoryView, CategoryType.REVIEWS);
        map.put(articlesCategoryView, CategoryType.ARTICLES);
        map.put(softwareCategoryView, CategoryType.SOFTWARE);

        for (View view : map.keySet()) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    analytics.drawer().categoryClicked(map.get(view));
                    setViewSelected(view);
                }
            });
        }

		updateProfile();
    }

	private void updateProfile() {
		long profileId = preferences.profileId().get();
		String login = preferences.profileLogin().get();
		String photo = preferences.profilePhoto().get();

		boolean isAuthorized = profileId != 0;

		loginView.setVisibility(isAuthorized ? View.GONE : View.VISIBLE);
		logoutView.setVisibility(isAuthorized ? View.VISIBLE : View.GONE);
		logoView.setVisibility(isAuthorized ? View.GONE : View.VISIBLE);
		profilePanel.setVisibility(isAuthorized ? View.VISIBLE : View.GONE);

		if (isAuthorized) {
			profileLoginView.setText(login);
			ViewUtils.loadImage(profilePhotoView, photo);
		}
	}

	@Click(R.id.login_view)
    void loginClicked() {
		// Тестовые данные
        preferences.profileId().put(1l);
		preferences.profileLogin().put("Test");
		preferences.profilePhoto().put("http://s.4pda.to/tp6nuQlKPdPSv8fwz1HfNVeHMOUxPbaFg.jpg");

		updateProfile();
    }

	@Click(R.id.logout_view)
    void logoutClicked() {
		getLoaderManager().restartLoader(LOGOUT_LOADER_ID, null, new LogoutCallbacks()).forceLoad();
	}

    @Click(R.id.about_view)
    void aboutClicked() {
        analytics.drawer().aboutClicked();
        AboutActivity_.intent(getActivity()).start();
    }

    public void setCategorySelected(CategoryType categoryType) {
        for (Map.Entry<View, CategoryType> entry : map.entrySet()) {
            if (entry.getValue() == categoryType) {
                setViewSelected(entry.getKey());
                return;
            }
        }
        throw new IllegalStateException("No view for category " + categoryType.name());
    }

    private void setViewSelected(View view) {
        for (View iterView : map.keySet()) {
            iterView.setSelected(false);
        }
        view.setSelected(true);
        for (ChangeCategoryListener listener : listeners) {
            listener.onChange(map.get(view));
        }
    }

    public interface ChangeCategoryListener {
        void onChange(CategoryType type);
    }

	class LogoutCallbacks implements LoaderManager.LoaderCallbacks<LoadResult<Boolean>> {

		@Override
		public Loader onCreateLoader(int loaderId, final Bundle args) {
			return new AsyncTaskLoader(getActivity()) {
				@Override
				public LoadResult<Boolean> loadInBackground() {
					try {
						return new LoadResult<>(client.getInstance().logout());
					} catch (IOException e) {
						L.error("Logout request error", e);
						return new LoadResult<>(e);
					}
				}
			};
		}

		@Override
		public void onLoadFinished(Loader<LoadResult<Boolean>> loader, LoadResult<Boolean> result) {
			if (result.getException() == null && result.getData()) {
				preferences.profileId().put(0l);
				preferences.profileLogin().put(null);
				preferences.profilePhoto().put(null);

				updateProfile();
			} else {
				//TODO Нужно ли запрашивать заново в случае ошибки?
			}
		}

		@Override
		public void onLoaderReset(Loader<LoadResult<Boolean>> loader) {
		}

	}
}
