package four.pda.ui.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import four.pda.App;
import four.pda.Auth;
import four.pda.R;
import four.pda.client.FourPdaClient;
import four.pda.client.model.Captcha;
import four.pda.ui.SupportView;

/**
 * Created by asavinova on 19/02/16.
 */
@EActivity
public class AuthActivity extends AppCompatActivity {

	private static final Logger L = LoggerFactory.getLogger(AuthActivity.class);

	private static final int CAPTCHA_LOADER_ID = 0;
	private static final int LOGIN_LOADER_ID = 1;
	private static final int PROFILE_LOADER_ID = 2;

	@ViewById Toolbar toolbar;

	@ViewById TextInputEditText loginView;
	@ViewById TextInputEditText passwordView;
	@ViewById TextInputEditText captchaTextView;
	@ViewById ImageView captchaImageView;
	@ViewById SupportView supportView;

	@Inject Auth auth;
	@Inject FourPdaClient client;

	Captcha captcha;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		((App) getApplication()).component().inject(this);

		if (auth.isAuthorized()) {
			setResult(RESULT_OK);
			finish();
			return;
		}

		setContentView(R.layout.signin);
	}

	@AfterViews
	void afterViews() {
		L.debug("Start login activity");

		toolbar.setTitle(R.string.auth_title);
		toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		toolbar.inflateMenu(R.menu.auth);
		toolbar.setOnMenuItemClickListener(new MenuListener());

		loadCaptcha();
	}

	void loadCaptcha() {
		supportView.showProgress();
		getLoaderManager().restartLoader(CAPTCHA_LOADER_ID, null, new CaptchaCallbacks(this)).forceLoad();
	}

	void loadProfile() {
		supportView.showProgress();
		getLoaderManager().restartLoader(PROFILE_LOADER_ID, null, new ProfileCallbacks(this)).forceLoad();
	}

	void signIn() {
		supportView.showProgress();
		getLoaderManager().restartLoader(LOGIN_LOADER_ID, null, new LoginCallbacks(this)).forceLoad();
	}

	private class MenuListener implements Toolbar.OnMenuItemClickListener {

		@Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.sign_in:
                    signIn();
                    break;
            }
            return false;
        }

	}

}
