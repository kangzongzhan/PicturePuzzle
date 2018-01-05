package com.khgame.picturepuzzle.ui.main;

import com.khgame.picturepuzzle.di.ActivityScoped;
import com.khgame.picturepuzzle.di.FragmentScoped;
import com.khgame.picturepuzzle.ui.main.classic.ClassicModule;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MainFragment mainFragment();

    @ActivityScoped
    @Binds
    abstract MainContract.Presenter mainPresenter(MainPresenter presenter);

}
