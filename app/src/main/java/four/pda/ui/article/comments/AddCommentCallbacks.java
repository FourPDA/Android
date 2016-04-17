package four.pda.ui.article.comments;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import four.pda.R;
import four.pda.client.model.AbstractComment;
import four.pda.ui.LoadResult;

/**
 * Created by asavinova on 16/03/16.
 */
public class AddCommentCallbacks implements LoaderManager.LoaderCallbacks<LoadResult<List<AbstractComment>>> {

	private static final Logger L = LoggerFactory.getLogger(AddCommentCallbacks.class);

	private AddCommentFragment fragment;

	public AddCommentCallbacks(AddCommentFragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public Loader<LoadResult<List<AbstractComment>>> onCreateLoader(final int id, Bundle args) {
		return new AsyncTaskLoader<LoadResult<List<AbstractComment>>>(fragment.getActivity()) {
			@Override
			public LoadResult<List<AbstractComment>> loadInBackground() {
				try {
					String message = fragment.messageEditText.getText().toString();
					return new LoadResult<>(fragment.client.addComment(message, fragment.replyId));
				} catch (Exception e) {
					L.error("Add comment request error", e);
					return new LoadResult<>(e);
				}
			}
		};
	}

	@Override
	public void onLoadFinished(Loader<LoadResult<List<AbstractComment>>> loader, LoadResult<List<AbstractComment>> result) {
		fragment.supportView.hide();

		if (result.getException() == null) {
			fragment.updateComments(result.getData());
			return;
		}

		fragment.supportView.showError(fragment.getString(R.string.add_comment_network_error), new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fragment.addComment();
			}
		});
	}

	@Override
	public void onLoaderReset(Loader<LoadResult<List<AbstractComment>>> loader) {
	}

}
