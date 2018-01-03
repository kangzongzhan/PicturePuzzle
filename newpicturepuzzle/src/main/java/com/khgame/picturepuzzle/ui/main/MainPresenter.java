package com.khgame.picturepuzzle.ui.main;


import com.khgame.picturepuzzle.di.ActivityScoped;

import javax.inject.Inject;

@ActivityScoped
public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;

    @Inject
    public MainPresenter() {
    }

    @Override
    public void takeView(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view = null;
    }
}
