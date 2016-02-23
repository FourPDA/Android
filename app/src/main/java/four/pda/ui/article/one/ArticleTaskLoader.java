package four.pda.ui.article.one;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;

import four.pda.App;
import four.pda.client.FourPdaClient;
import four.pda.ui.LoadResult;

/**
 * Created by asavinova on 12/04/15.
 */
public class ArticleTaskLoader extends AsyncTaskLoader<LoadResult<String>> {

	private static final Logger L = LoggerFactory.getLogger(ArticleTaskLoader.class);

	@Inject FourPdaClient client;

	private long id;
	private Date date;

	public ArticleTaskLoader(Context context, FourPdaClient client, long id, Date date) {
		super(context);

		((App) context.getApplicationContext()).component().inject(this);

		this.client = client;
		this.id = id;
		this.date = date;
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		forceLoad();
	}

	@Override
	public LoadResult<String> loadInBackground() {
		try {
			return new LoadResult<>(client.getArticleContent(date, id));
		} catch (IOException e) {
			L.error("Article request error", e);
			return new LoadResult<>(e);
		}
	}

}
