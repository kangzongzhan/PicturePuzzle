package com.khgame.sdk.picturepuzzle.serial;

import android.util.Log;

import com.khgame.sdk.picturepuzzle.model.Serial;
import com.khgame.sdk.picturepuzzle.operation.InstallSerialOperation;
import com.khgame.sdk.picturepuzzle.operation.Operation;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zkang on 2017/3/17.
 */

public class SerialInstallManager {

    private static final String TAG = "SerialInstallManager";
    private static SerialInstallManager instance;
    private EventBus bus = EventBus.getDefault();

    private SerialInstallManager(){}
    public static SerialInstallManager getInstance() {
        synchronized (SerialInstallManager.class) {
            if(instance == null) {
                instance = new SerialInstallManager();
            }
            return instance;
        }
    }

    private Map<Serial, Operation> installingSerial = new HashMap<>();

    public synchronized void install(final Serial serial) {
        if (serial.installState == Serial.State.INSTALLED) {
            Log.w(TAG, serial.name + " has already installed");
            return;
        }
        if (installingSerial.containsKey(serial)) {
            Log.w(TAG, serial.name + " is installing");
            return;
        }
        serial.installState = Serial.State.INSTALLING;
        new InstallSerialOperation(serial).callback(new Operation.Callback<Void, Void>() {
            @Override
            public void onProgress(int progress) {
                serial.installProgress = progress;
                bus.post(new SerialStateUpdateEvent(serial));
            }

            @Override
            public void onSuccess(Void aVoid) {
                installingSerial.remove(serial);
                serial.installState = Serial.State.INSTALLED;
                bus.post(new SerialStateUpdateEvent(serial));
            }
        }).enqueue();
    }
}
