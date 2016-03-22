package four.pda.ui.article.comments;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

import four.pda.App;
import four.pda.EventBus;
import four.pda.R;
import four.pda.client.FourPdaClient;
import four.pda.client.model.AbstractComment;
import four.pda.ui.SupportView;

/**
 * Created by asavinova on 16/03/16.
 */
@EFragment(R.layout.fragment_add_comment)
public class AddCommentFragment extends DialogFragment {

	private static final int ADD_COMMENT_LOADER_ID = 0;

	@FragmentArg Long replyId;
	@FragmentArg String replyAuthor;

	@ViewById Toolbar toolbar;
	@ViewById EditText messageEditText;
	@ViewById SupportView supportView;

	@Inject FourPdaClient client;
	@Bean EventBus eventBus;

	@AfterViews
	void afterViews() {
		((App) getActivity().getApplication()).component().inject(this);

		toolbar.setTitle(R.string.add_comment_dialog_title);
		toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AddCommentFragment.this.dismiss();
			}
		});

	}

	@Override
	public void onStart() {
		super.onStart();

		Dialog dialog = getDialog();
		if (dialog != null) {
			// Максимально растягивает окно диалога
			dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		}
	}

	@Click(R.id.cancel_button)
	void cancelClicked() {
		dismiss();
	}

	@Click(R.id.add_comment_button)
	void addCommentClicked() {
		String message = messageEditText.getText().toString();
		if (TextUtils.isEmpty(message)) {
			messageEditText.setError("Empty!");
		} else {
			addComment();
			dismiss();
		}
	}

	void addComment() {
		supportView.showProgress();
		getLoaderManager().restartLoader(ADD_COMMENT_LOADER_ID, null, new AddCommentCallbacks(this)).forceLoad();
	}

	void updateComments(List<AbstractComment> comments) {
		eventBus.post(new UpdateCommentsEvent(comments));
	}
}
