package com.finitemonkey.dougb.nflcrimewatch.data.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.finitemonkey.dougb.nflcrimewatch.data.NFLCrimewatchDatabase;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.TeamRecents;

import java.util.List;

public class TeamRecentsViewModel extends AndroidViewModel {
    public static final String TAG = TeamRecentsViewModel.class.getSimpleName();

    private LiveData<List<TeamRecents>> mTeamRecents;

    public TeamRecentsViewModel(@NonNull Application application) {
        super(application);
        NFLCrimewatchDatabase db = NFLCrimewatchDatabase.getInstance(this.getApplication());
        mTeamRecents = db.teamRecentDao().loadTeamRecents();
    }

    public LiveData<List<TeamRecents>> getTeamRecents() {return mTeamRecents;}
}
