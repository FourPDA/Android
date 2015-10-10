package ru4pda.news.ui;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import ru4pda.news.BuildConfig;
import ru4pda.news.R;

/**
 * Created by asavinova on 15/04/15.
 */
@EActivity(R.layout.activity_about)
public class AboutActivity extends ActionBarActivity {

	@ViewById Toolbar toolbar;

	@ViewById TextView descriptionTextView;
	@ViewById TextView sourcesTextView;
	@ViewById TextView versionTextView;

	@ViewById TextView userSwapi;
	@ViewById TextView userVarann;

	@AfterViews
	void afterViews() {
		toolbar.setTitle(R.string.about);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		descriptionTextView.setText(R.string.about_description);
		sourcesTextView.setText(R.string.about_sources);

		String version = BuildConfig.VERSION_NAME + (BuildConfig.DEBUG ? " (debug)" : "");
		versionTextView.setText(getString(R.string.about_version, version));

		userSwapi.setText(Html.fromHtml(getString(R.string.about_user_swapi)));
		userVarann.setText(Html.fromHtml(getString(R.string.about_user_varann)));
	}

}
