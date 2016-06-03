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
	@Bind(R.id.delimiter_view) View delimiterView;
	@Bind(R.id.nick_view) TextView nickView;
	@Bind(R.id.date_view) TextView dateView;
	@Bind(R.id.likes_view) TextView likesView;
	@Bind(R.id.content_view) TextView contentView;

	public CommentViewHolder(final View view) {
		super(view);
		ButterKnife.bind(this, view);
	}

	public void setComment(final Comment comment) {

		nickView.setText(comment.getNickname());

		String verboseDate = DATE_FORMAT.format(comment.getDate());
		dateView.setText(verboseDate);

		int likes = comment.getKarma().getLikes();
		likesView.setText(String.valueOf(likes));

		contentView.setText(Html.fromHtml(comment.getContent()));

		itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EventBus_.getInstance_(v.getContext())
						.post(new CommentActionsEvent(comment));
			}
		});

	}

}
