package four.pda.ui.article.search;

import android.database.Cursor;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import four.pda.R;
import four.pda.dao.ArticleDao;
import four.pda.ui.article.list.ArticleViewHolder;

/**
 * Created by asavinova on 08/05/16.
 */
public class ItemViewHolder extends ArticleViewHolder {

	@Bind(R.id.description_view) TextView descriptionView;

	public ItemViewHolder(View view) {
		super(view);
	}

	public void setCursor(Cursor cursor) {
		super.setCursor(cursor);

		String description = cursor.getString(ArticleDao.Properties.Description.ordinal);
		descriptionView.setText(Html.fromHtml(description));
	}

}
