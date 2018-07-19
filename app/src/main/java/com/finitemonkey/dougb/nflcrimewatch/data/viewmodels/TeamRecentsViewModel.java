package com.finitemonkey.dougb.nflcrimewatch.data.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.finitemonkey.dougb.nflcrimewatch.data.NFLCrimewatchDatabase;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Arrests;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Recents;

import java.util.List;

public class TeamRecentsViewModel extends AndroidViewModel {
    public static final String TAG = TeamRecentsViewModel.class.getSimpleName();

    private LiveData<List<Arrests>> mTeamRecents;

    public TeamRecentsViewModel(@NonNull Application application) {
        super(application);
        NFLCrimewatchDatabase db = NFLCrimewatchDatabase.getInstance(this.getApplication());
        mTeamRecents = db.arrestsDao().loadRecentTeamArrests();
    }

    public LiveData<List<Arrests>> getTeamRecents() {return mTeamRecents;}
}
