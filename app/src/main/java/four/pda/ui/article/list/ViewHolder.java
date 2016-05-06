package four.pda.ui.article.list;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import four.pda.EventBus;
import four.pda.EventBus_;
import four.pda.R;
import four.pda.dao.ArticleDao;
import four.pda.ui.ViewUtils;
import four.pda.ui.article.ShowArticleEvent;

/**
 * Created by pavel on 12/04/15.
 */
public class ViewHolder extends RecyclerView.ViewHolder {

	@Bind(R.id.image_view) ImageView imageView;
	@Bind(R.id.title_view) TextView titleView;
	@Bind(R.id.date_view) TextView dateView;

	private long id;
	private Date date;
	private String title;
	private String image;

	private final EventBus eventBus;

	public ViewHolder(View view) {
		super(view);
		eventBus = EventBus_.getInstance_(view.getContext());

		ButterKnife.bind(this, view);

		itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (id > 0) {
					eventBus.post(new ShowArticleEvent(id, date, title, image));
				}
			}
		});
	}

	public void setCursor(Cursor cursor) {

		id = cursor.getLong(ArticleDao.Properties.Id.ordinal);
		date = new Date(cursor.getLong(ArticleDao.Properties.Date.ordinal));
		title = cursor.getString(ArticleDao.Properties.Title.ordinal);
		image = cursor.getString(ArticleDao.Properties.Image.ordinal);

		titleView.setText(title);

		ViewUtils.loadImage(imageView, image);

		String verboseDate = ViewUtils.VERBOSE_DATE_FORMAT.format(date);
		dateView.setText(verboseDate);

	}

}
