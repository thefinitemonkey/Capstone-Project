package com.finitemonkey.dougb.nflcrimewatch.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.finitemonkey.dougb.nflcrimewatch.R;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.TeamRecents;
import com.finitemonkey.dougb.nflcrimewatch.network.RecentByTeamsAPI;
import com.finitemonkey.dougb.nflcrimewatch.utils.StadiumUtils;
import com.finitemonkey.dougb.nflcrimewatch.utils.TeamRecentsUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TeamRecentsUtils.TeamRecentsUpdateInPastDayResult,
        StadiumUtils.ClosestTeamCalculationResult, RecentByTeamsAPI.RecentByTeamsListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Test the check of updated TeamRecents
        TeamRecentsUtils.startCheckUpdatedInPastDay(this);

        // Test the calculation to closest stadium
        // lat 29.6503993 lon -95.7350763
        StadiumUtils.getClosestTeam(this, 40.1677863, -83.0089769);
    }

    @Override
    public void onTeamRecentsCheckResult(Boolean hasBeenUpdated) {
        if(!hasBeenUpdated) {
            // Test the retrieval of the recent team offenses
            String[] teamsIds = getResources().getStringArray(R.array.team_ids_array);
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strToday = dateFormat.format(today);
            String strBegin = "2000-01-01";
            RecentByTeamsAPI recentsRetrieval = new RecentByTeamsAPI();
            recentsRetrieval.getRecentByTeams(this, teamsIds, strBegin, strToday);
        }
    }

    @Override
    public void onClosestTeamResult(String teamId) {
        Log.d(TAG, "onClosestTeamResult: closest team id is " + teamId);
    }

    @Override
    public void onRecentByTeamsLoadComplete(List<TeamRecents> teamRecents) {
        for (TeamRecents tr: teamRecents
             ) {
            Log.d(TAG, "onRecentByTeamsLoadComplete: teamId - " + tr.getTeam());
        }
    }
}
