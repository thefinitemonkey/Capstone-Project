package com.finitemonkey.dougb.nflcrimewatch.data.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.finitemonkey.dougb.nflcrimewatch.data.tables.Stadiums;

import java.util.List;

@Dao
public interface StadiumsDao {
    @Query("SELECT * FROM stadiums ORDER BY teamID")
    LiveData<List<Stadiums>> loadStadiums();

    @Insert void insertAll(Stadiums... stadiums);
}
