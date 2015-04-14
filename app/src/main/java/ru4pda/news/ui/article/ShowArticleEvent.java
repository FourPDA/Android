package ru4pda.news.ui.article;

/**
 * Created by asavinova on 13/04/15.
 */
public class ShowArticleEvent {

	private long id;

	public ShowArticleEvent(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}
}
