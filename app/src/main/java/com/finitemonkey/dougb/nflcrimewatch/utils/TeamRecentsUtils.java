package com.finitemonkey.dougb.nflcrimewatch.utils;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import com.finitemonkey.dougb.nflcrimewatch.data.NFLCrimewatchDatabase;
import com.finitemonkey.dougb.nflcrimewatch.data.converters.DateConverter;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.TeamRecents;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TeamRecentsUtils {
    private static NFLCrimewatchDatabase mDb;
    private static final String TAG = TeamRecentsUtils.class.getSimpleName();

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

                    if (days < 1) {
                        hasBeenUpdated = true;
                    }
                }

                // Make the callback
                listener.onTeamRecentsCheckResult(hasBeenUpdated);
            }
        });
    }

    // Clears existing data from the TeamRecents table and inserts new data
    public static void updateAllTeamRecents(Context context, final List<TeamRecents> teamRecents) {
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

    public static void updateSingleTeamRecents(Context context, Object callback, final TeamRecents teamRecent) {
        // Hold onto the context as the listener for the callback
        final TeamRecentUpdateData listener = (TeamRecentUpdateData) callback;

        // Set up the instance of the database if needed
        if (mDb == null) {
            mDb = NFLCrimewatchDatabase.getInstance(context);
        }

        // Run the requests to check the existing data records for the given team to see if
        // there is a match. Update if so. Delete old data and insert new if not.
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                Date newUpdate = Calendar.getInstance().getTime();
                TeamRecents ntr = teamRecent;
                ntr.setUpdatedAt(newUpdate);
                Log.d(TAG, "run: value of ntr.updatedAt is " + DateConverter.toString(ntr.getUpdatedAt()));
                // There may be occurrences from a single team on a given date
                List<TeamRecents> ltr = mDb.teamRecentDao().checkTeamDateOccurrences(
                        ntr.getTeam(),
                        DateConverter.toString(ntr.getDate())
                );

                // If there isn't anything in the list then add the new occurrence and we're done
                if (ltr.size() < 1)
                {
                    if (ntr != null) {
                        mDb.teamRecentDao().updateTeamRecent(ntr);

                        listener.onTeamRecentDataUpdated(ntr);
                        return;
                    }
                };

                // If the first occurrence doesn't match the date of the one passed in then
                // delete occurrences for the team
                if (!ltr.get(0).getDate().equals(ntr.getDate())) {
                    mDb.teamRecentDao().deleteTeamRecentsForTeam(ntr.getTeam());
                    // Add the new occurrence and exit
                    mDb.teamRecentDao().updateTeamRecent(ntr);

                    listener.onTeamRecentDataUpdated(ntr);
                    return;
                }

                // If it's the same date then check that this occurrence isn't already in the list
                for (TeamRecents tr:ltr
                     ) {
                    if(tr.getArrestStatsId() == ntr.getArrestStatsId()) {
                        // We just need to update this particular record
                        tr.setUpdatedAt(ntr.getUpdatedAt());
                        mDb.teamRecentDao().updateTeamRecent(tr);

                        listener.onTeamRecentDataUpdated(tr);
                        return;
                    }
                }

                // This new occurrence doesn't exist yet so it needs to be added
                mDb.teamRecentDao().updateTeamRecent(ntr);
                listener.onTeamRecentDataUpdated(ntr);
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

    public interface TeamRecentUpdateData {
        void onTeamRecentDataUpdated(TeamRecents teamRecent);
    }
}
