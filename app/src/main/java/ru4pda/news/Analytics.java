package ru4pda.news;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Map;

import ru4pda.news.ui.CategoryType;

/**
 * Analytics.
 *
 * @author Pavel Savinov.
 */
public class Analytics {

    private Drawer drawer = new Drawer();

    private final Tracker tracker;

    public Analytics(Context context) {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
        analytics.enableAutoActivityReports((Application) context.getApplicationContext());
        tracker = analytics.newTracker("UA-68992461-1");
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
