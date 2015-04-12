package ru4pda.news.ui.article.one;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import ru4pda.news.Dao;
import ru4pda.news.R;
import ru4pda.news.client.Ru4pdaClient;
import ru4pda.news.ui.ViewUtils;

/**
 * Created by asavinova on 11/04/15.
 */
@EFragment(R.layout.article_one)
public class ArticleFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


	private static final int LOADER_ID = 0;

	@FragmentArg long id;
	@FragmentArg String title;

	@ViewById Toolbar toolbar;
	@ViewById SwipeRefreshLayout refresh;
	@ViewById ImageView imageView;
	@ViewById TextView titleView;
	@ViewById TextView dateView;
	@ViewById WebView webView;

	@Bean Dao dao;
	@Bean Ru4pdaClient client;

	@AfterViews
	void afterViews() {

		toolbar.setTitle(title);

		refresh.setOnRefreshListener(this);
		refresh.setColorSchemeResources(R.color.primary);
		refresh.setProgressViewOffset(false, 0,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

		loadData();
	}

	private void loadData() {
		refresh.setRefreshing(true);
		getLoaderManager().restartLoader(LOADER_ID, null, new Callbacks());
	}

	@Override
	public void onRefresh() {
		loadData();
	}

	@UiThread
	void updateData(ArticleTaskLoader.WrapperInfo info) {
		ViewUtils.loadImage(imageView, id);
		titleView.setText(info.article.getTitle());
		dateView.setText(ViewUtils.VERBOSE_DATE_FORMAT.format(info.article.getDate()));
		webView.loadData(info.content, "text/html; charset=utf-8", null);

		refresh.setRefreshing(false);
	}

	class Callbacks implements LoaderManager.LoaderCallbacks<ArticleTaskLoader.WrapperInfo> {

		@Override
		public Loader onCreateLoader(int loaderId, final Bundle args) {
			return new ArticleTaskLoader(getActivity(), dao, client, id);
		}

		@Override
		public void onLoadFinished(Loader loader, ArticleTaskLoader.WrapperInfo wrapperInfo) {
			updateData(wrapperInfo);
		}

		@Override
		public void onLoaderReset(Loader<ArticleTaskLoader.WrapperInfo> loader) {
		}
	}
}
