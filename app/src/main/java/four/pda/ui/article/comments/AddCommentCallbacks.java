package four.pda.ui.article.comments;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import four.pda.R;
import four.pda.client.model.CommentsContainer;
import four.pda.ui.LoadResult;

/**
 * Created by asavinova on 16/03/16.
 */
public class AddCommentCallbacks implements LoaderManager.LoaderCallbacks<LoadResult<CommentsContainer>> {

	private static final Logger L = LoggerFactory.getLogger(AddCommentCallbacks.class);

	private AddCommentDialog fragment;

	public AddCommentCallbacks(AddCommentDialog fragment) {
		this.fragment = fragment;
	}

	@Override
	public AddCommentLoader onCreateLoader(int id, Bundle args) {
		return new AddCommentLoader();
	}

	@Override
	public void onLoadFinished(Loader<LoadResult<CommentsContainer>> loader, LoadResult<CommentsContainer> result) {
		fragment.supportView.hide();

		if (result.getException() != null) {
			fragment.supportView.showError(fragment.getString(R.string.add_comment_network_error), new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					fragment.addComment();
				}
			});
			return;
		}

		fragment.updateComments(result.getData());
	}

	@Override
	public void onLoaderReset(Loader<LoadResult<CommentsContainer>> loader) {
	}

	private class AddCommentLoader extends AsyncTaskLoader<LoadResult<CommentsContainer>> {

		public AddCommentLoader() {
			super(AddCommentCallbacks.this.fragment.getActivity());
		}

		@Override
        public LoadResult<CommentsContainer> loadInBackground() {
            try {
                String message = fragment.messageEditText.getText().toString();
                CommentsContainer container = fragment.client.addComment(
						fragment.postId,
                        fragment.replyId,
                        message
				);
                return new LoadResult<>(container);
            } catch (Exception e) {
                L.error("Add comment request error", e);
                return new LoadResult<>(e);
            }
        }

	}

}
