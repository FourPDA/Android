package four.pda.dagger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import four.pda.client.CategoryType;
import four.pda.client.FourPdaClient;
import four.pda.client.LoginParams;
import four.pda.client.exceptions.ParseException;
import four.pda.client.model.AbstractComment;
import four.pda.client.model.Comment;
import four.pda.client.model.CommentsResponse;
import four.pda.client.model.DeletedComment;
import four.pda.client.model.ListArticle;
import four.pda.client.model.Profile;
import okhttp3.OkHttpClient;

/**
 * Created by asavinova on 23/02/16.
 */
public class DummyFourPdaClient extends FourPdaClient {

	private static List<ListArticle> articles = new ArrayList<>();
	private static List<AbstractComment> comments = new ArrayList<>();

	static {
		{
			ListArticle article = new ListArticle();
			article.setId(0);
			article.setTitle("Обычное поведение");
			article.setDescription("");
			article.setImage("http://s.4pda.to/Af3e6vSAQSlqjknILz0pcMd4igtNNtXXYZLvV.jpg");
			article.setDate(new Date());
			article.setPublishedDate(new Date());
			articles.add(article);
		}
		{
			ListArticle article = new ListArticle();
			article.setId(1);
			article.setTitle("Ошибка парсинга");
			article.setDescription("");
			article.setImage("http://s.4pda.to/Af3e6vSAQSlqjknILz0pcMd4igtNNtXXYZLvV.jpg");
			article.setDate(new Date());
			article.setPublishedDate(new Date());
			articles.add(article);
		}
		{
			ListArticle article = new ListArticle();
			article.setId(2);
			article.setTitle("Ошибка сети");
			article.setDescription("");
			article.setImage("http://s.4pda.to/Af3e6vSAQSlqjknILz0pcMd4igtNNtXXYZLvV.jpg");
			article.setDate(new Date());
			article.setPublishedDate(new Date());
			articles.add(article);
		}
		{
			ListArticle article = new ListArticle();
			article.setId(3);
			article.setTitle("Ошибка парсинга комментариев");
			article.setDescription("");
			article.setImage("http://s.4pda.to/Af3e6vSAQSlqjknILz0pcMd4igtNNtXXYZLvV.jpg");
			article.setDate(new Date());
			article.setPublishedDate(new Date());
			articles.add(article);
		}

		{
			ListArticle article = new ListArticle();
			article.setId(4);
			article.setTitle("Ошибка сети при запросе комментариев");
			article.setDescription("");
			article.setImage("http://s.4pda.to/Af3e6vSAQSlqjknILz0pcMd4igtNNtXXYZLvV.jpg");
			article.setDate(new Date());
			article.setPublishedDate(new Date());
			articles.add(article);
		}

		{
			// http://4pda.ru/2016/03/26/286724/
			ListArticle article = new ListArticle();
			article.setId(286724);
			article.setTitle("Honor 7 получает обновление до Android Marshmallow");
			article.setDescription("");
			article.setImage("http://s.4pda.to/2RBAcCaBdc1x4jkcsl7mWqZlpBepS7dfWN5z0.jpg");
			article.setDate(new Date());
			article.setPublishedDate(new Date());
			articles.add(article);
		}

		{
			// http://4pda.ru/2016/03/25/286355/
			ListArticle article = new ListArticle();
			article.setId(286355);
			article.setTitle("Microsoft работает над универсальным приложением Skype");
			article.setDescription("");
			article.setImage("http://s.4pda.to/2RBAdJQfItAff0ZxZkac7eLOd9jwhpr4Dw8W.jpg");
			article.setDate(new Date());
			article.setPublishedDate(new Date());
			articles.add(article);
		}

		{
			// http://4pda.ru/2016/03/25/286425/
			ListArticle article = new ListArticle();
			article.setId(286425);
			article.setTitle("Выиграй 1 ТБ и антивирус на все гаджеты в конкурсе от Dr. Web");
			article.setDescription("");
			article.setImage("http://s.4pda.to/2RBAdFIaF2xopvCJdarD5jSlphuRODs2224z1.jpg");
			article.setDate(new Date());
			article.setPublishedDate(new Date());
			articles.add(article);
		}

		{
			Comment comment = new Comment();
			comment.setId(newId());
			comment.setDate(new Date());
			comment.setNickname("Test");
			comment.setLevel(0);
			comment.setContent("Комментарий");
			comments.add(comment);
		}
		{
			DeletedComment comment = new DeletedComment();
			comment.setId(0);
			comment.setLevel(0);
			comment.setContent("Комментарий удален");
			comments.add(comment);
		}
		{
			Comment comment = new Comment();
			comment.setId(newId());
			comment.setDate(new Date());
			comment.setNickname("Test");
			comment.setLevel(1);
			comment.setContent("Комментарий");
			comments.add(comment);
		}
		{
			Comment comment = new Comment();
			comment.setId(newId());
			comment.setDate(new Date());
			comment.setNickname("Test");
			comment.setLevel(2);
			comment.setContent("Комментарий");
			comments.add(comment);
		}
		{
			Comment comment = new Comment();
			comment.setId(newId());
			comment.setDate(new Date());
			comment.setNickname("Test");
			comment.setLevel(3);
			comment.setContent("Комментарий");
			comments.add(comment);
		}
		{
			Comment comment = new Comment();
			comment.setId(newId());
			comment.setDate(new Date());
			comment.setNickname("Test");
			comment.setLevel(4);
			comment.setContent("Комментарий");
			comments.add(comment);
		}
		{
			Comment comment = new Comment();
			comment.setId(newId());
			comment.setDate(new Date());
			comment.setNickname("Test");
			comment.setLevel(5);
			comment.setContent("Комментарий");
			comments.add(comment);
		}
		{
			Comment comment = new Comment();
			comment.setId(newId());
			comment.setDate(new Date());
			comment.setNickname("Very long nick name that I can imagine in the world");
			comment.setLevel(5);
			comment.setContent("Комментарий");
			comments.add(comment);
		}
	}

	public DummyFourPdaClient(OkHttpClient client) {
		super(client);
	}

	private static long newId() {
		return (long) (Math.random() * Long.MAX_VALUE);
	}

	@Override
	public List<ListArticle> getArticles(CategoryType type, int page) throws IOException {
		return articles;
	}

	@Override
	public String getArticleContent(Date date, long id) throws IOException {
		if (id == 1) {
			throw new ParseException("");
		}

		if (id == 2) {
			throw new IOException();
		}

		return articles.get((int) id).getTitle();
	}

	@Override
	public CommentsResponse getArticleComments(Date date, Long id) throws IOException {
		if (id == 3) {
			throw new ParseException("");
		}

		if (id == 4) {
			throw new IOException();
		}

		CommentsResponse response = new CommentsResponse();
		response.setComments(comments);
		response.setCanComment(true);

		return response;
	}

	@Override
	public List<AbstractComment> addComment(String message, Long replyId) {
		Comment comment = new Comment();
		comment.setId(System.currentTimeMillis());
		comment.setDate(new Date());
		comment.setNickname("You");
		comment.setContent(message);

		if (replyId == null) {
			comment.setLevel(0);
			comments.add(comment);
			return comments;
		}

		List<AbstractComment> updatedComments = new ArrayList<>();
		for (AbstractComment cmnt : comments) {
			updatedComments.add(cmnt);

			if (cmnt.getId() == replyId) {
				comment.setLevel(cmnt.getLevel() + 1);
				updatedComments.add(comment);
			}
		}
		comments = updatedComments;
		return comments;
	}

	@Override
	public long login(LoginParams params) throws IOException {
		return 4975039l;
	}

	@Override
	public boolean logout() throws IOException {
		return true;
	}

	@Override
	public Profile getProfile(long id) throws IOException {
		Profile profile = new Profile();
		profile.setLogin("var.ann");
		profile.setPhoto("http://s.4pda.to/tp6nuQlKPdPSv8fwz1HfNVeHMOUxPbaFg.jpg");
		return profile;
	}
}
