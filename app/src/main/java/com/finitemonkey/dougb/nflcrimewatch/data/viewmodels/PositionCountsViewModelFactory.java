package com.finitemonkey.dougb.nflcrimewatch.data.viewmodels;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class PositionCountsViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;
    private int mParam;

    public PositionCountsViewModelFactory(Application application, int param) {
        mApplication = application;
        mParam = param;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PositionCountsViewModel(mApplication, mParam);
    }
}
