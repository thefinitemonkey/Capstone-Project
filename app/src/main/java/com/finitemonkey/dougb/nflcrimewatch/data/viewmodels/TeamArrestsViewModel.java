package com.finitemonkey.dougb.nflcrimewatch.data.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.finitemonkey.dougb.nflcrimewatch.data.NFLCrimewatchDatabase;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Arrests;

import java.util.List;

public class TeamArrestsViewModel extends AndroidViewModel {
    public static final String TAG = TeamArrestsViewModel.class.getSimpleName();

    private LiveData<List<Arrests>> mArrests;

    public TeamArrestsViewModel(@NonNull Application application, String teamId) {
        super(application);
        NFLCrimewatchDatabase db = NFLCrimewatchDatabase.getInstance(this.getApplication());
        mArrests = db.arrestsDao().loadTeamArrests(teamId);
    }

    public LiveData<List<Arrests>> getTeamArrests() {return mArrests;}
}
