package four.pda.ui.article.comments;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import four.pda.R;
import four.pda.client.model.AbstractComment;
import four.pda.client.model.Comment;
import four.pda.ui.ViewUtils;

/**
 * Created by asavinova on 05/12/15.
 */
public class ViewHolder extends RecyclerView.ViewHolder {

	@Bind(R.id.author_info_view) View authorInfoView;
	@Bind(R.id.nick_view) TextView nickView;
	@Bind(R.id.date_view) TextView dateView;
	@Bind(R.id.content_view) TextView contentView;

	public ViewHolder(View view) {
		super(view);
		ButterKnife.bind(this, view);
	}

	public void setComment(AbstractComment comment) {

		if (comment instanceof Comment) {
			authorInfoView.setVisibility(View.VISIBLE);

			nickView.setText(((Comment) comment).getNickname());

			String verboseDate = ViewUtils.VERBOSE_COMMENT_DATE_FORMAT.format(((Comment) comment).getDate());
			dateView.setText(verboseDate);
		} else {
			authorInfoView.setVisibility(View.GONE);
		}

		contentView.setText(Html.fromHtml(comment.getContent()));

		int padding = itemView.getResources().getDimensionPixelSize(R.dimen.offset_normal);
		itemView.setPadding(padding * (comment.getLevel() + 1), padding, padding, padding);

	}

}
