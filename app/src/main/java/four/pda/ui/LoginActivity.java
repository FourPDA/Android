package four.pda.ui;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import four.pda.FourPdaClient;
import four.pda.Preferences_;
import four.pda.R;
import four.pda.client.model.Captcha;
import four.pda.client.model.LoginResult;
import four.pda.client.model.Profile;

/**
 * Created by asavinova on 19/02/16.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

	private static final Logger L = LoggerFactory.getLogger(LoginActivity.class);

	private static final int CAPTCHA_LOADER_ID = 0;
	private static final int LOGIN_LOADER_ID = 1;
	private static final int PROFILE_LOADER_ID = 2;

	@ViewById Toolbar toolbar;

	@ViewById EditText loginView;
	@ViewById EditText passwordView;
	@ViewById AspectRatioImageView captchaImageView;
	@ViewById EditText captchaTextView;
	@ViewById Button enterView;
	@ViewById SupportView supportView;

	@Bean FourPdaClient client;
	@Pref Preferences_ preferences;

	private Captcha captcha;

	@AfterViews
	void afterViews() {
		L.debug("Start login activity");

		toolbar.setTitle(R.string.auth_title);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		loadCaptcha();
	}

	private void loadCaptcha() {
		supportView.showProgress();
		getLoaderManager().restartLoader(CAPTCHA_LOADER_ID, null, new CaptchaCallbacks()).forceLoad();
	}

	@Click(R.id.enter_view)
	void loginClicked() {
		supportView.showProgress();
		getLoaderManager().restartLoader(LOGIN_LOADER_ID, null, new LoginCallbacks()).forceLoad();
	}

	private void loadProfile() {
		supportView.showProgress();
		getLoaderManager().restartLoader(PROFILE_LOADER_ID, null, new ProfileCallbacks()).forceLoad();
	}

	class CaptchaCallbacks implements LoaderManager.LoaderCallbacks<LoadResult<Captcha>> {
		@Override
		public Loader<LoadResult<Captcha>> onCreateLoader(int id, Bundle args) {
			return new AsyncTaskLoader<LoadResult<Captcha>>(LoginActivity.this) {
				@Override
				public LoadResult<Captcha> loadInBackground() {
					try {
						return new LoadResult<>(client.getInstance().getCaptcha());
					} catch (IOException e) {
						L.error("Logout request error", e);
						return new LoadResult<>(e);
					}
				}
			};
		}

		@Override
		public void onLoadFinished(Loader<LoadResult<Captcha>> loader, LoadResult<Captcha> result) {
			if (result.getException() == null) {
				captcha = result.getData();
				ViewUtils.loadImage(captchaImageView, captcha.getUrl());
				supportView.hide();
			} else {
				supportView.showError(getString(R.string.article_network_error), new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						loadCaptcha();
					}
				});
			}
		}

		@Override
		public void onLoaderReset(Loader<LoadResult<Captcha>> loader) {
		}
	}

	class LoginCallbacks implements LoaderManager.LoaderCallbacks<LoadResult<LoginResult>> {
		@Override
		public Loader<LoadResult<LoginResult>> onCreateLoader(int id, Bundle args) {
			return new AsyncTaskLoader<LoadResult<LoginResult>>(LoginActivity.this) {
				@Override
				public LoadResult<LoginResult> loadInBackground() {
					try {
						four.pda.client.FourPdaClient.LoginParams params = new four.pda.client.FourPdaClient.LoginParams();
						params.setLogin(loginView.getText().toString());
						params.setPassword(passwordView.getText().toString());
						params.setCaptcha(captchaTextView.getText().toString());
						if (captcha != null) {
							params.setCaptchaSig(captcha.getSig());
							params.setCaptchaTime(captcha.getTime());
						}
						return new LoadResult<>(client.getInstance().login(params));
					} catch (IOException e) {
						L.error("Login request error", e);
						return new LoadResult<>(e);
					}
				}
			};
		}

		@Override
		public void onLoadFinished(Loader<LoadResult<LoginResult>> loader, LoadResult<LoginResult> result) {
			if (result.getException() == null) {
				LoginResult loginResult = result.getData();
				supportView.hide();

				if (LoginResult.Result.OK.equals(loginResult.getResult())) {
					preferences.profileId().put(loginResult.getMemberId());
					loadProfile();
				} else {
					StringBuilder errors = new StringBuilder();
					for (String e : loginResult.getErrors()) {
						errors.append(e);
						errors.append(" ");
					}
					supportView.showError(errors.toString().trim(), new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							loadCaptcha();
						}
					});
				}
			} else {
				supportView.showError(getString(R.string.article_network_error), new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						loginClicked();
					}
				});
			}
		}

		@Override
		public void onLoaderReset(Loader<LoadResult<LoginResult>> loader) {
		}
	}

	class ProfileCallbacks implements LoaderManager.LoaderCallbacks<LoadResult<Profile>> {
		@Override
		public Loader<LoadResult<Profile>> onCreateLoader(int id, Bundle args) {
			return new AsyncTaskLoader<LoadResult<Profile>>(LoginActivity.this) {
				@Override
				public LoadResult<Profile> loadInBackground() {
					try {
						return new LoadResult<>(client.getInstance().getProfile(preferences.profileId().get()));
					} catch (IOException e) {
						L.error("Profile request error", e);
						return new LoadResult<>(e);
					}
				}
			};
		}

		@Override
		public void onLoadFinished(Loader<LoadResult<Profile>> loader, LoadResult<Profile> result) {
			if (result.getException() == null) {
				Profile profile = result.getData();
				preferences.profileLogin().put(profile.getLogin());
				preferences.profilePhoto().put(profile.getPhoto());

				supportView.hide();
				setResult(RESULT_OK);
				finish();
			} else {
				supportView.showError(getString(R.string.article_network_error), new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						loadProfile();
					}
				});
			}
		}

		@Override
		public void onLoaderReset(Loader<LoadResult<Profile>> loader) {
		}
	}

}
