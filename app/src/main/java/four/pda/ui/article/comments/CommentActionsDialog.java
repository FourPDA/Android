package four.pda.ui.article.comments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.auto.value.AutoValue;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import four.pda.App;
import four.pda.EventBus;
import four.pda.R;
import four.pda.client.FourPdaClient;
import four.pda.client.model.Comment;
import four.pda.ui.LoadResult;

/**
 * Created by asavinova on 25/04/16.
 */
@EFragment(R.layout.comment_actions_dialog)
public class CommentActionsDialog extends DialogFragment {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy HH:ss");

	@FragmentArg Params params;

	@ViewById Toolbar toolbar;

	@ViewById TextView nickView;
	@ViewById TextView dateView;

	@ViewById View likesCheckView;
	@ViewById TextView likesCountView;

	@ViewById TextView contentView;

	@ViewById TextView replyButton;

	@ViewById View likeProgressView;
 	@ViewById ImageView likeButton;

	@Bean EventBus eventBus;

	@Inject FourPdaClient client;

	@AfterViews
	void afterViews() {

		((App) getContext().getApplicationContext()).component().inject(this);

		toolbar.setTitle(R.string.show_comment_dialog_title);
		toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CommentActionsDialog.this.dismiss();
			}
		});

		nickView.setText(params.nickname());

		String verboseDate = DATE_FORMAT.format(params.date());
		dateView.setText(verboseDate);

		contentView.setText(Html.fromHtml(params.content()));

		replyButton.setVisibility(params.canReply() ? View.VISIBLE : View.GONE);

		updateLikes();

	}

	@Click(R.id.share_button)
	void share() {
		startActivity(ShareCompat.IntentBuilder.from(getActivity())
				.setType("text/plain")
				.setText(client.getCommentUrl(params.articleId(), params.articleDate(), params.id()))
				.createChooserIntent());
		dismiss();
	}

	@Click(R.id.like_button)
	void likeButton() {

		switch (params.canLike()) {

			case ALREADY_LIKED:
				Toast.makeText(getContext(), "Уже залайкано", Toast.LENGTH_SHORT).show();
				break;

			case CAN:

				likeButton.setVisibility(View.INVISIBLE);
				likeProgressView.setVisibility(View.VISIBLE);

				Bundle args = new Bundle();
				args.putLong(LikeCommentLoaderCallbacks.ARTICLE_ID_BUNDLE_LONG_ARG, params.articleId());
				args.putLong(LikeCommentLoaderCallbacks.COMMENT_ID_BUNDLE_LONG_ARG, params.id());
				getLoaderManager().initLoader(0, args,
						new LikeCommentLoaderCallbacks(getContext(), client)).forceLoad();

				break;

			default:
				throw new IllegalStateException("Unexpected CanLike value " + params.canLike().name());

		}

	}

	@Click(R.id.reply_button)
	void reply() {
		eventBus.post(new AddCommentEvent(params.id(), params.nickname()));
		dismiss();
	}

	private void updateLikes() {

		likesCountView.setText(String.valueOf(params.likeCount()));

		likesCheckView.setVisibility(
				params.canLike() == Comment.CanLike.ALREADY_LIKED ?
						View.VISIBLE : View.GONE
		);

		likeButton.setVisibility(params.canLike() == Comment.CanLike.CANT ? View.INVISIBLE : View.VISIBLE);

	}

	@AutoValue
	abstract static class Params implements Parcelable {

		abstract long id();

		abstract String nickname();

		abstract Date date();

		/**
		 * @return {@link four.pda.client.model.Comment.CanLike#serverValue}
		 * @see four.pda.client.model.Comment.CanLike#fromServerValue(int)
		 */
		abstract Comment.CanLike canLike();

		abstract int likeCount();

		abstract String content();

		abstract boolean canReply();

		abstract long articleId();

		abstract Date articleDate();

		public static Params create(Comment comment, long articleId, Date articleDate) {
			return new AutoValue_CommentActionsDialog_Params(
					comment.getId(),
					comment.getNickname(),
					comment.getDate(),
					comment.getKarma().getCanLike(),
					comment.getKarma().getLikesCount(),
					comment.getContent(),
					comment.canReply(),
					articleId,
					articleDate
			);
		}

	}

	private class LikeCommentLoaderCallbacks implements LoaderManager.LoaderCallbacks<LoadResult<Void>> {

        public static final String COMMENT_ID_BUNDLE_LONG_ARG = "commentId";
        public static final String ARTICLE_ID_BUNDLE_LONG_ARG = "articleId";

        private Context context;
        private FourPdaClient client;

        public LikeCommentLoaderCallbacks(Context context, FourPdaClient client) {
            this.context = context;
            this.client = client;
        }

        @Override
        public Loader<LoadResult<Void>> onCreateLoader(int id, Bundle args) {
            return new LikeCommentLoader(context, client,
                    args.getLong(ARTICLE_ID_BUNDLE_LONG_ARG),
					args.getLong(COMMENT_ID_BUNDLE_LONG_ARG));
        }

        @Override
        public void onLoadFinished(Loader<LoadResult<Void>> loader, LoadResult<Void> result) {

			likeButton.setVisibility(View.VISIBLE);
			likeProgressView.setVisibility(View.INVISIBLE);

			if (result.isError()) {
				Toast.makeText(context, "Ошибка лайка", Toast.LENGTH_SHORT).show();
				return;
			}

			params = new AutoValue_CommentActionsDialog_Params(
					params.id(),
					params.nickname(),
					params.date(),
					Comment.CanLike.ALREADY_LIKED,
					params.likeCount() + 1,
					params.content(),
					params.canReply(),
					params.articleId(),
					params.articleDate());

			updateLikes();

        }

        @Override
        public void onLoaderReset(Loader<LoadResult<Void>> loader) {
        }

    }

}
