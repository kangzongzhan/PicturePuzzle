package com.khgame.picturepuzzle.ui.splash;

import android.os.Bundle;

import com.khgame.picturepuzzle.R;
import com.khgame.picturepuzzle.ui.base.BaseDaggerActivity;

import javax.inject.Inject;

import dagger.Lazy;

public class SplashActivity extends BaseDaggerActivity {

    @Inject
    SplashPresenter splashPresenter;
    @Inject
    Lazy<SplashFragment> splashFragmentProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SplashFragment splashFragment =
                (SplashFragment) getSupportFragmentManager().findFragmentById(R.id.main_content);
        if (splashFragment == null) {
            // Get the fragment from dagger
            splashFragment = splashFragmentProvider.get();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_content, splashFragment).commit();
        }
    }

}
