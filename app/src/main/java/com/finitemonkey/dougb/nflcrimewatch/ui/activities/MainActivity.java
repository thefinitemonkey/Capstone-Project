package com.finitemonkey.dougb.nflcrimewatch.ui.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.finitemonkey.dougb.nflcrimewatch.R;
import com.finitemonkey.dougb.nflcrimewatch.network.ApiRequestor;
import com.finitemonkey.dougb.nflcrimewatch.utils.StadiumUtils;
import com.finitemonkey.dougb.nflcrimewatch.utils.TeamRecentsUtils;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements TeamRecentsUtils.TeamRecentsUpdateInPastDayResult, StadiumUtils.ClosestTeamCalculationResult{
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Test the check of updated TeamRecents
        TeamRecentsUtils.startCheckUpdatedInPastDay(this);

        // Test the calculation to closest stadium
        // lat 29.6503993 lon -95.7350763
        StadiumUtils.getClosestTeam(this,29.6503993, -95.7350763);
    }

    @Override
    public void onTeamRecentsCheckResult(Boolean hasBeenUpdated) {
        Log.d(TAG, "onTeamRecentsCheckResult: has been updated today is " + hasBeenUpdated);
    }

    @Override
    public void onClosestTeamResult(String teamId) {
        Log.d(TAG, "onClosestTeamResult: closest team id is " + teamId);
    }
}
