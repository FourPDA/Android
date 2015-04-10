package ru4pda.news.ui.list;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import ru4pda.news.R;
import ru4pda.news.dao.ArticleDao;
import ru4pda.news.ui.CursorRecyclerViewAdapter;
import ru4pda.news.ui.ViewUtils;
import ru4pda.news.ui.item.ArticleActivity_;

/**
 * Created by asavinova on 10/04/15.
 */
public class ArticlesAdapter extends CursorRecyclerViewAdapter<ArticlesAdapter.ViewHolder> {

	public ArticlesAdapter(Context context, Cursor cursor) {
		super(context, cursor);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		public ImageView imageView;
		public TextView titleView;
		public TextView dateView;
		public TextView descriptionView;
		private long id;
		private String title;

		public ViewHolder(View view) {
			super(view);
			view.setOnClickListener(this);

			imageView = (ImageView) view.findViewById(R.id.image_view);
			titleView = (TextView) view.findViewById(R.id.title_view);
			dateView = (TextView) view.findViewById(R.id.date_view);
			descriptionView = (TextView) view.findViewById(R.id.description_view);
		}

		@Override
		public void onClick(View v) {
			ArticleActivity_.intent(v.getContext())
					.id(id)
					.title(title)
					.start();
		}

		public void setCursor(Cursor cursor) {
			id = cursor.getLong(ArticleDao.Properties.Id.ordinal);
			ViewUtils.loadImage(imageView, id);

			title = cursor.getString(ArticleDao.Properties.Title.ordinal);
			titleView.setText(title);

			String verboseDate = "";
			Date date = new Date(cursor.getLong(ArticleDao.Properties.Date.ordinal));
			verboseDate = ViewUtils.VERBOSE_DATE_FORMAT.format(date);
			dateView.setText(verboseDate);

			String description = cursor.getString(ArticleDao.Properties.Description.ordinal);
			descriptionView.setText(Html.fromHtml(description));
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.article_list_view, parent, false);
		ViewHolder viewHolder = new ViewHolder(itemView);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
		viewHolder.setCursor(cursor);
	}
}
