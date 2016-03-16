package four.pda.ui.article.comments;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import four.pda.R;

/**
 * Created by asavinova on 16/03/16.
 */
@EFragment(R.layout.fragment_add_comment)
public class AddCommentFragment extends DialogFragment {

	@ViewById Toolbar toolbar;
	@ViewById EditText messageEditText;

	@AfterViews
	void afterViews() {

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
	void cancel() {
		dismiss();
	}

	@Click(R.id.add_comment_button)
	void addComment() {
		dismiss();
	}

}
