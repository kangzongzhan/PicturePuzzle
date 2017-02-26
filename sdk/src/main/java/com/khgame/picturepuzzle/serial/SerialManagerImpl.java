package com.khgame.picturepuzzle.serial;

import com.khgame.picturepuzzle.db.model.SerialPicturePo;
import com.khgame.picturepuzzle.db.model.SerialPo;
import com.khgame.picturepuzzle.db.operation.GetAllSerialPictureByUuidOperation;
import com.khgame.picturepuzzle.db.operation.InsertSerialOperation;
import com.khgame.picturepuzzle.db.operation.InsertSerialPictureOperation;
import com.khgame.picturepuzzle.db.operation.QueryAllSerialOperation;
import com.khgame.picturepuzzle.model.Serial;
import com.khgame.picturepuzzle.model.SerialPicture;
import com.khgame.picturepuzzle.operation.DownloadPictureOperation;
import com.khgame.picturepuzzle.operation.Operation;
import com.khgame.picturepuzzle.service.model.SerialDto;
import com.khgame.picturepuzzle.service.model.SerialPictureDto;
import com.khgame.picturepuzzle.service.operation.GetAllSerialsOperation;
import com.khgame.picturepuzzle.service.operation.GetSerialPictureByUuid;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zkang on 2017/2/26.
 */

public class SerialManagerImpl implements SerialManager {

    private List<Serial> serialList = new ArrayList<>();

    private Serial currentSerial;
    private List<SerialPicture> currentSerialPictureList = new ArrayList<>();

    private EventBus bus = EventBus.getDefault();

    private static SerialManagerImpl instance;
    private SerialManagerImpl(){};
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

        final List<SerialPo> serialsDB = new ArrayList<>(); // from db
        final List<SerialDto> serialsNetwork = new ArrayList<>(); // from network

        // #1 load from db
        new QueryAllSerialOperation().callback(new Operation.Callback<List<SerialPo>, Void>() {
            @Override
            public void onSuccess(List<SerialPo> serialPos) {
                serialsDB.addAll(serialPos);
                serialList = merge(serialsDB, serialsNetwork);
                bus.post(new SerialsUpdateEvent());
            }
        }).enqueue();

        // #2 load from network
        new GetAllSerialsOperation().callback(new Operation.Callback<List<SerialDto>, Void>() {
            @Override
            public void onSuccess(List<SerialDto> serialDtos) {
                serialsNetwork.addAll(serialDtos);
                serialList = merge(serialsDB, serialsNetwork);
                bus.post(new SerialsUpdateEvent());
            }
        }).enqueue();

    }

    @Override
    public void install(final Serial serial) {
        serial.installed = Serial.SerialState.INSTALLING;

        bus.post(new SerialInstallEvent(SerialInstallEvent.EventType.BEGIN));
        new GetSerialPictureByUuid(serial.uuid).callback(new Operation.Callback<List<SerialPictureDto>, Void>() {
            @Override
            public void onSuccess(List<SerialPictureDto> serialPictureDtos) {

                for (SerialPictureDto serialPictureDto: serialPictureDtos) {
                    new DownloadPictureOperation(serialPictureDto.uuid, serialPictureDto.url).execute();
                    new InsertSerialPictureOperation(serialPictureDto).execute();

                    int progress = serialPictureDtos.indexOf(serialPictureDto) / serialPictureDtos.size();
                    bus.post(new SerialInstallEvent(SerialInstallEvent.EventType.INSTALLING, progress));
                }
                new InsertSerialOperation(serial).execute();
                Collections.sort(serialList);
                bus.post(new SerialInstallEvent(SerialInstallEvent.EventType.END));
            }
        }).enqueue();


    }

    @Override
    public List<Serial> getSerials() {
        return serialList;
    }

    @Override
    public Serial getSerialByUuid(String uuid) {

        for(Serial serial : serialList) {
            if(serial.uuid.equals(uuid)) {
                return serial;
            }
        }

        return null;
    }

    @Override
    public void startSerial(Serial serial) {
        this.currentSerial = serial;
        loadSerialPicturesBySerialUuid(serial.uuid);
    }

    @Override
    public Serial getCurrentSerial() {
        return currentSerial;
    }

    @Override
    public void endSerial() {
        currentSerialPictureList.clear();
        currentSerial = null;
    }

    @Override
    public void loadSerialPicturesBySerialUuid(String uuid) {
        new GetAllSerialPictureByUuidOperation(uuid).callback(new Operation.Callback<List<SerialPicturePo>, Void>() {
            @Override
            public void onSuccessMainThread(List<SerialPicturePo> serialPicturePos) {
                if(serialPicturePos == null || serialPicturePos.size() == 0) {
                    return;
                }

                if(currentSerial == null) {
                    return;
                }

                if(!serialPicturePos.get(0).serialUuid.equals(currentSerial.uuid)) {
                    return;
                }

                for(SerialPicturePo serialPicturePo : serialPicturePos) {
                    SerialPicture serialPicture = new SerialPicture();
                    serialPicture.uuid = serialPicturePo.uuid;
                    serialPicture.name = serialPicturePo.name;
                    serialPicture.serialUuid = serialPicturePo.serialUuid;
                    serialPicture.networkPath = serialPicturePo.networkPath;
                    serialPicture.easyData = serialPicturePo.easyData;
                    serialPicture.mediaData = serialPicturePo.mediumData;
                    serialPicture.hardData = serialPicturePo.hardData;
                    currentSerialPictureList.add(serialPicture);
                }
                bus.post(new SerialPicturesLoadFinishEvent());
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
            serial.networkCoverPath = serialPo.networkCoverPath;
            serial.installed = Serial.SerialState.INSTALLED;
            list.add(serial);
        }

        for(SerialDto serialDto: serialDtos) {
            if(have(list, serialDto)) {
                continue;
            }
            Serial serial = new Serial();
            serial.uuid = serialDto.uuid;
            serial.name = serialDto.name;
            serial.networkCoverPath = serialDto.coverUrl;
            serial.installed = Serial.SerialState.UNINSTALL;
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
