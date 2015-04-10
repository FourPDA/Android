package ru4pda.news.ui.list;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.List;

import ru4pda.news.Dao;
import ru4pda.news.R;
import ru4pda.news.client.Ru4pdaClient;
import ru4pda.news.client.model.SimpleArticle;

/**
 * Created by asavinova on 10/04/15.
 */
@EFragment(R.layout.fragment_list_articles)
public class ListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

	private static final int LOADER_ID = 0;
	private static final String FORCE = "force";

	@ViewById SwipeRefreshLayout refresh;
	@ViewById RecyclerView recyclerView;

	@Bean Dao dao;
	@Bean Ru4pdaClient client;

	private int page = 1;

	private ArticlesAdapter adapter;

	@AfterViews
	void afterViews() {

		final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(layoutManager);
		adapter = new ArticlesAdapter(getActivity(), null);
		recyclerView.setAdapter(adapter);

		recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

				int visibleItemCount = layoutManager.getChildCount();
				int totalItemCount = layoutManager.getItemCount();
				int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

				if (!refresh.isRefreshing()
						&& (totalItemCount - visibleItemCount) <= firstVisibleItemPosition) {

					loadData(false);
				}
			}
		});

		refresh.setOnRefreshListener(this);
		refresh.setColorSchemeResources(R.color.primary);
		refresh.setProgressViewOffset(false, 0,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

		loadData(false);
	}

	private void loadData(boolean force) {
		refresh.setRefreshing(true);

		Bundle bundle = new Bundle();
		bundle.putBoolean(FORCE, force);
		getLoaderManager().restartLoader(LOADER_ID, bundle, new Callbacks());
	}

	@Override
	public void onRefresh() {
		loadData(true);
	}


	class Callbacks implements LoaderManager.LoaderCallbacks<Cursor> {

		@Override
		public Loader<Cursor> onCreateLoader(int id, final Bundle args) {
			return new CursorLoader(getActivity()) {
				@Override
				public Cursor loadInBackground() {
					boolean force = args.getBoolean(FORCE);
					if (force) {
						page = 1;
					}

					try {
						List<SimpleArticle> articles = client.getArticles(page);

						boolean needClearData = page == 1;
						page++;
						dao.setArticles(articles, needClearData);

						return dao.getArticleCursor();
					} catch (IOException e) {
						e.printStackTrace();
					}
					return null;
				}
			};
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			adapter.swapCursor(cursor);
			adapter.notifyDataSetChanged();
			refresh.setRefreshing(false);
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
		}
	}
}
