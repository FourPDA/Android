package four.pda.ui.article.list;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import four.pda.App;
import four.pda.Dao;
import four.pda.R;
import four.pda.analytics.Analytics;
import four.pda.client.CategoryType;
import four.pda.client.FourPdaClient;
import four.pda.ui.BaseFragment;
import four.pda.ui.CategoryTitleMap;
import four.pda.ui.SupportView;

/**
 * Created by asavinova on 10/04/15.
 */
@EFragment(R.layout.article_list)
public class ListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

	private static final int LOADER_ID = 0;

	@FragmentArg CategoryType category;

	@ViewById LinearLayout layout;
	@ViewById Toolbar toolbar;
	@ViewById SwipeRefreshLayout refresh;
	@ViewById RecyclerView recyclerView;
	@ViewById View upButton;
	@ViewById SupportView supportView;

	@Bean Dao dao;
	@Bean Analytics analytics;

	@Inject FourPdaClient client;

	int page = 1;
	ArticlesAdapter adapter;

	private GridLayoutManager layoutManager;

	@AfterViews
	void afterViews() {
		((App) getActivity().getApplication()).component().inject(this);

		toolbar.setTitle(CategoryTitleMap.get(category));
		showMenuIcon();

		layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				int spanCount = (int) (layout.getWidth() / layout.getResources().getDimension(R.dimen.list_item_width));
				if (spanCount > 1) {
					layoutManager.setSpanCount(spanCount);
				}
			}
		});

		layoutManager = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
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

	@Click
	void upButton() {
		analytics.articlesList().scrollUp(layoutManager.findFirstVisibleItemPosition());
		layoutManager.scrollToPosition(0);
	}

	void loadData(boolean force) {
		refresh.setRefreshing(true);

		int itemCount = adapter.getItemCount();
		if (itemCount == 0) {
			supportView.showProgress();
		}

		Bundle bundle = new Bundle();
		bundle.putBoolean(ListCallbacks.FORCE_BUNDLE_ARG, force);
		getLoaderManager().restartLoader(LOADER_ID, bundle, new ListCallbacks(this)).forceLoad();
	}

	@Override
	public void onRefresh() {
		loadData(true);
	}

	public CategoryType getCategory() {
		return category;
	}

}
