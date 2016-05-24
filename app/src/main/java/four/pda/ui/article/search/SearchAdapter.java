package four.pda.ui.article.search;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import four.pda.R;
import four.pda.ui.CursorRecyclerViewAdapter;

/**
 * Created by asavinova on 07/05/16.
 */
public class SearchAdapter extends CursorRecyclerViewAdapter<RecyclerView.ViewHolder> {

	enum Type {
		SEARCH,
		ARTICLE
	}

	private LayoutInflater inflater;

	public SearchAdapter(Context context, Cursor cursor) {
		super(context, cursor);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == Type.SEARCH.ordinal()) {
			View view = inflater.inflate(R.layout.search_view_holder, parent, false);
			return new SearchViewHolder(view);
		}

		View view = inflater.inflate(R.layout.search_list_item, parent, false);
		return new ItemViewHolder(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		if (Type.SEARCH.ordinal() == getItemViewType(position)) {
			return;
		}

		super.onBindViewHolder(viewHolder, position - 1);
	}

	@Override
	public int getItemCount() {
		return super.getItemCount() + 1;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return Type.SEARCH.ordinal();
		}

		return Type.ARTICLE.ordinal();
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {
		((ItemViewHolder) viewHolder).setCursor(cursor);
	}
}
