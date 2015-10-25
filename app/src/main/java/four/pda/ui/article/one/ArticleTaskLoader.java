package four.pda.ui.article.one;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;

import four.pda.Dao;
import four.pda.FourPdaClient;
import four.pda.dao.Article;

/**
 * Created by asavinova on 12/04/15.
 */
public class ArticleTaskLoader extends AsyncTaskLoader<ArticleTaskLoader.WrapperInfo> {

	public class WrapperInfo {
		Article article;
		String content;
	}

	private Dao dao;
	private FourPdaClient client;
	private long id;

	public ArticleTaskLoader(Context context, Dao dao, FourPdaClient client, long id) {
		super(context);
		this.dao = dao;
		this.client = client;
		this.id = id;
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		forceLoad();
	}

	@Override
	public WrapperInfo loadInBackground() {
		Article article = dao.getArticle(id);

		try {
			String content = client.getArticleContent(article.getDate(), article.getServerId());

			WrapperInfo wrapperInfo = new WrapperInfo();
			wrapperInfo.article = article;
			wrapperInfo.content = content;

			return wrapperInfo;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
