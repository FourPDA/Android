package four.pda.ui.article;

import java.util.Date;

/**
 * Created by asavinova on 13/04/15.
 */
public class ShowArticleEvent {

	private long id;
	private Date date;
	private String title;
	private String image;

	public ShowArticleEvent(long id, Date date, String title, String image) {
		this.id = id;
		this.date = date;
		this.title = title;
		this.image = image;
	}

	public long getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public String getTitle() {
		return title;
	}

	public String getImage() {
		return image;
	}

}
