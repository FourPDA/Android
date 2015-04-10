package ru4pda.news;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

import ru4pda.news.dao.Article;
import ru4pda.news.dao.ArticleDao;
import ru4pda.news.dao.DaoMaster;
import ru4pda.news.dao.DaoSession;
import ru4pda.news.client.model.SimpleArticle;

/**
 * Created by asavinova on 10/04/15.
 */
@EBean(scope = EBean.Scope.Singleton)
public class Dao {

	private SQLiteDatabase db;
	@RootContext Context context;

	private DaoMaster.DevOpenHelper helper;
	private DaoMaster daoMaster;
	private DaoSession daoSession;

	@AfterInject
	void init() {
		helper = new DaoMaster.DevOpenHelper(context, "ru4pda-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
	}

	public void setArticles(final List<SimpleArticle> simpleArticles) {
		daoSession.runInTx(new Runnable() {
			@Override
			public void run() {
				ArticleDao dao = daoSession.getArticleDao();

				for (SimpleArticle simpleArticle : simpleArticles) {
					Article article = new Article();
					article.setId(simpleArticle.getId());
					article.setDate(simpleArticle.getDate());
					article.setTitle(simpleArticle.getTitle());
					article.setDescription(simpleArticle.getDescription());
					dao.insertOrReplace(article);
				}
			}
		});
	}

	public Cursor getArticleCursor() {
		ArticleDao dao = daoSession.getArticleDao();
		return db.query(ArticleDao.TABLENAME, dao.getAllColumns(), null, null, null, null,
				ArticleDao.Properties.Date.columnName);
	}
}
