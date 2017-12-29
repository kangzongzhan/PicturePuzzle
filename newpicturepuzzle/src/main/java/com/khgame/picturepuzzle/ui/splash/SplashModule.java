package com.khgame.picturepuzzle.ui.splash;

import com.khgame.picturepuzzle.di.ActivityScoped;
import com.khgame.picturepuzzle.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SplashModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract SplashFragment splashFragment();

    @ActivityScoped
    @Binds
    abstract SplashContract.Presenter splashPresenter(SplashPresenter presenter);
}
