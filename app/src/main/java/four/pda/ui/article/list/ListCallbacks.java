package four.pda.ui.article.list;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import four.pda.R;
import four.pda.client.model.ListArticle;
import four.pda.ui.LoadResult;

/**
 * Created by asavinova on 24/05/16.
 */
public class ListCallbacks implements LoaderManager.LoaderCallbacks<LoadResult<Cursor>> {

	private static final Logger L = LoggerFactory.getLogger(ListCallbacks.class);

	static final String FORCE_BUNDLE_ARG = "force";

	private boolean force;
	private ListFragment fragment;

	public ListCallbacks(ListFragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public Loader<LoadResult<Cursor>> onCreateLoader(int id, final Bundle args) {
		return new AsyncTaskLoader<LoadResult<Cursor>>(fragment.getActivity()) {

			@Override
			public LoadResult<Cursor> loadInBackground() {
				force = args.getBoolean(FORCE_BUNDLE_ARG);
				if (force) {
					fragment.page = 1;
				}

				try {
					List<ListArticle> articles = fragment.client.getArticles(fragment.category, fragment.page);

					boolean needClearData = fragment.page == 1;
					fragment.dao.setArticles(articles, fragment.category, needClearData);

					return new LoadResult<>(fragment.dao.getArticleCursor(fragment.category));
				} catch (Exception e) {
					L.error("Articles page request error", e);
					return new LoadResult<>(e);
				}
			}

		};
	}

	@Override
	public void onLoadFinished(Loader<LoadResult<Cursor>> loader, LoadResult<Cursor> result) {
		fragment.refresh.setRefreshing(false);

		if (result.getException() == null) {
			fragment.page++;

			fragment.adapter.swapCursor(result.getData());
			fragment.adapter.notifyDataSetChanged();

			fragment.supportView.hide();
			fragment.upButton.setVisibility(View.VISIBLE);

			return;
		}

		int itemCount = fragment.adapter.getItemCount();

		View.OnClickListener retryListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fragment.loadData(force);
			}
		};

		if (itemCount == 0) {
			fragment.upButton.setVisibility(View.GONE);
			fragment.supportView.showError(fragment.getString(R.string.article_list_network_error), retryListener);
			return;
		}

		Snackbar
				.make(fragment.layout, R.string.article_list_network_error, Snackbar.LENGTH_INDEFINITE)
				.setAction(R.string.retry_button, retryListener)
				.show();
	}

	@Override
	public void onLoaderReset(Loader<LoadResult<Cursor>> loader) {
	}

}
