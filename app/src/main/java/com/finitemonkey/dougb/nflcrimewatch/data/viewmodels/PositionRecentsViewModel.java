package com.finitemonkey.dougb.nflcrimewatch.data.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.finitemonkey.dougb.nflcrimewatch.data.NFLCrimewatchDatabase;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Recents;

import java.util.List;

public class PositionRecentsViewModel extends AndroidViewModel {
    public static final String TAG = PositionRecentsViewModel.class.getSimpleName();

    private LiveData<List<Recents>> mPositionRecents;

    public PositionRecentsViewModel(@NonNull Application application) {
        super(application);
        NFLCrimewatchDatabase db = NFLCrimewatchDatabase.getInstance(this.getApplication());
        mPositionRecents = db.recentsDao().loadTeamRecents();
    }

    public LiveData<List<Recents>> getPositionRecents() {return mPositionRecents;}
}
