package com.khgame.picturepuzzle.ui.main.classic;


import com.khgame.picturepuzzle.di.FragmentScoped;
import com.khgame.picturepuzzle.ui.main.MainPresenter;

import dagger.Binds;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ClassicModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract ClassicFragment mainFragment();

    @FragmentScoped
    @Binds
    abstract ClassicContract.Presenter classicPresenter(ClassicPresenter classicPresenter);
}
