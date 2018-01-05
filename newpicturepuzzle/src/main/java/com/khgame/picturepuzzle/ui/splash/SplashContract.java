package com.khgame.picturepuzzle.ui.splash;

import com.khgame.picturepuzzle.ui.base.BasePresenter;
import com.khgame.picturepuzzle.ui.base.BaseView;

public interface SplashContract {
    interface View extends BaseView<Presenter> {
        void showLogo();
        void showMainActivity();
    }
    interface Presenter extends BasePresenter<View> {
    }
}
