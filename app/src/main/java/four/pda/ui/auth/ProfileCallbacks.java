package four.pda.ui.auth;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import four.pda.Preferences_;
import four.pda.R;
import four.pda.client.FourPdaClient;
import four.pda.client.model.Profile;
import four.pda.ui.LoadResult;

/**
 * Created by asavinova on 21/02/16.
 */
class ProfileCallbacks implements LoaderManager.LoaderCallbacks<LoadResult<Profile>> {

	private static final Logger L = LoggerFactory.getLogger(ProfileCallbacks.class);

	private AuthActivity activity;
	private FourPdaClient pdaClient;
	private Preferences_ preferences;

	public ProfileCallbacks(AuthActivity activity) {
		this.activity = activity;
		pdaClient = activity.client;
		preferences = activity.preferences;
	}

	@Override
	public Loader<LoadResult<Profile>> onCreateLoader(int id, Bundle args) {
		return new AsyncTaskLoader<LoadResult<Profile>>(activity) {
			@Override
			public LoadResult<Profile> loadInBackground() {
				try {
					Long memberId = preferences.profileId().get();
					return new LoadResult<>(pdaClient.getProfile(memberId));
				} catch (Exception e) {
					L.error("Profile request error", e);
					return new LoadResult<>(e);
				}
			}
		};
	}

	@Override
	public void onLoadFinished(Loader<LoadResult<Profile>> loader, LoadResult<Profile> result) {

		if (result.isError()) {
			activity.supportView.showError(activity.getString(R.string.auth_network_error), new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					activity.loadProfile();
				}
			});
			return;
		}

		Profile profile = result.getData();
		preferences.profileLogin().put(profile.getLogin());
		preferences.profilePhoto().put(profile.getPhoto());

		activity.supportView.hide();
		activity.setResult(Activity.RESULT_OK);
		activity.finish();
	}

	@Override
	public void onLoaderReset(Loader<LoadResult<Profile>> loader) {
	}

}
