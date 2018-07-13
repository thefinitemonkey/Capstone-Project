package com.finitemonkey.dougb.nflcrimewatch.utils;


import com.finitemonkey.dougb.nflcrimewatch.data.tables.Stadiums;

import java.util.List;

public class StadiumUtils {

    public static String getClosestTeam(List<Stadiums> stadiums, final Double lat, final Double lon) {
        // Set the listener object for this request
        //final ClosestTeamCalculationResult listener = (ClosestTeamCalculationResult) context;
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

        return teamId;
    }
}
