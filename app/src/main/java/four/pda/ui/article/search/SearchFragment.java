package four.pda.ui.article.search;

import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import four.pda.App;
import four.pda.Dao;
import four.pda.EventBus;
import four.pda.R;
import four.pda.client.CategoryType;
import four.pda.client.FourPdaClient;
import four.pda.client.model.SearchContainer;
import four.pda.ui.BaseFragment;
import four.pda.ui.SupportView;

/**
 * Created by asavinova on 07/05/16.
 */
@EFragment(R.layout.search)
public class SearchFragment extends BaseFragment {

	private static final int LOADER_ID = 0;

	@ViewById LinearLayout layout;
	@ViewById Toolbar toolbar;
	@ViewById RecyclerView recyclerView;
	@ViewById View upButton;
	@ViewById SupportView supportView;

	@Bean Dao dao;
	@Bean EventBus eventBus;

	@Inject FourPdaClient client;

	private int currentPage = 0;
	private boolean hasNextPage;
	private String searchCriteria;
	private SearchAdapter adapter;
	private LinearLayoutManager layoutManager;

	@AfterViews
	void afterViews() {
		((App) getActivity().getApplication()).component().inject(this);

		toolbar.setTitle(R.string.category_search);
		showMenuIcon();

		layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
		recyclerView.setLayoutManager(layoutManager);
		adapter = new SearchAdapter(getActivity(), null);
		recyclerView.setAdapter(adapter);

		recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

				int visibleItemCount = layoutManager.getChildCount();
				int totalItemCount = layoutManager.getItemCount();
				int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

				if (!supportView.isLoading() && hasNextPage
						&& (totalItemCount - visibleItemCount) <= firstVisibleItemPosition) {
					loadData();
				}
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
		eventBus.register(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		eventBus.unregister(this);
	}

	@Click
	void upButton() {
		layoutManager.scrollToPosition(0);
	}

	public void onEvent(SearchArticlesEvent event) {
		currentPage = 0;
		searchCriteria = event.getSearchCriteria();
		adapter.swapCursor(null);
		loadData();
	}

	void loadData() {
		if (adapter.isEmpty()) {
			supportView.showProgress();
		}

		getLoaderManager().restartLoader(LOADER_ID, SearchCallbacks.createBundle(searchCriteria, currentPage), new SearchCallbacks(this)).forceLoad();
	}

	void updateData(SearchContainer container) {
		currentPage++;
		hasNextPage = container.hasNextPage();

		Cursor cursor = dao.getArticleCursor(CategoryType.SEARCH);
		adapter.swapCursor(cursor);
		adapter.notifyDataSetChanged();

		supportView.hide();
		upButton.setVisibility(View.VISIBLE);
	}

	void showError() {

		View.OnClickListener retryListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadData();
			}
		};

		if (adapter.isEmpty()) {
			upButton.setVisibility(View.GONE);
			supportView.showError(getString(R.string.article_list_network_error), retryListener);
			return;
		}

		Snackbar
				.make(layout, R.string.article_list_network_error, Snackbar.LENGTH_INDEFINITE)
				.setAction(R.string.retry_button, retryListener)
				.show();

	}

}
