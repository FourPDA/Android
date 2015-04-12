package ru4pda.news.ui.article.one;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import ru4pda.news.R;

/**
 * Created by asavinova on 11/04/15.
 */
@EActivity
public class ArticleActivity extends FragmentActivity {

	@Extra String title;
	@Extra long id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FrameLayout layout = new FrameLayout(this);
		layout.setId(R.id.container);
		setContentView(layout);

		if (savedInstanceState == null) {

			ArticleFragment fragment = ArticleFragment_.builder()
					.id(id)
					.title(title)
					.build();

			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.container, fragment)
					.commit();

		}
	}

}
