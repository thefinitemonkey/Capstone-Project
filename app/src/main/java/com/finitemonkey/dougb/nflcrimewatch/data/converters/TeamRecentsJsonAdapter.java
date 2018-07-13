package com.finitemonkey.dougb.nflcrimewatch.data.converters;

import android.util.Log;

import com.finitemonkey.dougb.nflcrimewatch.data.tables.TeamRecents;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.util.Calendar;
import java.util.Date;

public class TeamRecentsJsonAdapter {
    private static final String TAG = TeamRecentsJsonAdapter.class.getSimpleName();

    @FromJson
    TeamRecents teamRecentsFromJson(TeamRecentsJson trj) {
        Date date = DateConverter.toDate(trj.Date);

        TeamRecents teamRecents = new TeamRecents(trj.arrest_stats_id, date, trj.Team, 0,
                                                  trj.Team_Name, trj.Team_Preferred_Name,
                                                  trj.Team_City, trj.Team_Conference,
                                                  trj.Team_Division,
                                                  trj.Team_hex_color, trj.Team_hex_alt_color,
                                                  trj.Name,
                                                  trj.Position, trj.Position_name,
                                                  trj.Position_type,
                                                  trj.Encounter, trj.Category,
                                                  trj.Crime_category_color,
                                                  trj.Description, trj.Resolution,
                                                  trj.DaysToLastTeamArrest,
                                                  Calendar.getInstance().getTime()
        );
        Log.d(TAG, "teamRecentsFromJson: value of teamRecents.updatedAt is " + teamRecents.getUpdatedAt());

        return teamRecents;
    }

    @ToJson TeamRecentsJson teamRecentsToJson(TeamRecents tr) {
        TeamRecentsJson tj = new TeamRecentsJson();

        tj.arrest_stats_id = tr.getArrestStatsId();
        tj.Date = DateConverter.toString(tr.getDate());
        tj.Team = tr.getTeam();
        tj.Team_Name = tr.getTeamName();
        tj.Team_Preferred_Name = tr.getTeamPreferredName();
        tj.Team_City = tr.getTeamCity();
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
        tj.Resolution = tr.getResolution();
        tj.DaysToLastTeamArrest = tr.getDaysToLastTeamArrest();

        return tj;
    }
}
