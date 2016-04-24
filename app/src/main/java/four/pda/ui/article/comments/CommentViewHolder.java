package four.pda.ui.article.comments;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import four.pda.EventBus_;
import four.pda.R;
import four.pda.client.model.Comment;

/**
 * Created by asavinova on 05/12/15.
 */
public class CommentViewHolder extends RecyclerView.ViewHolder {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy HH:ss");

	@Bind(R.id.author_info_view) View authorInfoView;
	@Bind(R.id.delimeter_view) View delimeterView;
	@Bind(R.id.nick_view) TextView nickView;
	@Bind(R.id.date_view) TextView dateView;
	@Bind(R.id.content_view) TextView contentView;
	@Bind(R.id.reply_button) TextView replyButton;

	public CommentViewHolder(final View view) {
		super(view);
		ButterKnife.bind(this, view);
	}

	public void setComment(final Comment comment) {

		nickView.setText(comment.getNickname());

		String verboseDate = DATE_FORMAT.format(comment.getDate());
		dateView.setText(verboseDate);

		replyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EventBus_.getInstance_(v.getContext())
						.post(new AddCommentEvent(comment.getId(), comment.getNickname()));
			}
		});

		contentView.setText(Html.fromHtml(comment.getContent()));
		replyButton.setVisibility(comment.canReply() ? View.VISIBLE : View.GONE);

	}

}
