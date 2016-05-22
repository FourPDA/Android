package four.pda.ui.article.comments;

import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;

import four.pda.EventBus;
import four.pda.R;
import four.pda.client.model.Comment;

/**
 * Created by asavinova on 25/04/16.
 */
@EFragment(R.layout.comment_actions_dialog)
public class CommentActionsDialog extends DialogFragment {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy HH:ss");

	@FragmentArg Comment comment;

	@ViewById Toolbar toolbar;
	@ViewById TextView nickView;
	@ViewById TextView dateView;
	@ViewById TextView contentView;
	@ViewById TextView replyButton;

	@Bean EventBus eventBus;

	@AfterViews
	void afterViews() {

		toolbar.setTitle(R.string.show_comment_dialog_title);
		toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CommentActionsDialog.this.dismiss();
			}
		});

		nickView.setText(comment.getNickname());

		String verboseDate = DATE_FORMAT.format(comment.getDate());
		dateView.setText(verboseDate);

		contentView.setText(Html.fromHtml(comment.getContent()));

		replyButton.setVisibility(comment.canReply() ? View.VISIBLE : View.GONE);

	}

	@Click(R.id.reply_button)
	void addCommentClicked() {
		eventBus.post(new AddCommentEvent(comment.getId(), comment.getNickname()));
		dismiss();
	}

}