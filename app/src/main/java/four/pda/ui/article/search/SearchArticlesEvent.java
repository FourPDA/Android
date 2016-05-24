package four.pda.ui.article.search;

/**
 * Created by asavinova on 08/05/16.
 */
public class SearchArticlesEvent {

	private String search;

	public SearchArticlesEvent(String search) {
		this.search = search;
	}

	public String getSearch() {
		return search;
	}

}
