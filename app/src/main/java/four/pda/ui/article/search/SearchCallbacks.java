package four.pda.ui.article.search;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import four.pda.R;
import four.pda.client.CategoryType;
import four.pda.client.model.SearchContainer;
import four.pda.ui.LoadResult;

/**
 * Created by asavinova on 07/05/16.
 */
public class SearchCallbacks implements LoaderManager.LoaderCallbacks<LoadResult<SearchContainer>> {

	private static final Logger L = LoggerFactory.getLogger(SearchCallbacks.class);

	private SearchFragment fragment;

	public SearchCallbacks(SearchFragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public Loader<LoadResult<SearchContainer>> onCreateLoader(int id, final Bundle args) {
		return new AsyncTaskLoader<LoadResult<SearchContainer>>(fragment.getActivity()) {

			@Override
			public LoadResult<SearchContainer> loadInBackground() {

				try {
					SearchContainer container = fragment.client.searchArticles(fragment.search, fragment.nextPage);
					boolean needClearData = fragment.nextPage == 1;
					fragment.dao.setArticles(container.getArticles(), CategoryType.SEARCH, needClearData);

					return new LoadResult<>(container);
				} catch (Exception e) {
					L.error("Search articles request error", e);
					return new LoadResult<>(e);
				}
			}

		};
	}

	@Override
	public void onLoadFinished(Loader<LoadResult<SearchContainer>> loader, LoadResult<SearchContainer> result) {
		fragment.supportView.hide();

		if (result.getException() == null) {
			SearchContainer container = result.getData();
			fragment.nextPage = container.getCurrentPage() + 1;
			fragment.hasNextPage = container.hasNextPage();

			Cursor cursor = fragment.dao.getArticleCursor(CategoryType.SEARCH);
			fragment.adapter.swapCursor(cursor);
			fragment.adapter.notifyDataSetChanged();

			fragment.supportView.hide();
			fragment.upButton.setVisibility(View.VISIBLE);

			return;
		}

		int itemCount = fragment.adapter.getItemCount();

		View.OnClickListener retryListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fragment.loadData();
			}
		};

		if (itemCount == 1) {
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
	public void onLoaderReset(Loader<LoadResult<SearchContainer>> loader) {
	}

}
