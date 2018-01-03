package com.khgame.picturepuzzle.ui.main.classic;


import com.khgame.picturepuzzle.di.ActivityScoped;

import javax.inject.Inject;

@ActivityScoped
public class ClassicPresenter implements ClassicContract.Presenter {

    private ClassicContract.View view;

    @Inject
    public ClassicPresenter() {
    }

    @Override
    public void takeView(ClassicContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {

    }
}
