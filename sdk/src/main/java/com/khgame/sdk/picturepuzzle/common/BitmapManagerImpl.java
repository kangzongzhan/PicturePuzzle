package com.khgame.sdk.picturepuzzle.common;

import android.graphics.Bitmap;

import com.khgame.sdk.picturepuzzle.events.BitmapLoadEvent;
import com.khgame.sdk.picturepuzzle.operation.LoadPictureOperation;
import com.khgame.sdk.picturepuzzle.operation.Operation;
import com.khgame.sdk.picturepuzzle.serial.SerialManager;
import com.khgame.sdk.picturepuzzle.serial.SerialManagerImpl;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zkang on 2017/4/8.
 */

public class BitmapManagerImpl implements BitmapManager {

    private EventBus bus = EventBus.getDefault();

    private static BitmapManagerImpl instance;
    private BitmapManagerImpl(){}
    public static BitmapManager getInstance() {
        synchronized (BitmapManagerImpl.class) {
            if(instance != null) {
                return instance;
            }
            instance = new BitmapManagerImpl();
        }
        return instance;
    }

    @Override
    public void loadBitmapByUuid(final String uuid) {
        new LoadPictureOperation(uuid).callback(new Operation.Callback<Bitmap, Void>() {
            @Override
            public void onSuccessMainThread(Bitmap bitmap) {
                BitmapLoadEvent event = new BitmapLoadEvent(Result.Success);
                event.uuid = uuid;
                event.bitmap = bitmap;
                bus.post(event);
            }
        }).enqueue();
    }
}
