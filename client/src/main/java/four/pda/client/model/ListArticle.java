package four.pda.client.model;

import java.util.Date;

/**
 * Created by asavinova on 09/04/15.
 */
public class ListArticle extends AbstractArticle {

	private Date publishedDate;

	public Date getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}

}
