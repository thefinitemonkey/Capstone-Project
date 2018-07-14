package com.finitemonkey.dougb.nflcrimewatch.data.viewmodels;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class TeamArrestsViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;
    private String mParam;

    public TeamArrestsViewModelFactory(Application application, String param) {
        mApplication = application;
        mParam = param;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TeamArrestsViewModel(mApplication, mParam);
    }
}
