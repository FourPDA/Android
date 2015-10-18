package ru4pda.news;

import android.app.Application;

/**
 * Application.
 *
 * @author Pavel Savinov.
 */
public class App extends Application {

    private Analytics analytics;

    @Override
    public void onCreate() {
        super.onCreate();
        analytics = new Analytics(this);
    }

    public Analytics getAnalytics() {
        return analytics;
    }

}
