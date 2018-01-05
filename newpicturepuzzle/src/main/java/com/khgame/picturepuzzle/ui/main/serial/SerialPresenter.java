package com.khgame.picturepuzzle.ui.main.serial;


import com.khgame.picturepuzzle.di.ActivityScoped;

import javax.inject.Inject;

@ActivityScoped
public class SerialPresenter implements SerialContract.Presenter {

    private SerialContract.View view;

    @Inject
    public SerialPresenter() {
    }

    @Override
    public void takeView(SerialContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {

    }
}
