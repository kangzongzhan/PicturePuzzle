package com.khgame.picturepuzzle.ui.splash;

import android.support.annotation.Nullable;

import com.khgame.picturepuzzle.di.ActivityScoped;

import javax.inject.Inject;

@ActivityScoped
public class SplashPresenter implements SplashContract.Presenter {
    @Nullable
    private SplashContract.View splashView;

    @Inject
    public SplashPresenter() {
    }

    @Override
    public void takeView(SplashContract.View view) {
        splashView = view;
        splashView.showLogo();
    }

    @Override
    public void dropView() {
        this.splashView = null;
    }
}
