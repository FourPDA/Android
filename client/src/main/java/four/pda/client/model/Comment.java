package four.pda.client.model;

import java.util.Date;

/**
 * Created by asavinova on 02/11/15.
 */
public class Comment extends AbstractComment {

	private String nickname;
	private Date date;
	private Karma karma;

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

	public Karma getKarma() {
		return karma;
	}

	public void setKarma(Karma karma) {
		this.karma = karma;
	}

	public static class Karma {

		private int likes;
		private int unknown1;
		private int unknown2;
		private int unknown3;

		public int getLikes() {
			return likes;
		}

		public void setLikes(int likes) {
			this.likes = likes;
		}

		public int getUnknown1() {
			return unknown1;
		}

		public void setUnknown1(int unknown1) {
			this.unknown1 = unknown1;
		}

		public int getUnknown2() {
			return unknown2;
		}

		public void setUnknown2(int unknown2) {
			this.unknown2 = unknown2;
		}

		public int getUnknown3() {
			return unknown3;
		}

		public void setUnknown3(int unknown3) {
			this.unknown3 = unknown3;
		}

	}

}
