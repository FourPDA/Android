package four.pda.ui.article.search;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import four.pda.client.model.SearchContainer;
import four.pda.ui.LoadResult;

/**
 * Created by asavinova on 07/05/16.
 */
public class SearchCallbacks implements LoaderManager.LoaderCallbacks<LoadResult<SearchContainer>> {

	private static final Logger L = LoggerFactory.getLogger(SearchCallbacks.class);

	private static final String SEARCH_CRITERIA_BUNDLE_ARG = "search";
	private static final String CURRENT_PAGE_BUNDLE_ARG = "page";

	private SearchFragment fragment;

	public SearchCallbacks(SearchFragment fragment) {
		this.fragment = fragment;
	}

	public static Bundle createBundle(String searchText, int currentPage) {
		Bundle bundle = new Bundle();
		bundle.putString(SEARCH_CRITERIA_BUNDLE_ARG, searchText);
		bundle.putInt(CURRENT_PAGE_BUNDLE_ARG, currentPage);
		return bundle;
	}

	@Override
	public Loader<LoadResult<SearchContainer>> onCreateLoader(int id, final Bundle args) {
		return new AsyncTaskLoader<LoadResult<SearchContainer>>(fragment.getActivity()) {

			@Override
			public LoadResult<SearchContainer> loadInBackground() {

				String searchCriteria = args.getString(SEARCH_CRITERIA_BUNDLE_ARG);
				int currentPage = args.getInt(CURRENT_PAGE_BUNDLE_ARG);

				try {
					SearchContainer container = fragment.client.searchArticles(searchCriteria, currentPage + 1);
					boolean needClearData = currentPage == 0;
					fragment.dao.setSearchArticles(container.getArticles(), needClearData);

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
			fragment.updateData(result.getData());
			return;
		}

		fragment.showError();
	}

	@Override
	public void onLoaderReset(Loader<LoadResult<SearchContainer>> loader) {
	}

}
