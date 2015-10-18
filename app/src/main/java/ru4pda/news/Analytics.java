package ru4pda.news;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Analytics.
 *
 * @author Pavel Savinov.
 */
public class Analytics {

    private final Tracker tracker;

    public Analytics(Context context) {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
        tracker = analytics.newTracker("UA-68992461-1");
    }

}
