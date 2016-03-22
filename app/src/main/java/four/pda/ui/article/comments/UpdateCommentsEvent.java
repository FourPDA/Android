package four.pda.ui.article.comments;

import java.util.List;

import four.pda.client.model.AbstractComment;

/**
 * Created by asavinova on 16/03/16.
 */
public class UpdateCommentsEvent {

	private List<AbstractComment> comments;

	public UpdateCommentsEvent(List<AbstractComment> comments) {
		this.comments = comments;
	}

	public List<AbstractComment> getComments() {
		return comments;
	}

}
