package four.pda.ui.article.one;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;
import java.util.Date;

import four.pda.FourPdaClient;
import four.pda.ui.LoadResult;

/**
 * Created by asavinova on 12/04/15.
 */
public class ArticleTaskLoader extends AsyncTaskLoader<LoadResult<String>> {

	private FourPdaClient client;
	private long id;
	private Date date;

	public ArticleTaskLoader(Context context, FourPdaClient client, long id, Date date) {
		super(context);
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
			e.printStackTrace();
			return new LoadResult<>(e);
		}
	}

}
