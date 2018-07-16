package com.finitemonkey.dougb.nflcrimewatch.data.converters;

import com.finitemonkey.dougb.nflcrimewatch.data.tables.Recents;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.util.Calendar;
import java.util.Date;

public class RecentsJsonAdapter {
    private static final String TAG = RecentsJsonAdapter.class.getSimpleName();

    @FromJson
    Recents teamRecentsFromJson(RecentsJson trj) {
        Date date = DateConverter.toDate(trj.Date);

        Recents recents = new Recents(trj.arrest_stats_id, date, trj.Team, 0,
                                      trj.Team_name, trj.Team_preffered_name,
                                      trj.Team_city, trj.Team_Conference,
                                      trj.Team_Division,
                                      trj.Team_hex_color, trj.Team_hex_alt_color,
                                      trj.Name,
                                      trj.Position, trj.Position_name,
                                      trj.Position_type,
                                      trj.Encounter, trj.Category,
                                      trj.Crime_category_color,
                                      trj.Description, trj.Outcome,
                                      trj.DaysToLastTeamArrest,
                                      -1,
                                      Calendar.getInstance().getTime()
        );

        return recents;
    }

    @ToJson
    RecentsJson teamRecentsToJson(Recents tr) {
        RecentsJson tj = new RecentsJson();

        tj.arrest_stats_id = tr.getArrestStatsId();
        tj.Date = DateConverter.toString(tr.getDate());
        tj.Team = tr.getTeam();
        tj.Team_name = tr.getTeamName();
        tj.Team_preffered_name = tr.getTeamPreferredName();
        tj.Team_city = tr.getTeamCity();
        tj.Team_Conference = tr.getTeamConference();
        tj.Team_Division = tr.getTeamDivision();
        tj.Team_hex_color = tr.getTeamHexColor();
        tj.Team_hex_alt_color = tr.getTeamHexAltColor();
        tj.Name = tr.getPlayerName();
        tj.Position = tr.getPlayerPosition();
        tj.Position_name = tr.getPlayerPositionName();
        tj.Position_type = tr.getPlayerPositionType();
        tj.Encounter = tr.getEncounter();
        tj.Category = tr.getCategory();
        tj.Crime_category_color = tr.getCrimeCategoryColor();
        tj.Description = tr.getDescription();
        tj.Outcome = tr.getResolution();
        tj.DaysToLastTeamArrest = tr.getDaysToLastTeamArrest();

        return tj;
    }
}
