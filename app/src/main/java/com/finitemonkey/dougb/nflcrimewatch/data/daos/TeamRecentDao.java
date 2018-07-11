package com.finitemonkey.dougb.nflcrimewatch.data.daos;

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
    List<TeamRecents> loadTeamRecents();

    @Query("SELECT * FROM team_recents WHERE team=:teamId ORDER BY date")
    List<TeamRecents> loadSpecificTeamRecents(String teamId);

    @Insert
    void insertTeamRecent(TeamRecents teamRecent);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTeamRecent(TeamRecents teamRecent);

    @Delete
    void deleteTeamRecent(TeamRecents teamRecent);
}