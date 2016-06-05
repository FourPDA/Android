package four.pda.ui.article.comments;

import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

		likesCountView.setText(String.valueOf(params.likeCount()));

		Comment.CanLike canLike = Comment.CanLike.fromJsValue(params.canLike());

		likesCheckView.setVisibility(
				canLike == Comment.CanLike.ALREADY_LIKED ?
				View.VISIBLE : View.GONE
		);

		likeButton.setVisibility(canLike == Comment.CanLike.CANT ? View.INVISIBLE : View.VISIBLE);

		contentView.setText(Html.fromHtml(params.content()));

		replyButton.setVisibility(params.canReply() ? View.VISIBLE : View.GONE);

	}

	@Click(R.id.share_button)
	void share() {
		startActivity(ShareCompat.IntentBuilder.from(getActivity())
				.setType("text/plain")
				.setText(client.getCommentUrl(params.articleId(), params.articleDate(), params.id()))
				.createChooserIntent());
		dismiss();
	}

	@Click(R.id.reply_button)
	void reply() {
		eventBus.post(new AddCommentEvent(params.id(), params.nickname()));
		dismiss();
	}

	@AutoValue
	abstract static class Params implements Parcelable {

		abstract long id();
		abstract String nickname();
		abstract Date date();

		/**
		 * @see four.pda.client.model.Comment.CanLike#fromJsValue(int)
		 * @return {@link four.pda.client.model.Comment.CanLike#jsValue}
         */
		abstract int canLike();

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
					comment.getKarma().getCanLike().jsValue(),
					comment.getKarma().getLikesCount(),
					comment.getContent(),
					comment.canReply(),
					articleId,
					articleDate
			);
		}

	}

}
