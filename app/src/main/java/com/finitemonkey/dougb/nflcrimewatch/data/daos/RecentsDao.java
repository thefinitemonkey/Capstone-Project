package com.finitemonkey.dougb.nflcrimewatch.data.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.finitemonkey.dougb.nflcrimewatch.data.proxies.Positions;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Recents;

import java.util.List;

@Dao
public interface RecentsDao {
    @Query("SELECT * FROM Recents WHERE sourceType=0 ORDER BY date DESC")
    LiveData<List<Recents>> loadTeamRecents();

    @Query("SELECT * FROM Recents WHERE team=:teamId ORDER BY date DESC")
    List<Recents> loadSpecificTeamRecents(String teamId);

    @Query("SELECT * FROM Recents WHERE team=:teamId AND date=:date AND sourceType=:sourceType ORDER BY date DESC")
    List<Recents> checkTeamDateOccurrences(String teamId, String date, int sourceType);

    @Query("SELECT * FROM Recents WHERE sourceType=1 ORDER BY date DESC")
    LiveData<List<Recents>> loadPositionRecents();

    @Query("SELECT DISTINCT playerPosition, playerPositionName, playerPositionType, " +
            "Count(arrestStatsId) AS arrestCount FROM Recents WHERE sourceType=:sourceType " +
            "GROUP BY playerPosition ORDER BY playerPositionName ASC")
    LiveData<List<Positions>> loadPositionArrestCounts(int sourceType);

    @Query("SELECT * FROM Recents WHERE playerPosition=:playerPosition AND date=:date AND sourceType=:sourceType ORDER BY date DESC")
    List<Recents> checkPositionDateOccurrences(String playerPosition, String date, int sourceType);

    @Query("SELECT * FROM Recents WHERE category=:category AND date=:date ORDER BY date DESC")
    List<Recents> checkCategoryDateOccurrences(String category, String date);

    @Query("DELETE FROM Recents")
    void deleteAllTeamRecents();

    @Query("DELETE FROM Recents WHERE team=:teamId")
    void deleteTeamRecentsForTeam(String teamId);

    @Insert
    void insertRecent(Recents teamRecent);

    @Insert
    void insertAll(Recents... recents);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecent(Recents teamRecent);

    @Delete
    void deleteTeamRecent(Recents teamRecent);
}
