package com.finitemonkey.dougb.nflcrimewatch.utils;

import android.content.Context;
import android.util.Log;

import com.finitemonkey.dougb.nflcrimewatch.R;
import com.finitemonkey.dougb.nflcrimewatch.data.NFLCrimewatchDatabase;
import com.finitemonkey.dougb.nflcrimewatch.data.converters.DateConverter;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Arrests;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Recents;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecentsUtils {
    private static NFLCrimewatchDatabase mDb;
    private static final String TAG = RecentsUtils.class.getSimpleName();

    // This function does the work to check in the team_recents (TeamRecents) table to
    // verify whether the data is more than a day old
    public static Boolean startCheckUpdatedInPastDay(List<Arrests> recents) {
        // If there are no results then we've never loaded. Otherwise check
        // to see how long since the last update.'
        Boolean hasBeenUpdated = false;
        if (recents.size() > 0) {
            Date today = Calendar.getInstance().getTime();
            Date lastUpdate = recents.get(0).getUpdatedAt();
            int days = (int) (today.getTime() - lastUpdate.getTime()) / (1000 * 60 * 60 * 24);

            if (days < 1) {
                hasBeenUpdated = true;
            }
        }

        return hasBeenUpdated;
    }

    public static void updateSingleRecent(final Context context, Object callback, final int sourceType, final Recents recent) {
        // Hold onto the context as the listener for the callback
        final TeamRecentUpdateData listener = (TeamRecentUpdateData) callback;

        // Set up the instance of the database if needed
        if (mDb == null) {
            mDb = NFLCrimewatchDatabase.getInstance(context);
        }

        // Run the requests to check the existing data records for the given recent event to see if
        // there is a match. Update if so. Delete old data and insert new if not.
        final Boolean isTeamSource = sourceType == context.getResources().getInteger(R.integer.source_type_team);
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                Date newUpdate = Calendar.getInstance().getTime();
                Recents ntr = recent;
                ntr.setUpdatedAt(newUpdate);
                // There may be occurrences from a single type on a given date. Get the records
                // for the appropriate type for checking.
                List<Recents> ltr;
                if (sourceType == context.getResources().getInteger(R.integer.source_type_team)) {
                    ltr = mDb.recentsDao().checkTeamDateOccurrences(
                            ntr.getTeam(),
                            DateConverter.toString(ntr.getDate()),
                            sourceType
                    );
                } else if (sourceType == context.getResources().getInteger(R.integer.source_type_position)) {
                    ltr = mDb.recentsDao().checkPositionDateOccurrences(
                            ntr.getPlayerPosition(),
                            DateConverter.toString(ntr.getDate()),
                            sourceType
                    );
                } else if (sourceType == context.getResources().getInteger(R.integer.source_type_crime)) {
                    ltr = mDb.recentsDao().checkCategoryDateOccurrences(
                            ntr.getCategory(),
                            DateConverter.toString(ntr.getDate())
                    );
                } else {
                    Log.d(TAG, "run: sourceType not recognized " + sourceType);
                    return;
                }

                // If there isn't anything in the list then add the new occurrence and we're done
                if (ltr.size() < 1)
                {
                    if (ntr != null) {
                        mDb.recentsDao().insertRecent(ntr);

                        listener.onRecentDataUpdated(ntr);
                        return;
                    }
                }

                // If the first occurrence doesn't match the date of the one passed in then
                // delete occurrences for the team (only for recent teams data)
                if (isTeamSource && !ltr.get(0).getDate().equals(ntr.getDate())) {
                    mDb.recentsDao().deleteTeamRecentsForTeam(ntr.getTeam());
                    // Add the new occurrence and exit
                    mDb.recentsDao().insertRecent(ntr);

                    listener.onRecentDataUpdated(ntr);
                    return;
                }

                // If it's the same date then check that this occurrence isn't already in the list
                for (Recents tr:ltr
                     ) {
                    // Anything before the date of this occurrence isn't going to be the same occurrence
                    if (tr.getDate().before(ntr.getDate())) break;

                    if(tr.getArrestStatsId() == ntr.getArrestStatsId()) {
                        // We just need to update this particular record
                        tr.setUpdatedAt(ntr.getUpdatedAt());
                        mDb.recentsDao().updateRecent(tr);

                        listener.onRecentDataUpdated(tr);
                        return;
                    }
                }

                // This new occurrence doesn't exist yet so it needs to be added
                mDb.recentsDao().insertRecent(ntr);
                listener.onRecentDataUpdated(ntr);
            }
        });
    }

    public interface TeamRecentUpdateData {
        void onRecentDataUpdated(Recents teamRecent);
    }
}
