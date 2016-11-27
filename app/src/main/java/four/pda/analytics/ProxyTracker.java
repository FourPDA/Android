package four.pda.analytics;

import android.content.Context;

import java.util.Map;

/**
 * Трекер-прокси, переправляющий запросы в реальный трекер аналитики.
 *
 * @author Pavel Savinov
 * @since 1.0.0
 */
class ProxyTracker extends AnalyticsTracker {

    public ProxyTracker(Context context) {
    }

    @Override
    public void send(Map<String, String> params) {
    }

}
