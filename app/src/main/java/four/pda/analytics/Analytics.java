package four.pda.analytics;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
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

    private final AnalyticsTracker tracker;

    public Analytics(Context context) {

        if (BuildConfig.VERSION_CODE == 1) {
            // Не отправляем аналитику, если сборка девелоперская
            tracker = new AnalyticsTracker();
            return;
        }

        GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
        analytics.enableAutoActivityReports((Application) context.getApplicationContext());
        tracker = new ProxyTracker(analytics.newTracker("UA-68992461-1"));
    }

    public Drawer drawer() {
        return drawer;
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

}
