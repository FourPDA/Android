package ru4pda.news.ui.list;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import ru4pda.news.R;

/**
 * Created by asavinova on 10/04/15.
 */
@EActivity(R.layout.activity_list_articles)
public class ListActivity extends FragmentActivity {

	@ViewById Toolbar toolbar;

	@AfterViews
	void afterViews() {
		toolbar.setTitle(R.string.news_title);
	}

}
