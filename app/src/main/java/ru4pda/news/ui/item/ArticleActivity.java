package ru4pda.news.ui.item;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import ru4pda.news.R;

/**
 * Created by asavinova on 11/04/15.
 */
@EActivity(R.layout.activity_article)
public class ArticleActivity extends FragmentActivity {

	@ViewById Toolbar toolbar;

	@Extra String title;
	@Extra long id;

	@AfterViews
	void afterViews() {
		toolbar.setTitle(title);

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.container, ArticleFragment_.builder().id(id).build())
				.commit();
	}

}
