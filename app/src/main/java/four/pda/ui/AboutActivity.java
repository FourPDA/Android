package four.pda.ui;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import four.pda.BuildConfig;
import four.pda.R;


/**
 * Created by asavinova on 15/04/15.
 */
@EActivity(R.layout.activity_about)
public class AboutActivity extends ActionBarActivity {

	private static final Logger L = LoggerFactory.getLogger(AboutActivity.class);

	@ViewById Toolbar toolbar;

	@ViewById TextView descriptionTextView;
	@ViewById TextView sourcesTextView;
	@ViewById TextView versionTextView;

	@ViewById TextView userSwapi;
	@ViewById TextView userVarann;

	@AfterViews
	void afterViews() {
		L.debug("Start about activity");

		toolbar.setTitle(R.string.about);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		descriptionTextView.setText(R.string.about_description);
		sourcesTextView.setText(R.string.about_sources);

		String versionName = BuildConfig.VERSION_NAME;
		int versionCode = BuildConfig.VERSION_CODE;
		String type = BuildConfig.DEBUG ? "debug" : "";
		versionTextView.setText(getString(R.string.about_version, versionName, versionCode, type));

		userSwapi.setText(Html.fromHtml(getString(R.string.about_user_swapi)));
		userVarann.setText(Html.fromHtml(getString(R.string.about_user_varann)));
	}

}
