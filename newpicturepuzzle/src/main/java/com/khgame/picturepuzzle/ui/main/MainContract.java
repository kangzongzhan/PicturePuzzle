package com.khgame.picturepuzzle.ui.main;


import com.khgame.picturepuzzle.ui.BasePresenter;
import com.khgame.picturepuzzle.ui.BaseView;

public interface MainContract {
    interface View extends BaseView<MainContract.Presenter> {
    }
    interface Presenter extends BasePresenter<MainContract.View> {
    }
}
