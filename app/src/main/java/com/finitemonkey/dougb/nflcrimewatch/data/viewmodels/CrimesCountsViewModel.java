package com.finitemonkey.dougb.nflcrimewatch.data.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.finitemonkey.dougb.nflcrimewatch.data.NFLCrimewatchDatabase;
import com.finitemonkey.dougb.nflcrimewatch.data.proxies.Crimes;

import java.util.List;

public class CrimesCountsViewModel extends AndroidViewModel {
    public static final String TAG = CrimesCountsViewModel.class.getSimpleName();

    private LiveData<List<Crimes>> mCrimeRecents;

    public CrimesCountsViewModel(@NonNull Application application) {
        super(application);
        NFLCrimewatchDatabase db = NFLCrimewatchDatabase.getInstance(this.getApplication());
        mCrimeRecents = db.arrestsDao().loadCrimeArrestCounts();
    }

    public LiveData<List<Crimes>> getCrimesCounts() {return mCrimeRecents;}
}
