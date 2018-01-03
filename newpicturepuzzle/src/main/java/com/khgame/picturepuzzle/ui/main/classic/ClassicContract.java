package com.khgame.picturepuzzle.ui.main.classic;


import com.khgame.picturepuzzle.ui.BasePresenter;
import com.khgame.picturepuzzle.ui.BaseView;

public interface ClassicContract {
    interface View extends BaseView<ClassicContract.Presenter> {
    }
    interface Presenter extends BasePresenter<ClassicContract.View> {
    }
}
