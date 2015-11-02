package four.pda.analytics;

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

    public ProxyTracker(Tracker tracker) {
        this.tracker = tracker;
		String version = String.format("%s (%d)", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
		tracker.setAppVersion(version);
		tracker.enableAdvertisingIdCollection(true);
		tracker.enableAutoActivityTracking(true);
    }

    @Override
    public void send(Map<String, String> params) {
        tracker.send(params);
    }

}
