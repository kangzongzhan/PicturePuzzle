package com.khgame.picturepuzzle2;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.khgame.picturepuzzle.base.SquaredActivity;
import com.khgame.picturepuzzle.model.Serial;
import com.khgame.picturepuzzle.serial.SerialManager;
import com.khgame.picturepuzzle.serial.SerialManagerImpl;
import com.khgame.picturepuzzle.serial.SerialPicturesLoadFinishEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by zkang on 2017/2/26.
 */

public class SerialListActivity extends SquaredActivity {

    private Serial serial;
    private SerialManager serialManager = SerialManagerImpl.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String uuid = getIntent().getStringExtra("uuid");
        serial = serialManager.getSerialByUuid(uuid);
        serialManager.startSerial(serial);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @SuppressWarnings("unused") // invoked by event bus
    public void onEventMainThread(SerialPicturesLoadFinishEvent event) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serialManager.endSerial();
    }
}
