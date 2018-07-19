package com.finitemonkey.dougb.nflcrimewatch.data.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.finitemonkey.dougb.nflcrimewatch.data.placeholders.Crimes;
import com.finitemonkey.dougb.nflcrimewatch.data.placeholders.Positions;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Arrests;

import java.util.List;

@Dao
public interface ArrestsDao {
    @Query("SELECT * FROM arrests ORDER BY date DESC")
    LiveData<List<Arrests>> loadArrests();

    @Insert
    void insertArrest(Arrests arrest);

    @Insert
    void insertAll(Arrests... arrests);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateArrest(Arrests arrest);

    @Delete
    void deleteArrest(Arrests arrest);

    @Query("DELETE FROM arrests")
    void deleteAllArrests();

    @Query("SELECT * FROM arrests WHERE arrestStatsId=:arrestId")
    List<Arrests> loadSpecificArrestId(int arrestId);

    @Query("SELECT * FROM arrests WHERE team=:teamId ORDER BY date DESC")
    LiveData<List<Arrests>> loadTeamArrests(String teamId);

    @Query("SELECT * FROM arrests WHERE date = (SELECT max(date) from arrests as a " +
            "WHERE a.team=arrests.team) ORDER BY date DESC")
    LiveData<List<Arrests>> loadRecentTeamArrests();

    @Query("SELECT * FROM arrests WHERE playerPosition=:position ORDER BY date DESC")
    LiveData<List<Arrests>> loadPositionArrests(String position);

    @Query("SELECT * FROM arrests WHERE date = (SELECT max(date) from arrests as a " +
            "WHERE a.playerPosition=arrests.playerPosition) ORDER BY date DESC")
    LiveData<List<Arrests>> loadRecentPositionArrests();

    @Query("SELECT DISTINCT playerPosition, playerPositionName, playerPositionType, Count(arrestStatsId) AS arrestCount FROM arrests " +
            "GROUP BY playerPosition ORDER BY arrestCount DESC")
    LiveData<List<Positions>> loadPositionArrestCounts();

    @Query("SELECT * FROM arrests WHERE encounter=:crimeId ORDER BY date DESC")
    LiveData<List<Arrests>> loadCrimeArrests(String crimeId);

    @Query("SELECT * FROM arrests WHERE date = (SELECT max(date) FROM arrests as a " +
            "WHERE a.encounter=arrests.encounter) ORDER BY date DESC")
    LiveData<List<Arrests>> loadRecentCrimeArrests();

    @Query("SELECT DISTINCT encounter, category, crimeCategoryColor, Count(arrestStatsId) AS arrestCount FROM arrests " +
            "GROUP BY encounter ORDER BY arrestCount DESC")
    LiveData<List<Crimes>> loadCrimeArrestCounts();

    @Query("DELETE FROM arrests WHERE team=:team")
    int deleteTeamArrests(String team);

    @Query("DELETE FROM arrests WHERE playerPosition=:position")
    int deletePositionArrests(String position);

    @Query("DELETE FROM arrests WHERE encounter=:crimeId")
    int deleteCrimeArrests(String crimeId);

    @Query("SELECT * FROM arrests WHERE encounter=:encounter ORDER BY date DESC")
    LiveData<List<Arrests>> loadEncounterArrests(String encounter);


}
