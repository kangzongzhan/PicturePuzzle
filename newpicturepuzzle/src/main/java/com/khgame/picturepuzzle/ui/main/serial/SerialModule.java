package com.khgame.picturepuzzle.ui.main.serial;


import com.khgame.picturepuzzle.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SerialModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract SerialFragment mainFragment();

    @FragmentScoped
    @Binds
    abstract SerialContract.Presenter classicPresenter(SerialPresenter classicPresenter);
}
