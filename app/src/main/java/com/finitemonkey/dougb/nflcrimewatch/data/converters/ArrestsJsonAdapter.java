package com.finitemonkey.dougb.nflcrimewatch.data.converters;

import android.support.annotation.Nullable;

import com.finitemonkey.dougb.nflcrimewatch.data.tables.Arrests;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.TeamRecents;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.util.Calendar;
import java.util.Date;

public class ArrestsJsonAdapter {
    private static final String TAG = ArrestsJsonAdapter.class.getSimpleName();

    @FromJson
    Arrests arrestsFromJson(ArrestsJson aj) {
        Date date = DateConverter.toDate(aj.Date);

        Arrests arrests = new Arrests(aj.arrest_stats_id, date, aj.Team, 0,
                                          aj.Team_name, aj.Team_preffered_name,
                                          aj.Team_city, aj.Team_Conference,
                                          aj.Team_Division,
                                          aj.Team_hex_color, aj.Team_hex_alt_color,
                                          aj.Name,
                                          aj.Position, aj.Position_name,
                                          aj.Position_type,
                                          aj.Encounter, aj.Category,
                                          aj.Crime_category_color,
                                          aj.Description, aj.Outcome,
                                          Calendar.getInstance().getTime()
        );

        return arrests;
    }

    @FromJson
    public int intFromJson(@Nullable Integer value) {
        if (value == null) {
            return 0;
        }
        return value;
    }

    @ToJson ArrestsJson arrestsToJson(Arrests ar) {
        ArrestsJson aj = new ArrestsJson();

        aj.arrest_stats_id = ar.getArrestStatsId();
        aj.Date = DateConverter.toString(ar.getDate());
        aj.Team = ar.getTeam();
        aj.Team_name = ar.getTeamName();
        aj.Team_preffered_name = ar.getTeamPreferredName();
        aj.Team_city = ar.getTeamCity();
        aj.Team_Conference = ar.getTeamConference();
        aj.Team_Division = ar.getTeamDivision();
        aj.Team_hex_color = ar.getTeamHexColor();
        aj.Team_hex_alt_color = ar.getTeamHexAltColor();
        aj.Name = ar.getPlayerName();
        aj.Position = ar.getPlayerPosition();
        aj.Position_name = ar.getPlayerPositionName();
        aj.Position_type = ar.getPlayerPositionType();
        aj.Encounter = ar.getEncounter();
        aj.Category = ar.getCategory();
        aj.Crime_category_color = ar.getCrimeCategoryColor();
        aj.Description = ar.getDescription();
        aj.Outcome = ar.getResolution();

        return aj;
    }
}
