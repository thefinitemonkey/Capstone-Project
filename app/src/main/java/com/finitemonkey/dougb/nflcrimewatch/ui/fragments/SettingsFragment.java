package com.finitemonkey.dougb.nflcrimewatch.ui.fragments;

import android.app.Activity;
import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.finitemonkey.dougb.nflcrimewatch.R;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Arrests;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.TeamArrestsViewModel;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.TeamArrestsViewModelFactory;
import com.finitemonkey.dougb.nflcrimewatch.ui.widget.NFLCrimewatchWidget;
import com.finitemonkey.dougb.nflcrimewatch.ui.widget.WidgetData;
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
            String teamId = sharedPreferences.getString(key, null);

            // Get the view model for the team arrests and watch for changes
            final Activity activity = this.getActivity();
            final Context context = this.getContext();
            Application application = this.getActivity().getApplication();
            mViewModel =
                    ViewModelProviders.of(this, new TeamArrestsViewModelFactory(
                            application, teamId)
                    ).get(TeamArrestsViewModel.class);
            mViewModel.getTeamArrests().observe(this, new Observer<List<Arrests>>() {
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
                            context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Prefs.RECENT_PLAYER, recentPlayer)
                            .putString(Prefs.RECENT_POSITION, recentPosition)
                            .putString(Prefs.RECENT_CRIME, recentCrime)
                            .putString(Prefs.RECENT_PRIMARY_COLOR, recentPrimary)
                            .putString(Prefs.RECENT_SECONDARY_COLOR, recentSecondary)
                            .putInt(Prefs.RECENT_LOGO, recentLogo).commit();

                    // Make the call to update widgets
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(activity);
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(activity, NFLCrimewatchWidget.class));
                    for (int appWidgetId: appWidgetIds
                            ) {
                        NFLCrimewatchWidget.updateCrimewatchWidget(activity, appWidgetManager, appWidgetId);
                    }
                }
            });


            WidgetData.updateTeamArrestWidgetData(this.getActivity(), teamId);
        }
    }
}
