package com.finitemonkey.dougb.nflcrimewatch.data.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.finitemonkey.dougb.nflcrimewatch.data.NFLCrimewatchDatabase;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Arrests;

import java.util.List;

public class PlayerArrestsViewModel extends AndroidViewModel {
    public static final String TAG = PlayerArrestsViewModel.class.getSimpleName();

    private LiveData<List<Arrests>> mArrests;

    public PlayerArrestsViewModel(@NonNull Application application, String player) {
        super(application);
        NFLCrimewatchDatabase db = NFLCrimewatchDatabase.getInstance(this.getApplication());
        mArrests = db.arrestsDao().loadPlayerArrests(player);
    }

    public LiveData<List<Arrests>> getPlayerArrests() {return mArrests;}
}
