package ru4pda.news.ui.list;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru4pda.news.R;
import ru4pda.news.dao.ArticleDao;
import ru4pda.news.ui.CursorRecyclerViewAdapter;

/**
 * Created by asavinova on 10/04/15.
 */
public class ArticlesAdapter extends CursorRecyclerViewAdapter<ArticlesAdapter.ViewHolder> {

	public ArticlesAdapter(Context context, Cursor cursor) {
		super(context, cursor);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public TextView titleView;
		public TextView descriptionView;

		public ViewHolder(View view) {
			super(view);
			titleView = (TextView) view.findViewById(R.id.title_view);
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

		String title = cursor.getString(ArticleDao.Properties.Title.ordinal);
		viewHolder.titleView.setText(title);

		String description = cursor.getString(ArticleDao.Properties.Description.ordinal);
		viewHolder.descriptionView.setText(description);
	}
}
