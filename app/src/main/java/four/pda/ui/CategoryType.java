package four.pda.ui;

import four.pda.R;

/**
 * Created by asavinova on 12/04/15.
 */
public enum CategoryType {
	ALL(R.string.category_all, four.pda.client.CategoryType.ALL),
	NEWS(R.string.category_news, four.pda.client.CategoryType.NEWS),
	ARTICLES(R.string.category_articles, four.pda.client.CategoryType.ARTICLES),
	REVIEWS(R.string.category_reviews, four.pda.client.CategoryType.REVIEWS),
	SOFTWARE(R.string.category_software, four.pda.client.CategoryType.SOFTWARE),
	GAMES(R.string.category_games, four.pda.client.CategoryType.GAMES);

	private int title;
	private four.pda.client.CategoryType type;

	CategoryType(int title, four.pda.client.CategoryType type) {
		this.title = title;
		this.type = type;
	}

	public int getTitle() {
		return title;
	}

	public four.pda.client.CategoryType getType() {
		return type;
	}

}
