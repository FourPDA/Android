package four.pda.analytics;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.Map;

import four.pda.BuildConfig;

/**
 * Трекер-прокси, переправляющий запросы в реальный трекер Google Analytics.
 *
 * @author Pavel Savinov
 * @since 1.0.0
 */
class ProxyTracker extends AnalyticsTracker {

    private final Tracker tracker;

    public ProxyTracker(Context context) {

		GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
		analytics.enableAutoActivityReports((Application) context.getApplicationContext());
		tracker = analytics.newTracker("UA-68992461-1");
		tracker.enableAdvertisingIdCollection(true);
		tracker.enableAutoActivityTracking(true);

		String version = String.format("%s (%d)", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
		tracker.setAppVersion(version);
    }

    @Override
    public void send(Map<String, String> params) {
        tracker.send(params);
    }

}
