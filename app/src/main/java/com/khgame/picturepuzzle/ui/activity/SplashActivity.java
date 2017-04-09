package com.khgame.picturepuzzle.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.khgame.sdk.picturepuzzle.base.SquaredActivity;
import com.khgame.sdk.picturepuzzle.classic.ClassicPictureManager;
import com.khgame.sdk.picturepuzzle.classic.ClassicPictureManagerImpl;
import com.khgame.sdk.picturepuzzle.classic.InitClassicPictureFinishEvent;
import com.khgame.sdk.picturepuzzle.common.SettingManager;
import com.khgame.picturepuzzle.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Kisha Deng on 2/27/2017.
 */

public class SplashActivity extends SquaredActivity {

    private ClassicPictureManager classicManager = ClassicPictureManagerImpl.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int openTimes = SettingManager.Instance().getInt("OpenTimes", 0);

        if(openTimes != 1) {
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        classicManager.initialize();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)

    @SuppressWarnings("unused") // invoked by event bus
    public void onEventMainThread(InitClassicPictureFinishEvent event) {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
