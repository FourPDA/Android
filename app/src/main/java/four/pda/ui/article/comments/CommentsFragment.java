package four.pda.ui.article.comments;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import javax.inject.Inject;

import four.pda.App;
import four.pda.Dao;
import four.pda.EventBus;
import four.pda.Preferences_;
import four.pda.R;
import four.pda.client.FourPdaClient;
import four.pda.ui.BaseFragment;
import four.pda.ui.SupportView;
import four.pda.ui.UpdateProfileEvent;
import four.pda.ui.auth.AuthActivity_;

/**
 * Created by asavinova on 05/12/15.
 */
@EFragment(R.layout.comments_list)
public class CommentsFragment extends BaseFragment {

	private static final int LOADER_ID = 0;
	private static final int LOGIN_REQUEST_CODE = 0;

	@FragmentArg long id;

	@ViewById Toolbar toolbar;
	@ViewById SwipeRefreshLayout refresh;
	@ViewById RecyclerView recyclerView;
	@ViewById SupportView supportView;

	@Bean Dao dao;
	@Bean EventBus eventBus;

	@Inject FourPdaClient client;
	@Pref Preferences_ preferences;
	CommentsAdapter adapter;
	private AddCommentEvent addCommentEvent;

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

		if (getView() == null) {
			throw new IllegalStateException("View is NULL");
		}

		adapter = new CommentsAdapter(getActivity());
		adapter.setViewWidth(getView().getWidth());

		getView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
				adapter.setViewWidth(getView().getWidth());
				adapter.notifyDataSetChanged();
			}
		});

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

	void loadData() {
		refresh.setRefreshing(true);
		supportView.showProgress();

		getLoaderManager().restartLoader(LOADER_ID, null, new CommentsCallbacks(this)).forceLoad();
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

	public void onEvent(CommentActionsEvent event) {
		CommentActionsDialog_.builder()
				.comment(event.getComment())
				.build()
				.show(getChildFragmentManager(), "show_comment");
	}

	public void onEvent(AddCommentEvent event) {
		this.addCommentEvent = event;
		boolean isAuthorized = preferences.profileId().get() != 0;

		if (isAuthorized) {
			showAddCommentDialog();
		} else {
			startActivityForResult(new Intent(getActivity(), AuthActivity_.class), LOGIN_REQUEST_CODE);
		}

	}

	@OnActivityResult(LOGIN_REQUEST_CODE)
	void onResult(int resultCode) {
		if (getActivity().RESULT_OK == resultCode) {
			updateProfile();
			showAddCommentDialog();
		}
	}

	@UiThread
	void updateProfile() {
		eventBus.post(new UpdateProfileEvent());
	}

	@UiThread
	void showAddCommentDialog() {
		AddCommentDialog_.builder()
				.replyId(addCommentEvent.getReplyId())
				.replyAuthor(addCommentEvent.getReplyAuthor())
				.build()
				.show(getChildFragmentManager(), "add_comment");
	}

	public void onEvent(UpdateCommentsEvent event) {
		adapter.setCommentsContainer(event.getCommentsContainer());
		adapter.notifyDataSetChanged();
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
