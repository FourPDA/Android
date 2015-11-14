package four.pda.client.model;

import java.util.List;

/**
 * Created by asavinova on 07/11/15.
 */
public abstract class AbstractComment {

	private long id;
	private String content;
	private int level;
	private List<AbstractComment> children;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public List<AbstractComment> getChildren() {
		return children;
	}

	public void setChildren(List<AbstractComment> children) {
		this.children = children;
	}

}
