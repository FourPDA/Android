package ru4pda.news.ui.article.list;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ru4pda.news.EventBus_;
import ru4pda.news.R;
import ru4pda.news.dao.ArticleDao;
import ru4pda.news.ui.ViewUtils;
import ru4pda.news.ui.article.ShowArticleEvent;

/**
 * Created by pavel on 12/04/15.
 */
public class ViewHolder extends RecyclerView.ViewHolder {

	@InjectView(R.id.image_view) ImageView imageView;
	@InjectView(R.id.title_view) TextView titleView;
	@InjectView(R.id.date_view) TextView dateView;

	public ViewHolder(View view) {
		super(view);
		ButterKnife.inject(this, view);
	}

	public void setCursor(Cursor cursor) {

		final long id = cursor.getLong(ArticleDao.Properties.ServerId.ordinal);
		final String title = cursor.getString(ArticleDao.Properties.Title.ordinal);
		Date date = new Date(cursor.getLong(ArticleDao.Properties.Date.ordinal));
		String image = cursor.getString(ArticleDao.Properties.Image.ordinal);

		itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EventBus_.getInstance_(v.getContext())
						.post(new ShowArticleEvent(id));
			}
		});

		String verboseDate = ViewUtils.VERBOSE_DATE_FORMAT.format(date);

		ViewUtils.loadImage(imageView, image);
		titleView.setText(title);
		dateView.setText(verboseDate);

	}

}
