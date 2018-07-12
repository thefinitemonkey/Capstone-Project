package com.finitemonkey.dougb.nflcrimewatch.utils;

import android.content.Context;

import com.finitemonkey.dougb.nflcrimewatch.data.NFLCrimewatchDatabase;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.TeamRecents;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TeamRecentsUtils {
    private static NFLCrimewatchDatabase mDb;

    // This function does the work to check in the team_recents (TeamRecents) table to
    // verify whether the data is more than a day old
    public static void startCheckUpdatedInPastDay(Context context) {
        // Hold onto the context as the listener for the callback
        final TeamRecentsUpdateInPastDayResult listener = (TeamRecentsUpdateInPastDayResult) context;

        // Set up the instance of the database if needed
        if (mDb == null) {
            mDb = NFLCrimewatchDatabase.getInstance(context);
        }

        // Run the request for all recent team offenses and check last update in a Runnable
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                Boolean hasBeenUpdated = false;
                final List<TeamRecents> offenses = mDb.teamRecentDao().loadTeamRecents();
                // If there are no results then we've never loaded. Otherwise check
                // to see how long since the last update.
                if (offenses.size() > 0) {
                    Date today = Calendar.getInstance().getTime();
                    Date lastUpdate = offenses.get(0).getUpdatedAt();
                    int days = (int) (today.getTime() - lastUpdate.getTime()) / (1000 * 60 * 60 * 24);

                    if (days == 0) {
                        hasBeenUpdated = true;
                    }
                }

                // Make the callback
                listener.onTeamRecentsCheckResult(hasBeenUpdated);
            }
        });
    }

    // Clears existing data from the TeamRecents table and inserts new data
    public static void updateTeamRecents(Context context, final List<TeamRecents> teamRecents) {
        // Hold onto the context as the listener for the callback
        final TeamRecentsUpdateData listener = (TeamRecentsUpdateData) context;

        // Set up the instance of the database if needed
        if (mDb == null) {
            mDb = NFLCrimewatchDatabase.getInstance(context);
        }

        // Run the requests to dump the existing data and add the new data
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                TeamRecents[] trArray = new TeamRecents[teamRecents.size()];
                for (int i = 0; i < trArray.length - 1; i++) {
                    trArray[i] = teamRecents.get(i);
                }
                mDb.teamRecentDao().deleteAllTeamRecents();
                mDb.teamRecentDao().insertAll(trArray);

                // Make the callback
                listener.onTeamRecentsDataUpdated(teamRecents);
            }
        });

    }

    // Interface so caller can listen for result of the updatedInPastDay check
    public interface TeamRecentsUpdateInPastDayResult {
        void onTeamRecentsCheckResult(Boolean hasBeenUpdated);
    }

    public interface TeamRecentsUpdateData {
        void onTeamRecentsDataUpdated(List<TeamRecents> teamRecents);
    }
}
