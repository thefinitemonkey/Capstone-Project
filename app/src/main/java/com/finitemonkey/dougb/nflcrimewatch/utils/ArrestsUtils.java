package com.finitemonkey.dougb.nflcrimewatch.utils;

import android.content.Context;

import com.finitemonkey.dougb.nflcrimewatch.data.NFLCrimewatchDatabase;
import com.finitemonkey.dougb.nflcrimewatch.data.converters.DateConverter;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Arrests;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.TeamRecents;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ArrestsUtils {
    private static NFLCrimewatchDatabase mDb;
    private static final String TAG = ArrestsUtils.class.getSimpleName();

    // This function does the work to check in the arrests (Arrests) table to
    // verify whether the data is more than a day old
    public static Boolean startCheckUpdatedInPastDay(List<Arrests> arrests) {
        // If there are no results then we've never loaded. Otherwise check
        // to see how long since the last update.'
        Boolean hasBeenUpdated = false;
        if (arrests.size() > 0) {
            Date today = Calendar.getInstance().getTime();
            Date lastUpdate = arrests.get(0).getUpdatedAt();
            int days = (int) (today.getTime() - lastUpdate.getTime()) / (1000 * 60 * 60 * 24);

            if (days < 1) {
                hasBeenUpdated = true;
            }
        }

        return hasBeenUpdated;
    }

    public static void updateSingleArrest(Context context, Object callback, final Arrests arrests) {
        // Hold onto the context as the listener for the callback
        final ArrestUpdateData listener = (ArrestUpdateData) callback;

        // Set up the instance of the database if needed
        if (mDb == null) {
            mDb = NFLCrimewatchDatabase.getInstance(context);
        }

        // Run the requests to check the existing data records for the given arrest to see if
        // there is a match. Update if so. Delete old data and insert new if not.
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                Date newUpdate = Calendar.getInstance().getTime();
                Arrests na = arrests;
                na.setUpdatedAt(newUpdate);

                // Check whether the event already exists in the arrests table
                List<Arrests> la = mDb.arrestsDao().loadSpecificArrestId(na.getArrestStatsId());

                // If there isn't anything in the list then add the new event and we're done
                if (la.size() < 1)
                {
                    if (na != null) {
                        mDb.arrestsDao().insertArrest(na);

                        listener.onArrestDataUpdated(na);
                        return;
                    }
                }

                // Otherwise update the existing event and we're done
                mDb.arrestsDao().updateArrest(na);
                listener.onArrestDataUpdated(na);
            }
        });
    }

    public interface ArrestUpdateData {
        void onArrestDataUpdated(Arrests arrest);
    }
}
