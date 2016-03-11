package four.pda.ui.auth;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import four.pda.App;
import four.pda.Preferences_;
import four.pda.R;
import four.pda.client.FourPdaClient;
import four.pda.client.model.Captcha;
import four.pda.ui.AspectRatioImageView;
import four.pda.ui.SupportView;

/**
 * Created by asavinova on 19/02/16.
 */
@EActivity(R.layout.activity_login)
public class AuthActivity extends AppCompatActivity {

	private static final Logger L = LoggerFactory.getLogger(AuthActivity.class);

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

	@Inject FourPdaClient client;

	@Pref Preferences_ preferences;

	Captcha captcha;

	@AfterViews
	void afterViews() {
		L.debug("Start login activity");
		((App) getApplication()).component().inject(this);

		toolbar.setTitle(R.string.auth_title);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		loadCaptcha();
	}

	void loadCaptcha() {
		supportView.showProgress();
		getLoaderManager().restartLoader(CAPTCHA_LOADER_ID, null, new CaptchaCallbacks(this)).forceLoad();
	}

	@Click(R.id.enter_view)
	void loginClicked() {
		supportView.showProgress();
		getLoaderManager().restartLoader(LOGIN_LOADER_ID, null, new LoginCallbacks(this)).forceLoad();
	}

	void loadProfile() {
		supportView.showProgress();
		getLoaderManager().restartLoader(PROFILE_LOADER_ID, null, new ProfileCallbacks(this)).forceLoad();
	}

}
