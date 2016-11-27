package four.pda.analytics;

import android.content.Context;

import org.androidannotations.annotations.EBean;

import four.pda.BuildConfig;
import four.pda.client.CategoryType;

/**
 * Analytics.
 *
 * @author Pavel Savinov.
 */
@EBean(scope = EBean.Scope.Singleton)
public class Analytics {

	private Drawer drawer = new Drawer();
	private ArticlesList articlesList = new ArticlesList();

	private final AnalyticsTracker tracker;

	public Analytics(Context context) {

		if (BuildConfig.VERSION_CODE == 1) {
			// Не отправляем аналитику, если сборка девелоперская
			tracker = new AnalyticsTracker();
			return;
		}

		this.tracker = new ProxyTracker(context);
	}

	public Drawer drawer() {
		return drawer;
	}

	public ArticlesList articlesList() {
		return articlesList;
	}

	public class Drawer {

		public void aboutClicked() {
			// TODO Send by tracker
		}

		public void categoryClicked(CategoryType type) {
			// TODO Send by tracker
		}

	}

	public class ArticlesList {

		public void scrollUp(int currentPosition) {
			// TODO Send by tracker
		}

	}

}
