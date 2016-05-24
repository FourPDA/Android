package four.pda.ui.article.search;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import four.pda.EventBus_;
import four.pda.R;

/**
 * Created by asavinova on 08/05/16.
 */
public class SearchViewHolder extends RecyclerView.ViewHolder {

	@Bind(R.id.search_view) SearchView searchView;
	@Bind(R.id.find_button) TextView findButton;

	public SearchViewHolder(View view) {
		super(view);

		ButterKnife.bind(this, view);

		findButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String search = searchView.getQuery().toString();
				EventBus_.getInstance_(v.getContext()).post(new SearchArticlesEvent(search));
			}
		});
	}

}
