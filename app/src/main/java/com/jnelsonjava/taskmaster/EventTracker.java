package com.jnelsonjava.taskmaster;

import android.view.View;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.core.Amplify;

import static com.jnelsonjava.taskmaster.MainActivity.loggerI;

public class EventTracker {
    public static void trackButtonClicked(View v) {
        loggerI("EventTracker", "button click on " + v.getResources().getResourceEntryName(v.getId()));

        String currentUser = (Amplify.Auth.getCurrentUser() != null) ? Amplify.Auth.getCurrentUser().getUsername() : "Guest";

        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("ButtonClicked")
                .addProperty("ButtonID", v.getResources().getResourceEntryName(v.getId()))
                .addProperty("User", currentUser)
                .build();

        Amplify.Analytics.recordEvent(event);
    }

    public static void trackAppStart() {
        loggerI("EventTracker", "App Start Event");

        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("NewAppStart")
                .addProperty("StartupSuccess", true)
                .build();

        Amplify.Analytics.recordEvent(event);
    }
}
