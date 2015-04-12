package ru4pda.news;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

import ru4pda.news.client.model.ListArticle;
import ru4pda.news.dao.Article;
import ru4pda.news.dao.ArticleDao;
import ru4pda.news.dao.DaoMaster;
import ru4pda.news.dao.DaoSession;

/**
 * Created by asavinova on 10/04/15.
 */
@EBean(scope = EBean.Scope.Singleton)
public class Dao {

	private static final Logger L = LoggerFactory.getLogger(Dao.class);

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

	public void setArticles(final List<ListArticle> listArticles, final boolean needClearData) {
		daoSession.runInTx(new Runnable() {
			@Override
			public void run() {
				ArticleDao dao = daoSession.getArticleDao();

				if (needClearData) {
					dao.deleteAll();
					L.trace("Delete all articles");
				}

				ListArticle firstArticle = listArticles.get(0);
				Date currentDate = firstArticle.getDate();
				int position = getMaxInDayPosition(currentDate);

				for (ListArticle listArticle : listArticles) {

					if (!currentDate.equals(listArticle.getDate())) {
						currentDate = listArticle.getDate();
						position = getMaxInDayPosition(currentDate);
					}

					Article article = new Article();
					article.setId(listArticle.getId());
					article.setDate(listArticle.getDate());
					article.setTitle(listArticle.getTitle());
					article.setDescription(listArticle.getDescription());
					position++;
					article.setPosition(position);

					dao.insertOrReplace(article);
				}
			}
		});
	}

	private int getMaxInDayPosition(Date date) {
		Article article = daoSession.getArticleDao().queryBuilder()
				.where(ArticleDao.Properties.Date.eq(date))
				.orderDesc(ArticleDao.Properties.Position)
				.limit(1)
				.build().unique();
		if (article == null) {
			return -1;
		}
		return article.getPosition();
	}

	public Cursor getArticleCursor() {
		ArticleDao dao = daoSession.getArticleDao();
		return db.query(ArticleDao.TABLENAME, dao.getAllColumns(), null, null, null, null,
				ArticleDao.Properties.Date.columnName + " DESC, " +
				ArticleDao.Properties.Position.columnName + " ASC");
	}

	public Article getArticle(long id) {
		ArticleDao dao = daoSession.getArticleDao();
		return dao.queryBuilder()
				.where(ArticleDao.Properties.Id.eq(id))
				.build().unique();
	}
}
