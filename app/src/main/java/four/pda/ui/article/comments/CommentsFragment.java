package four.pda.ui.article.comments;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.List;

import four.pda.Dao;
import four.pda.FourPdaClient;
import four.pda.R;
import four.pda.client.model.AbstractComment;
import four.pda.dao.Article;
import four.pda.ui.BaseFragment;
import four.pda.ui.LoadResult;
import four.pda.ui.SupportView;

/**
 * Created by asavinova on 05/12/15.
 */
@EFragment(R.layout.comments_list)
public class CommentsFragment extends BaseFragment {

	private static final int LOADER_ID = 0;

	@FragmentArg long id;

	@ViewById Toolbar toolbar;
	@ViewById SwipeRefreshLayout refresh;
	@ViewById RecyclerView recyclerView;
	@ViewById SupportView supportView;

	@Bean Dao dao;
	@Bean FourPdaClient client;
	private CommentsAdapter adapter;

	@AfterViews
	void afterViews() {

		toolbar.setTitle(R.string.comments_title);
		toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
			}
		});

		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		adapter = new CommentsAdapter(getActivity());
		recyclerView.setAdapter(adapter);

		refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				loadData();
			}
		});
		refresh.setColorSchemeResources(R.color.primary);
		refresh.setProgressViewOffset(false, 0,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

		loadData();
	}

	private void loadData() {
		refresh.setRefreshing(true);
		supportView.showProgress();

		getLoaderManager().restartLoader(LOADER_ID, null, new Callbacks()).forceLoad();
	}

	class Callbacks implements LoaderManager.LoaderCallbacks<LoadResult<List<AbstractComment>>> {

		@Override
		public Loader<LoadResult<List<AbstractComment>>> onCreateLoader(final int id, Bundle args) {
			return new AsyncTaskLoader<LoadResult<List<AbstractComment>>>(getActivity()) {
				@Override
				public LoadResult<List<AbstractComment>> loadInBackground() {
					Article article = dao.getArticle(CommentsFragment.this.id);
					try {
						return new LoadResult<>(client.getInstance().getArticleComments(article.getDate(), article.getServerId()));
					} catch (IOException e) {
						L.error("Article comments request error", e);
						return new LoadResult<>(e);
					}
				}
			};
		}

		@Override
		public void onLoadFinished(Loader<LoadResult<List<AbstractComment>>> loader, LoadResult<List<AbstractComment>> result) {
			refresh.setRefreshing(false);

			if (result.getException() == null) {
				adapter.setComments(result.getData());
				adapter.notifyDataSetChanged();
				supportView.hide();
			} else {
				supportView.showError(getString(R.string.comments_network_error), new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						loadData();
					}
				});
			}
		}

		@Override
		public void onLoaderReset(Loader<LoadResult<List<AbstractComment>>> loader) {
		}

	}

}
