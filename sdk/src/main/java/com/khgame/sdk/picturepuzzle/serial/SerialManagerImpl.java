package com.khgame.sdk.picturepuzzle.serial;

import android.graphics.Color;
import android.util.Log;

import com.khgame.sdk.picturepuzzle.common.Result;
import com.khgame.sdk.picturepuzzle.db.model.SerialPo;
import com.khgame.sdk.picturepuzzle.db.operation.QueryAllSerialsOperation;
import com.khgame.sdk.picturepuzzle.db.operation.QuerySerialBySerialUuidOperation;
import com.khgame.sdk.picturepuzzle.model.Serial;
import com.khgame.sdk.picturepuzzle.operation.InstallSerialOperation;
import com.khgame.sdk.picturepuzzle.operation.Operation;
import com.khgame.sdk.picturepuzzle.service.model.SerialDto;
import com.khgame.sdk.picturepuzzle.service.operation.GetAllSerialsFromServiceOperation;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zkang on 2017/2/26.
 */

public class SerialManagerImpl implements SerialManager {
    private static final String TAG = "SerialManagerImpl";
    private EventBus bus = EventBus.getDefault();

    private Map<String, Operation> installingSerials = new HashMap();

    private static SerialManagerImpl instance;
    private SerialManagerImpl(){}
    public static SerialManager getInstance() {
        synchronized (SerialManagerImpl.class) {
            if(instance != null) {
                return instance;
            }
            instance = new SerialManagerImpl();
        }
        return instance;
    }

    @Override
    public void loadSerials() {
        final List<Serial> serialList = new ArrayList<>();
        final List<SerialPo> serialsDB = new ArrayList<>(); // from db
        final List<SerialDto> serialsNetwork = new ArrayList<>(); // from network

        // #1 load from db
        new QueryAllSerialsOperation().callback(new Operation.Callback<List<SerialPo>, Void>() {
            @Override
            public void onSuccess(List<SerialPo> serialPos) {
                serialsDB.addAll(serialPos);
                serialList.clear();
                serialList.addAll(merge(serialsDB, serialsNetwork));
                SerialsLoadEvent event = new SerialsLoadEvent(Result.Success);
                event.serials = serialList;
                bus.post(event);
            }
        }).enqueue();

        // #2 load from network
        new GetAllSerialsFromServiceOperation().callback(new Operation.Callback<List<SerialDto>, Void>() {
            @Override
            public void onSuccess(List<SerialDto> serialDtos) {
                serialsNetwork.addAll(serialDtos);
                serialList.clear();
                serialList.addAll(merge(serialsDB, serialsNetwork));
                SerialsLoadEvent event = new SerialsLoadEvent(Result.Success);
                event.serials = serialList;
                bus.post(event);
            }
        }).enqueue();

    }

    @Override
    public void install(Serial serial) {
        if (serial.installState == Serial.State.INSTALLED) {
            Log.w(TAG, serial.name + " has already installed");
            return;
        }
        if (installingSerials.containsKey(serial.uuid)) {
            Log.w(TAG, serial.name + " is installing");
            return;
        }

        SerialInstallEvent beginEvent = new SerialInstallEvent(SerialInstallEvent.EventType.BEGIN);
        beginEvent.serial = serial;
        bus.post(beginEvent);

        serial.installState = Serial.State.INSTALLING;

        Operation operation = new InstallSerialOperation(serial).callback(new Operation.Callback<Void, Void>() {
            @Override
            public void onProgress(int progress) {
                SerialInstallEvent updateEvent = new SerialInstallEvent(SerialInstallEvent.EventType.INSTALLING);
                updateEvent.progress = progress;
                updateEvent.serial = serial;
                bus.post(updateEvent);
            }
            @Override
            public void onSuccess(Void aVoid) {
                installingSerials.remove(serial);
                serial.installState = Serial.State.INSTALLED;
                SerialInstallEvent endEvent = new SerialInstallEvent(SerialInstallEvent.EventType.END);
                endEvent.serial = serial;
                bus.post(endEvent);
            }
        }).enqueue();
        installingSerials.put(serial.uuid, operation);
    }

    @Override
    public void getSerialBySerialUuid(String serialUuid) {
        new QuerySerialBySerialUuidOperation(serialUuid).callback(new Operation.Callback<SerialPo, Void>() {
            @Override
            public void onSuccessMainThread(SerialPo serialPo) {
                Serial serial = new Serial();
                serial.name = serialPo.name;
                serial.uuid = serialPo.uuid;
                serial.gameLevel = serialPo.gameLevel;
                serial.primaryColor = serialPo.primaryColor;
                serial.secondaryColor = serialPo.secondaryColor;
                SerialLoadEvent event = new SerialLoadEvent(Result.Success);
                event.serial = serial;
                bus.post(event);
            }
        }).enqueue();
    }

    /**
     * ======= Private Method =======
     */

    private List<Serial> merge(final List<SerialPo> serialPos, final List<SerialDto> serialDtos) {
        List<Serial> list = new ArrayList<>();

        for(SerialPo serialPo: serialPos) {
            Serial serial = new Serial();
            serial.uuid = serialPo.uuid;
            serial.name = serialPo.name;
            serial.gameLevel = serialPo.gameLevel;
            serial.primaryColor = serialPo.primaryColor;
            serial.secondaryColor = serialPo.secondaryColor;
            serial.installState = Serial.State.INSTALLED;
            list.add(serial);
        }

        for(SerialDto serialDto: serialDtos) {
            if(have(list, serialDto)) {
                continue;
            }
            Serial serial = new Serial();
            serial.uuid = serialDto.uuid;
            serial.name = serialDto.name;
            serial.primaryColor = Color.parseColor(serialDto.primaryColor);
            serial.secondaryColor = Color.parseColor(serialDto.secondaryColor);
            serial.installState = Serial.State.UNINSTALL;
            list.add(serial);
        }

        return list;
    }

    private boolean have(List<Serial> list, SerialDto serialDto) {
        for (Serial serial: list) {
            if(serial.uuid.equals(serialDto.uuid)) {
                return true;
            }
        }
        return false;
    }
}
