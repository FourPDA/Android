package four.pda.client.model;

import java.util.List;

/**
 * Created by asavinova on 07/05/16.
 */
public class SearchContainer {

	private static final int ARTICLES_PER_PAGE = 30;

	private int allArticlesCount;
	private int currentPage;
	private List<ListArticle> articles;

	public int getAllArticlesCount() {
		return allArticlesCount;
	}

	public void setAllArticlesCount(int allArticlesCount) {
		this.allArticlesCount = allArticlesCount;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public List<ListArticle> getArticles() {
		return articles;
	}

	public void setArticles(List<ListArticle> articles) {
		this.articles = articles;
	}

	public boolean hasNextPage() {
		return currentPage * ARTICLES_PER_PAGE < allArticlesCount;
	}

}
