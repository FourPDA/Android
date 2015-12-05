package four.pda.ui.article.comments;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import four.pda.R;
import four.pda.ui.BaseFragment;

/**
 * Created by asavinova on 05/12/15.
 */
@EFragment(R.layout.comments_list)
public class CommentsFragment extends BaseFragment {

	@FragmentArg long id;

	@ViewById Toolbar toolbar;
	@ViewById TextView textView;

	@AfterViews
	void afterViews() {
		textView.setText(String.format("Comments id = %d", id));

		toolbar.setTitle(R.string.comments_title);
		toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
			}
		});

	}

}
