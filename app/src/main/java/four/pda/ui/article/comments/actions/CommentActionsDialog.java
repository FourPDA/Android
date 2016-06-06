package four.pda.ui.article.comments.actions;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;

import javax.inject.Inject;

import four.pda.App;
import four.pda.EventBus;
import four.pda.R;
import four.pda.client.FourPdaClient;
import four.pda.client.model.Comment;
import four.pda.ui.article.comments.add.AddCommentEvent;

/**
 * Created by asavinova on 25/04/16.
 */
@EFragment(R.layout.comment_actions_dialog)
public class CommentActionsDialog extends DialogFragment {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy HH:ss");

	@FragmentArg DialogParams params;

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
				Toast.makeText(getContext(), R.string.article_comment_like_already, Toast.LENGTH_SHORT).show();
				break;

			case CAN:

				likeButton.setVisibility(View.INVISIBLE);
				likeProgressView.setVisibility(View.VISIBLE);

				getLoaderManager().initLoader(0, null,
						new LikeCommentLoaderCallbacks(this, getContext(), client)).forceLoad();

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

	void updateLikes() {

		likesCountView.setText(String.valueOf(params.likeCount()));

		likesCheckView.setVisibility(
				params.canLike() == Comment.CanLike.ALREADY_LIKED ?
						View.VISIBLE : View.GONE
		);

		likeButton.setVisibility(params.canLike() == Comment.CanLike.CANT ? View.INVISIBLE : View.VISIBLE);

	}

}
