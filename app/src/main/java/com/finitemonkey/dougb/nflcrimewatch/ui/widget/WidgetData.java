package com.finitemonkey.dougb.nflcrimewatch.ui.widget;

import android.app.Activity;
import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.finitemonkey.dougb.nflcrimewatch.data.tables.Arrests;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.TeamArrestsViewModel;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.TeamArrestsViewModelFactory;
import com.finitemonkey.dougb.nflcrimewatch.utils.Prefs;

import java.util.List;

public class WidgetData {
    private TeamArrestsViewModel mViewModel;

    public WidgetData(final Activity activity, String teamId) {
        // Get the view model for the team arrests and watch for changes
        Application application = activity.getApplication();
        mViewModel =
                ViewModelProviders.of((FragmentActivity) activity, new TeamArrestsViewModelFactory(
                        application, teamId)
                ).get(TeamArrestsViewModel.class);
        LifecycleOwner owner = (LifecycleOwner) activity;
        mViewModel.getTeamArrests().observe(owner, new Observer<List<Arrests>>() {
            @Override
            public void onChanged(@Nullable List<Arrests> arrests) {
                // Get the data for the first record and set it into the prefs
                if (arrests.size() == 0) return;

                // If there is are arrests then grab the data from the first one
                String recentPlayer = arrests.get(0).getPlayerName();
                String recentPosition = arrests.get(0).getPlayerPositionName();
                String recentCrime = arrests.get(0).getCategory();
                String recentPrimary = arrests.get(0).getTeamHexColor();
                String recentSecondary = arrests.get(0).getTeamHexAltColor();
                int recentLogo = arrests.get(0).getLogo();

                // Write the new data into the preferences
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                        activity);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Prefs.RECENT_PLAYER, recentPlayer)
                        .putString(Prefs.RECENT_POSITION, recentPosition)
                        .putString(Prefs.RECENT_CRIME, recentCrime)
                        .putString(Prefs.RECENT_PRIMARY_COLOR, recentPrimary)
                        .putString(Prefs.RECENT_SECONDARY_COLOR, recentSecondary)
                        .putInt(Prefs.RECENT_LOGO, recentLogo).apply();

                // Make the call to update widgets
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(activity);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(activity, NFLCrimewatchWidget.class));
                for (int appWidgetId: appWidgetIds
                        ) {
                    NFLCrimewatchWidget.updateCrimewatchWidget(activity, appWidgetManager, appWidgetId);
                }
            }
        });
    }

    public static void updateTeamArrestWidgetData(final Activity activity, String teamId) {
        // Check that teamId isn't null
        //if (teamId == null) return;


    }
}
