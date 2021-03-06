package com.finitemonkey.dougb.nflcrimewatch.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
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
import com.finitemonkey.dougb.nflcrimewatch.data.daos.ArrestsDao;
import com.finitemonkey.dougb.nflcrimewatch.data.daos.RecentsDao;
import com.finitemonkey.dougb.nflcrimewatch.data.daos.StadiumsDao;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Arrests;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Stadiums;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Recents;
import com.finitemonkey.dougb.nflcrimewatch.utils.AppExecutors;

@Database(entities = {Stadiums.class, Recents.class, Arrests.class}, version = 2, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class NFLCrimewatchDatabase extends RoomDatabase {
    private static final String TAG = NFLCrimewatchDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "nflcrimewatch";
    private static NFLCrimewatchDatabase sInstance;


    public static NFLCrimewatchDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "getInstance: Creating new instance of database");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                                                 NFLCrimewatchDatabase.class,
                                                 NFLCrimewatchDatabase.DATABASE_NAME
                ).addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Log.d(TAG, "onCreate: NFLCrimewatchDatabase created");
                        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                getInstance(context).stadiumsDao().insertAll(Stadiums.populateStadiums());
                                Log.d(TAG, "run: stadiums inserted into database");
                            }
                        });
                    }
                }).build();
            }
        }
        Log.d(TAG, "getInstance: Getting database instance");
        return sInstance;
    }

    public abstract RecentsDao recentsDao();

    public abstract StadiumsDao stadiumsDao();

    public abstract ArrestsDao arrestsDao();


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
