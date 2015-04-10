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

import java.text.SimpleDateFormat;
import java.util.Date;

import ru4pda.news.R;
import ru4pda.news.dao.ArticleDao;
import ru4pda.news.ui.CursorRecyclerViewAdapter;
import ru4pda.news.ui.ViewUtils;

/**
 * Created by asavinova on 10/04/15.
 */
public class ArticlesAdapter extends CursorRecyclerViewAdapter<ArticlesAdapter.ViewHolder> {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat VERBOSE_DATE_FORMAT = new SimpleDateFormat("dd MMMM yyyy");

	public ArticlesAdapter(Context context, Cursor cursor) {
		super(context, cursor);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public ImageView imageView;
		public TextView titleView;
		public TextView dateView;
		public TextView descriptionView;

		public ViewHolder(View view) {
			super(view);
			imageView = (ImageView) view.findViewById(R.id.image_view);
			titleView = (TextView) view.findViewById(R.id.title_view);
			dateView = (TextView) view.findViewById(R.id.date_view);
			descriptionView = (TextView) view.findViewById(R.id.description_view);
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
		long id = cursor.getLong(ArticleDao.Properties.Id.ordinal);
		ViewUtils.loadImage(viewHolder.imageView, id);

		String title = cursor.getString(ArticleDao.Properties.Title.ordinal);
		viewHolder.titleView.setText(title);

		String verboseDate = "";
			Date date = new Date(cursor.getLong(ArticleDao.Properties.Date.ordinal));
			verboseDate = VERBOSE_DATE_FORMAT.format(date);
			viewHolder.dateView.setText(verboseDate);

		String description = cursor.getString(ArticleDao.Properties.Description.ordinal);
		viewHolder.descriptionView.setText(Html.fromHtml(description));
	}
}
