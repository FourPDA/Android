package four.pda.analytics;

import android.content.Context;

import com.google.android.gms.analytics.HitBuilders;

import java.util.Map;

import four.pda.BuildConfig;
import four.pda.ui.CategoryType;

/**
 * Analytics.
 *
 * @author Pavel Savinov.
 */
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
			tracker.send(action("About click"));
		}

		public void categoryClicked(CategoryType type) {
			tracker.send(action("Category click " + type.name()));
		}

		private Map<String, String> action(String action) {
			return new HitBuilders.EventBuilder()
					.setCategory("Drawer")
					.setAction(action)
					.build();
		}

	}

	public class ArticlesList {

		public void scrollUp(int currentPosition) {
			tracker.send(new HitBuilders.EventBuilder()
					.setCategory("ArticlesList")
					.setAction("Scroll up")
					.setCustomMetric(1, currentPosition)
					.build());
		}

	}

}
