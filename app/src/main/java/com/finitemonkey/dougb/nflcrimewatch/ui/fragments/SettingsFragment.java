package com.finitemonkey.dougb.nflcrimewatch.ui.fragments;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.finitemonkey.dougb.nflcrimewatch.R;
import com.finitemonkey.dougb.nflcrimewatch.data.NFLCrimewatchDatabase;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Arrests;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.TeamArrestsViewModel;
import com.finitemonkey.dougb.nflcrimewatch.ui.widget.NFLCrimewatchWidget;
import com.finitemonkey.dougb.nflcrimewatch.ui.widget.WidgetData;
import com.finitemonkey.dougb.nflcrimewatch.utils.AppExecutors;
import com.finitemonkey.dougb.nflcrimewatch.utils.Prefs;

import java.util.List;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    TeamArrestsViewModel mViewModel;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_crimewatch);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Prefs.FAVORITE_TEAM)) {
            final String teamId = sharedPreferences.getString(key, null);
            final Context context = this.getContext();
            final Activity activity = this.getActivity();

            // Run the requests to delete all the matching arrest records and then insert all
            // the new set of records
            AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    NFLCrimewatchDatabase db = NFLCrimewatchDatabase.getInstance(context);
                    List<Arrests> arrests = db.arrestsDao().loadWidgetArrests(teamId);

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
                            context);
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
                    WidgetData.updateTeamArrestWidgetData(activity, teamId);
                }
            });
        }
    }
}
