package four.pda.ui.article.comments;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import four.pda.R;
import four.pda.client.model.CommentsResponse;
import four.pda.dao.Article;
import four.pda.ui.LoadResult;

/**
 * Created by asavinova on 11/03/16.
 */
public class CommentsCallbacks implements LoaderManager.LoaderCallbacks<LoadResult<CommentsResponse>> {

	private static final Logger L = LoggerFactory.getLogger(CommentsCallbacks.class);

	private CommentsFragment fragment;

	public CommentsCallbacks(CommentsFragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public Loader<LoadResult<CommentsResponse>> onCreateLoader(final int id, Bundle args) {
		return new AsyncTaskLoader<LoadResult<CommentsResponse>>(fragment.getActivity()) {
			@Override
			public LoadResult<CommentsResponse> loadInBackground() {
				Article article = fragment.dao.getArticle(fragment.id);
				try {
					return new LoadResult<>(fragment.client.getArticleComments(article.getDate(), article.getId()));
				} catch (Exception e) {
					L.error("Article comments request error", e);
					return new LoadResult<>(e);
				}
			}
		};
	}

	@Override
	public void onLoadFinished(Loader<LoadResult<CommentsResponse>> loader, LoadResult<CommentsResponse> result) {
		fragment.refresh.setRefreshing(false);

		if (result.getException() == null) {
			fragment.adapter.setComments(result.getData().getComments());
			fragment.adapter.notifyDataSetChanged();
			fragment.supportView.hide();
			return;
		}

		fragment.supportView.showError(fragment.getString(R.string.comments_network_error), new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fragment.loadData();
			}
		});
	}

	@Override
	public void onLoaderReset(Loader<LoadResult<CommentsResponse>> loader) {
	}

}
