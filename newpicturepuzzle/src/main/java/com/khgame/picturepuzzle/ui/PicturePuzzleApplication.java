package com.khgame.picturepuzzle.ui;

import com.khgame.picturepuzzle.common.ApplicationDelegate;
import com.khgame.picturepuzzle.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class PicturePuzzleApplication extends DaggerApplication {
    @Inject
    ApplicationDelegate applicationDelegate;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationDelegate.onCreate();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}
