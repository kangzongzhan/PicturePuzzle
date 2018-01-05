package com.khgame.picturepuzzle.ui.main;


import com.khgame.picturepuzzle.ui.base.BasePresenter;
import com.khgame.picturepuzzle.ui.base.BaseView;

public interface MainContract {
    interface View extends BaseView<MainContract.Presenter> {
    }
    interface Presenter extends BasePresenter<MainContract.View> {
    }
}
