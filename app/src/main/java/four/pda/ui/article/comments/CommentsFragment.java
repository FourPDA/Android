package four.pda.ui.article.comments;

import android.graphics.Rect;
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

import java.util.List;

import javax.inject.Inject;

import four.pda.App;
import four.pda.Dao;
import four.pda.R;
import four.pda.client.FourPdaClient;
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
	@Inject FourPdaClient client;

	private CommentsAdapter adapter;

	@AfterViews
	void afterViews() {
		((App) getActivity().getApplication()).component().inject(this);

		toolbar.setTitle(R.string.comments_title);
		toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
			}
		});

		adapter = new CommentsAdapter(getActivity());

		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.addItemDecoration(new SpaceDecorator(getResources().getDimensionPixelOffset(R.dimen.offset_normal)));

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
						return new LoadResult<>(client.getArticleComments(article.getDate(), article.getId()));
					} catch (Exception e) {
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
				return;
			}

			supportView.showError(getString(R.string.comments_network_error), new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					loadData();
				}
			});
		}

		@Override
		public void onLoaderReset(Loader<LoadResult<List<AbstractComment>>> loader) {
		}

	}

	/**
	 * http://stackoverflow.com/questions/24618829
	 */
	private class SpaceDecorator extends RecyclerView.ItemDecoration {

		private final int verticalSpace;

		public SpaceDecorator(int verticalSpace) {
			this.verticalSpace = verticalSpace;
		}

		@Override
		public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
								   RecyclerView.State state) {

			outRect.bottom = verticalSpace;

			// Last element has no margin
			if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
				outRect.bottom = 0;
			}

		}

	}

}
