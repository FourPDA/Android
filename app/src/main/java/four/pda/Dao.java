package four.pda;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import four.pda.client.CategoryType;
import four.pda.client.model.ListArticle;
import four.pda.dao.Article;
import four.pda.dao.ArticleDao;
import four.pda.dao.DaoMaster;
import four.pda.dao.DaoSession;

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
		helper = new DaoMaster.DevOpenHelper(context, "app", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
	}

	public void setArticles(final List<ListArticle> articles, final CategoryType category, final boolean needClearData) {
		daoSession.runInTx(new Runnable() {
			@Override
			public void run() {
				ArticleDao dao = daoSession.getArticleDao();

				if (needClearData) {
					dao.queryBuilder()
							.where(ArticleDao.Properties.Category.eq(getCategoryValue(category)))
							.buildDelete()
							.executeDeleteWithoutDetachingEntities();
					L.trace("Delete all articles from category {}", category);
				} else {
					L.trace("No need clear for category {}", category);
				}

				for (ListArticle article : articles) {

					Article daoArticle = new Article();
					daoArticle.setId(article.getId());
					daoArticle.setDate(article.getDate());
					daoArticle.setTitle(article.getTitle());
					daoArticle.setImage(article.getImage());
					daoArticle.setCategory(getCategoryValue(category));
					daoArticle.setDescription(article.getDescription());
					daoArticle.setPublishedDate(article.getPublishedDate());
					daoArticle.setCommentsCount(article.getCommentsCount());

					dao.insertOrReplace(daoArticle);
				}
			}
		});

		L.trace("All articles count = {}", daoSession.getArticleDao().count());
	}

	private String getCategoryValue(CategoryType category) {
		return category.name().toLowerCase();
	}

	public Cursor getArticleCursor(CategoryType category) {
		ArticleDao dao = daoSession.getArticleDao();
		return db.query(ArticleDao.TABLENAME, dao.getAllColumns(),
				ArticleDao.Properties.Category.columnName + " == '" + getCategoryValue(category) + "'",
				null,
				null,
				null,
				ArticleDao.Properties.PublishedDate.columnName + " DESC");
	}

	public Article getArticle(long id) {
		ArticleDao dao = daoSession.getArticleDao();
		return dao.load(id);
	}
}
