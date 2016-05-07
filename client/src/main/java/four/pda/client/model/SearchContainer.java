package four.pda.client.model;

import java.util.List;

/**
 * Created by asavinova on 07/05/16.
 */
public class SearchContainer {

	private int count;
	private List<ListArticle> articles;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<ListArticle> getArticles() {
		return articles;
	}

	public void setArticles(List<ListArticle> articles) {
		this.articles = articles;
	}

}
