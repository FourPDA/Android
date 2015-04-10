package ru4pda.news.ui.item;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.io.IOException;

import ru4pda.news.Dao;
import ru4pda.news.R;
import ru4pda.news.client.Ru4pdaClient;
import ru4pda.news.client.model.FullArticle;
import ru4pda.news.client.model.SimpleArticle;
import ru4pda.news.dao.Article;
import ru4pda.news.ui.ViewUtils;

/**
 * Created by asavinova on 11/04/15.
 */
@EFragment(R.layout.fragment_article)
public class ArticleFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

	private static final int LOADER_ID = 0;

	@FragmentArg long id;

	@ViewById SwipeRefreshLayout refresh;
	@ViewById ImageView imageView;
	@ViewById TextView titleView;
	@ViewById TextView dateView;
	@ViewById WebView webView;

	@Bean Dao dao;
	@Bean Ru4pdaClient client;

	@AfterViews
	void afterViews() {

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
	void updateData(FullArticle article) {
		ViewUtils.loadImage(imageView, id);
		SimpleArticle simpleArticle = article.getSimpleArticle();
		titleView.setText(simpleArticle.getTitle());
		dateView.setText(ViewUtils.VERBOSE_DATE_FORMAT.format(simpleArticle.getDate()));
		webView.loadData(article.getContent(), "text/html; charset=utf-8", null);

		refresh.setRefreshing(false);
	}

	class Callbacks implements LoaderManager.LoaderCallbacks<FullArticle> {

		@Override
		public Loader<FullArticle> onCreateLoader(final int id, final Bundle args) {
			return new AsyncTaskLoader<FullArticle>(getActivity()) {
				@Override
				protected void onStartLoading() {
					super.onStartLoading();
					forceLoad();
				}

				@Override
				public FullArticle loadInBackground() {
					Article article = dao.getArticle(ArticleFragment.this.id);
					SimpleArticle simpleArticle = new SimpleArticle();
					simpleArticle.setId(article.getId());
					simpleArticle.setDate(article.getDate());
					simpleArticle.setTitle(article.getTitle());
					simpleArticle.setDescription(article.getDescription());

					try {
						return client.getContentArticle(simpleArticle);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return null;
				}
			};
		}

		@Override
		public void onLoadFinished(Loader<FullArticle> loader, FullArticle data) {
			updateData(data);
		}

		@Override
		public void onLoaderReset(Loader<FullArticle> loader) {
		}
	}
}
