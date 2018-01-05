package com.khgame.picturepuzzle.ui.main.serial;


import com.khgame.picturepuzzle.ui.base.BasePresenter;
import com.khgame.picturepuzzle.ui.base.BaseView;

public interface SerialContract {
    interface View extends BaseView<SerialContract.Presenter> {
    }
    interface Presenter extends BasePresenter<SerialContract.View> {
    }
}
