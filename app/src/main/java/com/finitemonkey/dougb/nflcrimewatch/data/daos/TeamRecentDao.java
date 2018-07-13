package com.finitemonkey.dougb.nflcrimewatch.data.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.finitemonkey.dougb.nflcrimewatch.data.tables.TeamRecents;

import java.util.List;

@Dao
public interface TeamRecentDao {
    @Query("SELECT * FROM team_recents ORDER BY date")
    LiveData<List<TeamRecents>> loadTeamRecents();

    @Query("SELECT * FROM team_recents WHERE team=:teamId ORDER BY date")
    List<TeamRecents> loadSpecificTeamRecents(String teamId);

    @Query("SELECT * FROM team_recents WHERE team=:teamId AND date=:date ORDER BY date")
    List<TeamRecents> checkTeamDateOccurrences(String teamId, String date);

    @Query("DELETE FROM team_recents")
    void deleteAllTeamRecents();

    @Query("DELETE FROM team_recents WHERE team=:teamId")
    void deleteTeamRecentsForTeam(String teamId);

    @Insert
    void insertTeamRecent(TeamRecents teamRecent);

    @Insert
    void insertAll(TeamRecents... teamRecents);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTeamRecent(TeamRecents teamRecent);

    @Delete
    void deleteTeamRecent(TeamRecents teamRecent);
}
