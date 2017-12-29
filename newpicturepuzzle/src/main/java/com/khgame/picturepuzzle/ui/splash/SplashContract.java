package com.khgame.picturepuzzle.ui.splash;

import com.khgame.picturepuzzle.ui.BasePresenter;
import com.khgame.picturepuzzle.ui.BaseView;

public interface SplashContract {
    interface View extends BaseView<Presenter> {
        void showLogo();
        void showMainActivity();
    }
    interface Presenter extends BasePresenter<View> {
    }
}
