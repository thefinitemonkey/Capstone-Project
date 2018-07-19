package com.finitemonkey.dougb.nflcrimewatch.data.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.finitemonkey.dougb.nflcrimewatch.data.NFLCrimewatchDatabase;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Arrests;

import java.util.List;

public class CrimesArrestsViewModel extends AndroidViewModel {
    public static final String TAG = CrimesArrestsViewModel.class.getSimpleName();

    private LiveData<List<Arrests>> mArrests;

    public CrimesArrestsViewModel(@NonNull Application application, String crime) {
        super(application);
        NFLCrimewatchDatabase db = NFLCrimewatchDatabase.getInstance(this.getApplication());
        mArrests = db.arrestsDao().loadCrimeArrests(crime);
    }

    public LiveData<List<Arrests>> getPositionArrests() {return mArrests;}
}
