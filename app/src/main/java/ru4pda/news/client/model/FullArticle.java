package ru4pda.news.client.model;

/**
 * Created by asavinova on 10/04/15.
 */
public class FullArticle {

	private SimpleArticle simpleArticle;
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public SimpleArticle getSimpleArticle() {
		return simpleArticle;
	}

	public void setSimpleArticle(SimpleArticle simpleArticle) {
		this.simpleArticle = simpleArticle;
	}
}
