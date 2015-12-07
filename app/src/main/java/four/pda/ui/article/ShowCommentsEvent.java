package four.pda.ui.article;

/**
 * Created by asavinova on 05/12/15.
 */
public class ShowCommentsEvent {

	private long id;

	public ShowCommentsEvent(long id) {

		this.id = id;
	}

	public long getId() {
		return id;
	}

}
