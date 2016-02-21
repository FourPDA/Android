package four.pda.client.model;

import java.util.Date;

/**
 * Created by asavinova on 02/11/15.
 */
public class Comment extends AbstractComment {

	private String nickname;
	private Date date;
	private int likes;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

}
