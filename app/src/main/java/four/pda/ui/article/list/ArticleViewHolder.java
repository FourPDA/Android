package four.pda.ui.article.list;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import four.pda.DateFormats;
import four.pda.EventBus;
import four.pda.EventBus_;
import four.pda.R;
import four.pda.dao.ArticleDao;
import four.pda.ui.Images;
import four.pda.ui.article.ShowArticleEvent;
import four.pda.ui.profile.ProfileActivity_;

/**
 * Created by pavel on 12/04/15.
 */
public class ArticleViewHolder extends RecyclerView.ViewHolder {

	@BindView(R.id.image_view) ImageView imageView;
	@BindView(R.id.title_view) TextView titleView;
	@BindView(R.id.date_view) TextView dateView;
	@BindView(R.id.author_view) TextView authorView;
	@BindView(R.id.comments_count_text_view) TextView commentsCountView;

	private long id;
	private Date date;
	private String title;
	private String image;
	private int commentsCount;
	private long authorId;
	private String authorName;

	private final EventBus eventBus;

	public ArticleViewHolder(View view) {
		super(view);
		ButterKnife.bind(this, view);

		eventBus = EventBus_.getInstance_(view.getContext());

		itemView.setOnClickListener(v -> {
			if (id > 0) {
				eventBus.post(new ShowArticleEvent(id, date, title, image, authorId, authorName));
			}
		});

		authorView.setOnClickListener(v -> {
			if (authorId > 0) {
				ProfileActivity_.intent(v.getContext())
						.profileId(authorId)
						.start();
			}
		});
	}

	public void setCursor(Cursor cursor) {

		id = cursor.getLong(ArticleDao.Properties.Id.ordinal);
		date = new Date(cursor.getLong(ArticleDao.Properties.Date.ordinal));
		title = cursor.getString(ArticleDao.Properties.Title.ordinal);
		image = cursor.getString(ArticleDao.Properties.Image.ordinal);
		commentsCount = cursor.getInt(ArticleDao.Properties.CommentsCount.ordinal);
		authorId = cursor.getLong(ArticleDao.Properties.AuthorId.ordinal);
		authorName = cursor.getString(ArticleDao.Properties.AuthorName.ordinal);

		titleView.setText(title);

		Images.load(imageView, image);

		String verboseDate = DateFormats.VERBOSE.format(date);
		dateView.setText(verboseDate);

		authorView.setText(authorName);

		commentsCountView.setText(String.valueOf(commentsCount));

	}

}
