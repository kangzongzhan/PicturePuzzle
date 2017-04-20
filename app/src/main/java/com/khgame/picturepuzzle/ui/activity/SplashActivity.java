package com.khgame.picturepuzzle.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

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

    private boolean canFinish = false;

    private static final int CLOSE = 100;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int openTimes = SettingManager.Instance().getInt("OpenTimes", 0);
        Log.d(SplashActivity.class.getSimpleName(), "OpenTimes:" + openTimes);
        if (openTimes == 1) {
            Log.d(this.getClass().getSimpleName(), "Open this app first time, init class picture");
            classicManager.initialize();
            handler.sendEmptyMessageDelayed(CLOSE, 3000);
        } else {
            Log.d(this.getClass().getSimpleName(), "Open this app " + openTimes + " times, start main activity 3s later");
            canFinish = true;
            startMainActivity();
        }
        SettingManager.Instance().setInt("OpenTimes", ++openTimes);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @SuppressWarnings("unused") // invoked by event bus
    public void onEventMainThread(InitClassicPictureFinishEvent event) {
        startMainActivity();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CLOSE) {
                canFinish = true;
                startMainActivity();
            }
        }
    };

    private void startMainActivity() {
        if (canFinish) {
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

}
