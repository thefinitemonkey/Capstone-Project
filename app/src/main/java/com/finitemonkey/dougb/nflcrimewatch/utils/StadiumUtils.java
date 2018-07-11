package com.finitemonkey.dougb.nflcrimewatch.utils;

import android.content.Context;

import com.finitemonkey.dougb.nflcrimewatch.data.NFLCrimewatchDatabase;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Stadiums;

import java.util.List;

public class StadiumUtils {
    private static NFLCrimewatchDatabase mDb;

    public static void getClosestTeam(final Context context, final Double lat, final Double lon) {
        // Set the listener object for this request
        final ClosestTeamCalculationResult listener = (ClosestTeamCalculationResult) context;

        // Get database instance if not already available
        if (mDb == null) {
            mDb = NFLCrimewatchDatabase.getInstance(context);
        }

        // Run the request for all stadiums and the calculation for which is closest in a Runnable
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Stadiums> stadiums = mDb.stadiumsDao().loadStadiums();

                // If there are no results then emit a null
                if (stadiums.size() < 1) {
                    listener.onClosestTeamResult(null);
                    return;
                }

                // Iterate over the stadiums to determine which is the closest to the origin point
                String teamId = "";
                Double distance = 100000.0;

                for (Stadiums stadium: stadiums
                     ) {
                    Double calc = Geo.distance(lat, lon, stadium.getLat(), stadium.getLon(), "M");
                    if (calc < distance) {
                        distance = calc;
                        teamId = stadium.getTeamID();
                    }
                }

                // Emit the result
                listener.onClosestTeamResult(teamId);
            }
        });
    }


    // Interface for caller to listen on for response
    public interface ClosestTeamCalculationResult {
        void onClosestTeamResult(String teamId);
    }
}
