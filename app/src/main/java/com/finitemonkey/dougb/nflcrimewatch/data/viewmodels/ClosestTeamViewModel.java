package com.finitemonkey.dougb.nflcrimewatch.data.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.finitemonkey.dougb.nflcrimewatch.data.NFLCrimewatchDatabase;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Stadiums;

import java.util.List;

public class ClosestTeamViewModel extends AndroidViewModel {
    private static final String TAG = ClosestTeamViewModel.class.getSimpleName();

    private LiveData<List<Stadiums>> mStadiums;

    public ClosestTeamViewModel(@NonNull Application application) {
        super(application);
        NFLCrimewatchDatabase db = NFLCrimewatchDatabase.getInstance(this.getApplication());
        mStadiums = db.stadiumsDao().loadStadiums();
    }

    public LiveData<List<Stadiums>> getStadiums() {return mStadiums;}
}
