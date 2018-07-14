package com.finitemonkey.dougb.nflcrimewatch.data.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.finitemonkey.dougb.nflcrimewatch.data.tables.Arrests;

import java.util.List;

@Dao
public interface ArrestsDao {
    @Query("SELECT * FROM arrests ORDER BY date DESC")
    LiveData<List<Arrests>> loadArrests();

    @Insert
    void insertArrest(Arrests arrest);

    @Insert
    void insertAll(Arrests... arrest);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateArrest(Arrests arrest);

    @Delete
    void deleteArrest(Arrests arrest);

    @Query("DELETE FROM arrests")
    void deleteAllArrests();

    @Query("SELECT * FROM arrests WHERE team=:teamId ORDER BY date DESC")
    LiveData<List<Arrests>> loadTeamArrests(String teamId);

    @Query("SELECT * FROM arrests WHERE team=:teamId AND date=:date ORDER BY date DESC")
    List<Arrests> checkTeamDateArrests(String teamId, String date);

    @Query("DELETE FROM arrests WHERE team=:teamId")
    void deleteArrestsForTeam(String teamId);

    @Query("SELECT * FROM arrests WHERE playerPosition=:position ORDER BY date DESC")
    LiveData<List<Arrests>> loadPositionArrests(String position);

    @Query("SELECT * FROM arrests WHERE playerPosition=:position AND date=:date ORDER BY date DESC")
    List<Arrests> checkPositionDateArrests(String position, String date);

    @Query("DELETE FROM arrests WHERE team=:position")
    void deleteArrestsForPosition(String position);

    @Query("SELECT * FROM arrests WHERE encounter=:encounter ORDER BY date DESC")
    LiveData<List<Arrests>> loadEncounterArrests(String encounter);

    @Query("SELECT * FROM arrests WHERE encounter=:encounter AND date=:date ORDER BY date DESC")
    List<Arrests> checkEncounterDateArrests(String encounter, String date);

    @Query("DELETE FROM arrests WHERE team=:encounter")
    void deleteArrestsForEncounter(String encounter);

}
