package com.finitemonkey.dougb.nflcrimewatch.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.finitemonkey.dougb.nflcrimewatch.R;
import com.finitemonkey.dougb.nflcrimewatch.ui.activities.MainActivity;
import com.finitemonkey.dougb.nflcrimewatch.utils.Prefs;

/**
 * Implementation of App Widget functionality.
 */
public class NFLCrimewatchWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Get the shared preferences reference
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                context);
        String recentPlayer = sharedPreferences.getString(Prefs.RECENT_PLAYER, "none");
        String recentPosition = sharedPreferences.getString(Prefs.RECENT_POSITION, "none");
        int recentLogo = sharedPreferences.getInt(Prefs.RECENT_LOGO, R.drawable.ic_nfl);
        String recentCrime = sharedPreferences.getString(Prefs.RECENT_CRIME, "none");
        String recentPrimary = sharedPreferences.getString(Prefs.RECENT_PRIMARY_COLOR, "FFFFFF");
        String recentSecondary = sharedPreferences.getString(Prefs.RECENT_SECONDARY_COLOR, "444444");

        // Set the click handler to open the DetailActivity for plant ID,
        // or the MainActivity if plant ID is invalid
        Intent intent;
            intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(
                context.getPackageName(), R.layout.nflcrimewatch_widget);
        views.setTextViewText(R.id.widget_player, recentPlayer);
        views.setTextViewText(R.id.widget_crime, recentCrime);
        views.setImageViewResource(R.id.widget_logo, recentLogo);
        views.setOnClickPendingIntent(R.id.rl_widget, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateCrimewatchWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        updateAppWidget(context, appWidgetManager, appWidgetId);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

