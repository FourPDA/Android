package ru4pda.news.ui.list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.List;

import ru4pda.news.R;
import ru4pda.news.Dao;
import ru4pda.news.client.Ru4pdaClient;
import ru4pda.news.client.model.SimpleArticle;

/**
 * Created by asavinova on 10/04/15.
 */
@EFragment(R.layout.fragment_list_articles)
public class ListFragment extends Fragment {

	private static final int LOADER_ID = 0;

	@ViewById SwipeRefreshLayout refresh;
	@ViewById RecyclerView recyclerView;

	@Bean Dao dao;
	@Bean Ru4pdaClient client;

	private ArticlesAdapter adapter;

	@AfterViews
	void afterViews() {

		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		adapter = new ArticlesAdapter(getActivity(), dao.getArticleCursor());
		recyclerView.setAdapter(adapter);

		refresh.setEnabled(false);

		getLoaderManager().initLoader(LOADER_ID, null, new Callbacks());
	}


	class Callbacks implements LoaderManager.LoaderCallbacks<List<SimpleArticle>> {

		@Override
		public Loader<List<SimpleArticle>> onCreateLoader(int id, final Bundle args) {
			return new AsyncTaskLoader<List<SimpleArticle>>(getActivity()) {
				@Override
				protected void onStartLoading() {
					super.onStartLoading();
					forceLoad();
				}

				@Override
				public List<SimpleArticle> loadInBackground() {
					try {
						return client.getArticles(1);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return null;
				}
			};
		}

		@Override
		public void onLoadFinished(Loader<List<SimpleArticle>> loader, List<SimpleArticle> data) {
			if (data == null) {
				return;
			}
			dao.setArticles(data);
			adapter.notifyDataSetChanged();
		}

		@Override
		public void onLoaderReset(Loader<List<SimpleArticle>> loader) {
		}

	}
}
