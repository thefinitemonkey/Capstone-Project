package com.finitemonkey.dougb.nflcrimewatch.data.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.finitemonkey.dougb.nflcrimewatch.data.NFLCrimewatchDatabase;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Arrests;

import java.util.List;

public class CrimesRecentsViewModel extends AndroidViewModel {
    public static final String TAG = CrimesRecentsViewModel.class.getSimpleName();

    private LiveData<List<Arrests>> mCrimeRecents;

    public CrimesRecentsViewModel(@NonNull Application application) {
        super(application);
        NFLCrimewatchDatabase db = NFLCrimewatchDatabase.getInstance(this.getApplication());
        mCrimeRecents = db.arrestsDao().loadRecentCrimeArrests();
    }

    public LiveData<List<Arrests>> getCrimeRecents() {return mCrimeRecents;}
}
