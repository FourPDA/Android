package four.pda.client.model;

import java.util.List;

/**
 * Created by asavinova on 05/04/16.
 */
public class CommentsResponse {

	private boolean canComment;
	private List<AbstractComment> comments;

	public boolean canComment() {
		return canComment;
	}

	public void setCanComment(boolean canComment) {
		this.canComment = canComment;
	}

	public List<AbstractComment> getComments() {
		return comments;
	}

	public void setComments(List<AbstractComment> comments) {
		this.comments = comments;
	}

}
