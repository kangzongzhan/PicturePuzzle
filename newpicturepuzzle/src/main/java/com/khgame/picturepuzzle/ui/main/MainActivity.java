package com.khgame.picturepuzzle.ui.main;

import android.os.Bundle;

import com.khgame.picturepuzzle.R;
import com.khgame.picturepuzzle.ui.BaseActivity;

import javax.inject.Inject;

import dagger.Lazy;

public class MainActivity extends BaseActivity {

    private static final String MAIN_FRAGMENT = "MAIN_FRAGMENT";

    @Inject
    MainPresenter mainPresenter;

    @Inject
    Lazy<MainFragment> mainFragmentProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(MAIN_FRAGMENT);

        if(mainFragment == null) {
            mainFragment = mainFragmentProvider.get();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_content, mainFragment).commit();
        }

    }
}
