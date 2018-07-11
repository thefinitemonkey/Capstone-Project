package com.finitemonkey.dougb.nflcrimewatch.data;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.finitemonkey.dougb.nflcrimewatch.data.converters.DateConverter;

@Database(entities = {TeamRecents.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public class NFLCrimewatchDatabase extends RoomDatabase {
    private static final String TAG = NFLCrimewatchDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "nflcrimewatch";
    private static NFLCrimewatchDatabase sInstance;


    public static NFLCrimewatchDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "getInstance: Creating new instance of database");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                                                 NFLCrimewatchDatabase.class,
                                                 NFLCrimewatchDatabase.DATABASE_NAME
                ).build();
            }
        }
        Log.d(TAG, "getInstance: Getting database instance");
        return sInstance;
    }


    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
