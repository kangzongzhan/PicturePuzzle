package com.khgame.sdk.picturepuzzle.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

/**
 * Created by zkang on 2017/2/26.
 */

public abstract class SquaredFragment extends Fragment{

    protected EventBus bus = EventBus.getDefault();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if(!bus.isRegistered(this)) {
            bus.register(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(bus.isRegistered(this)){
            bus.unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(String s){}
}
