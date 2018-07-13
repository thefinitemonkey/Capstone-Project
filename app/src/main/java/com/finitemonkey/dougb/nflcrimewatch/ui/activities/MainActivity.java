package com.finitemonkey.dougb.nflcrimewatch.ui.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.finitemonkey.dougb.nflcrimewatch.R;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Stadiums;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.TeamRecents;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.ClosestTeamViewModel;
import com.finitemonkey.dougb.nflcrimewatch.data.viewmodels.TeamRecentsViewModel;
import com.finitemonkey.dougb.nflcrimewatch.network.RecentByTeamsAPI;
import com.finitemonkey.dougb.nflcrimewatch.utils.StadiumUtils;
import com.finitemonkey.dougb.nflcrimewatch.utils.TeamRecentsUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecentByTeamsAPI.RecentByTeamsListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupClosestTeamViewModel();
        setupTeamRecentsViewModel();
    }

    private void setupClosestTeamViewModel() {
        ClosestTeamViewModel viewModel = ViewModelProviders.of(this).get(
                ClosestTeamViewModel.class);
        viewModel.getStadiums().observe(this, new Observer<List<Stadiums>>() {
            @Override
            public void onChanged(@Nullable List<Stadiums> stadiums) {
                // Test the calculation to closest stadium
                // lat 29.6503993 lon -95.7350763
                String teamId = StadiumUtils.getClosestTeam(stadiums, 40.1677863, -83.0089769);
                Log.d(TAG, "onChanged: closest team is " + teamId);
            }
        });
    }

    private void setupTeamRecentsViewModel() {
        TeamRecentsViewModel viewModel = ViewModelProviders.of(this).get(
                TeamRecentsViewModel.class);
        viewModel.getTeamRecents().observe(this, new Observer<List<TeamRecents>>() {
            @Override
            public void onChanged(@Nullable List<TeamRecents> teamRecents) {
                onTeamRecentsCheckResult(TeamRecentsUtils.startCheckUpdatedInPastDay(teamRecents));
            }
        });
    }

    private void onTeamRecentsCheckResult(Boolean hasBeenUpdated) {
        Log.d(TAG, "onTeamRecentsCheckResult: data has been updated today is " + hasBeenUpdated);
        if (!hasBeenUpdated) {
            // Need to make the daily check for updates to TeamRecents (recents offenses by team)
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
    public void onRecentByTeamsLoadComplete(List<TeamRecents> teamRecents) {
        for (TeamRecents tr : teamRecents
                ) {
            Log.d(TAG, "onRecentByTeamsLoadComplete: teamId - " + tr.getTeam());
        }
    }
}
