package four.pda.ui.article.comments;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import four.pda.R;
import four.pda.client.model.AbstractComment;
import four.pda.client.model.Comment;

/**
 * Created by asavinova on 05/12/15.
 */
public class ViewHolder extends RecyclerView.ViewHolder {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy HH:ss");

	@Bind(R.id.author_info_view) View authorInfoView;
	@Bind(R.id.nick_view) TextView nickView;
	@Bind(R.id.date_view) TextView dateView;
	@Bind(R.id.content_view) TextView contentView;

	public ViewHolder(View view) {
		super(view);
		ButterKnife.bind(this, view);
	}

	public void setComment(AbstractComment abstractComment) {

		if (abstractComment instanceof Comment) {
			Comment comment = (Comment) abstractComment;

			authorInfoView.setVisibility(View.VISIBLE);

			nickView.setText(comment.getNickname());

			String verboseDate = DATE_FORMAT.format(comment.getDate());
			dateView.setText(verboseDate);
		} else {
			authorInfoView.setVisibility(View.GONE);
		}

		contentView.setText(Html.fromHtml(abstractComment.getContent()));

		int padding = itemView.getResources().getDimensionPixelSize(R.dimen.offset_normal);
		itemView.setPadding(padding * (abstractComment.getLevel() + 1), padding, padding, padding);

	}

}
