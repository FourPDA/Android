package four.pda.ui.article.comments;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import four.pda.EventBus_;
import four.pda.R;

/**
 * Created by asavinova on 10/03/16.
 */
public class AddCommentViewHolder extends RecyclerView.ViewHolder {

	@Bind(R.id.add_comment_button) View addCommentButton;

	public AddCommentViewHolder(View view) {
		super(view);
		ButterKnife.bind(this, view);

		addCommentButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EventBus_.getInstance_(v.getContext())
						.post(new AddCommentEvent());
			}
		});
	}

}
