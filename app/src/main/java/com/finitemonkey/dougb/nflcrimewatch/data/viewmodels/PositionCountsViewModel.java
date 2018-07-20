package com.finitemonkey.dougb.nflcrimewatch.data.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.finitemonkey.dougb.nflcrimewatch.data.NFLCrimewatchDatabase;
import com.finitemonkey.dougb.nflcrimewatch.data.proxies.Positions;

import java.util.List;

public class PositionCountsViewModel extends AndroidViewModel {
    public static final String TAG = PositionCountsViewModel.class.getSimpleName();

    private LiveData<List<Positions>> mPositionRecents;

    public PositionCountsViewModel(@NonNull Application application) {
        super(application);
        NFLCrimewatchDatabase db = NFLCrimewatchDatabase.getInstance(this.getApplication());
        mPositionRecents = db.arrestsDao().loadPositionArrestCounts();
    }

    public LiveData<List<Positions>> getPositionCounts() {return mPositionRecents;}
}
