package ru4pda.news.analytics;

import com.google.android.gms.analytics.Tracker;

import java.util.Map;

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
    }

    @Override
    public void send(Map<String, String> params) {
        tracker.send(params);
    }

}
