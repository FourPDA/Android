package four.pda.ui;

import four.pda.R;

/**
 * Created by asavinova on 12/04/15.
 */
public enum CategoryType {
	ALL(R.string.category_all),
	NEWS(R.string.category_news),
	ARTICLES(R.string.category_articles),
	REVIEWS(R.string.category_reviews),
	SOFTWARE(R.string.category_software),
	GAMES(R.string.category_games);

	private int title;

	CategoryType(int title) {
		this.title = title;
	}

	public int getTitle() {
		return title;
	}
}
