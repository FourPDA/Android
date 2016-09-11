package four.pda.client.model;

import java.util.List;

/**
 * Created by asavinova on 01/09/16.
 */
public class ArticleContent {

	private String content;
	private List<String> images;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

}
