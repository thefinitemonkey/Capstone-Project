package com.finitemonkey.dougb.nflcrimewatch.data.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.finitemonkey.dougb.nflcrimewatch.data.NFLCrimewatchDatabase;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Arrests;

import java.util.List;

public class PositionArrestsViewModel extends AndroidViewModel {
    public static final String TAG = PositionArrestsViewModel.class.getSimpleName();

    private LiveData<List<Arrests>> mArrests;

    public PositionArrestsViewModel(@NonNull Application application, String position) {
        super(application);
        NFLCrimewatchDatabase db = NFLCrimewatchDatabase.getInstance(this.getApplication());
        mArrests = db.arrestsDao().loadPositionArrests(position);
    }

    public LiveData<List<Arrests>> getPositionArrests() {return mArrests;}
}
