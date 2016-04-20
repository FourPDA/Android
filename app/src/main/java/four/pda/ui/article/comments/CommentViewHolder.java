package four.pda.ui.article.comments;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import four.pda.EventBus_;
import four.pda.R;
import four.pda.client.model.AbstractComment;
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

	private Integer leftPadding;
	private Integer level;

	public CommentViewHolder(final View view) {
		super(view);
		ButterKnife.bind(this, view);

		view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				updatePaddings();
			}
		});
	}

	public void setComment(final AbstractComment abstractComment) {

		boolean isNormalComment = abstractComment instanceof Comment;

		if (isNormalComment) {

			final Comment comment = (Comment) abstractComment;

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

			contentView.setTextColor(itemView.getResources().getColor(R.color.material_grey_600));
			contentView.setTypeface(null, Typeface.NORMAL);

		} else {
			contentView.setTextColor(itemView.getResources().getColor(R.color.primary_text_disabled_material_light));
			contentView.setTypeface(null, Typeface.ITALIC);
		}

		contentView.setText(Html.fromHtml(abstractComment.getContent()));

		authorInfoView.setVisibility(isNormalComment ? View.VISIBLE : View.GONE);
		delimeterView.setVisibility(isNormalComment ? View.VISIBLE : View.GONE);
		replyButton.setVisibility(abstractComment.canReply() ? View.VISIBLE : View.GONE);

		level = abstractComment.getLevel();
		updatePaddings();

	}

	private void updatePaddings() {

		if (itemView.getWidth() > 0) {
			leftPadding = itemView.getWidth() / 30;
		}

		if (leftPadding == null) {
			return;
		}

		if (level == null) {
			return;
		}

		int left = leftPadding * level;

		itemView.setPadding(left, 0, 0, 0);

	}

}
