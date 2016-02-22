package four.pda.ui.auth;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import four.pda.R;
import four.pda.client.LoginParams;
import four.pda.client.model.LoginResult;
import four.pda.ui.LoadResult;

/**
 * Created by asavinova on 21/02/16.
 */
class LoginCallbacks implements LoaderManager.LoaderCallbacks<LoadResult<LoginResult>> {

	private static final Logger L = LoggerFactory.getLogger(LoginCallbacks.class);

	private AuthActivity activity;

	public LoginCallbacks(AuthActivity activity) {
		this.activity = activity;
	}

	@Override
	public Loader<LoadResult<LoginResult>> onCreateLoader(int id, Bundle args) {
		return new AsyncTaskLoader<LoadResult<LoginResult>>(activity) {
			@Override
			public LoadResult<LoginResult> loadInBackground() {
				try {
					LoginParams params = new LoginParams();
					params.setLogin(activity.loginView.getText().toString());
					params.setPassword(activity.passwordView.getText().toString());
					params.setCaptcha(activity.captchaTextView.getText().toString());
					if (activity.captcha != null) {
						params.setCaptchaSig(activity.captcha.getSig());
						params.setCaptchaTime(activity.captcha.getTime());
					}
					return new LoadResult<>(activity.client.getInstance().login(params));
				} catch (IOException e) {
					L.error("Login request error", e);
					return new LoadResult<>(e);
				}
			}
		};
	}

	@Override
	public void onLoadFinished(Loader<LoadResult<LoginResult>> loader, LoadResult<LoginResult> result) {

		if (result.isError()) {
			activity.supportView.showError(activity.getString(R.string.auth_network_error), new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					activity.loginClicked();
				}
			});
			return;
		}

		LoginResult loginResult = result.getData();
		activity.supportView.hide();

		if (loginResult.getResult() == LoginResult.Result.ERROR) {

			StringBuilder errors = new StringBuilder();
			for (String e : loginResult.getErrors()) {
				errors.append(e);
				errors.append(" ");
			}
			activity.supportView.showError(errors.toString().trim(), new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					activity.loadCaptcha();
				}
			});
			return;
		}

		activity.preferences.profileId().put(loginResult.getMemberId());
		activity.loadProfile();
	}

	@Override
	public void onLoaderReset(Loader<LoadResult<LoginResult>> loader) {
	}

}
