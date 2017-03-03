package com.khgame.picturepuzzle.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by zkang on 2017/2/26.
 */

public class SquaredActivity extends AppCompatActivity {

    protected EventBus bus = EventBus.getDefault();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!bus.isRegistered(this)) {
            bus.register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bus.isRegistered(this)) {
            bus.unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    @SuppressWarnings("unused") // invoked by event bus
    public void onEvent(String event) {
        // do nothing
        // if a sub class who has no subscriber when inject itself will get exception
    }
}
