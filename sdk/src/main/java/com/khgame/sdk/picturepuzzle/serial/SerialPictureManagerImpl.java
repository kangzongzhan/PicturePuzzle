package com.khgame.sdk.picturepuzzle.serial;

import com.khgame.sdk.picturepuzzle.common.Result;
import com.khgame.sdk.picturepuzzle.db.model.SerialPicturePo;
import com.khgame.sdk.picturepuzzle.db.operation.GetSerialPictureByUuidOperation;
import com.khgame.sdk.picturepuzzle.db.operation.QueryAllSerialPicturesBySerialUuidOperation;
import com.khgame.sdk.picturepuzzle.db.operation.UpdateSerialPictureOperation;
import com.khgame.sdk.picturepuzzle.model.SerialPicture;
import com.khgame.sdk.picturepuzzle.operation.Operation;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zkang on 2017/4/8.
 */

public class SerialPictureManagerImpl implements SerialPictureManager {

    private EventBus bus = EventBus.getDefault();

    private static SerialPictureManagerImpl instance;
    private SerialPictureManagerImpl(){}
    public static SerialPictureManager getInstance() {
        synchronized (SerialPictureManagerImpl.class) {
            if (instance != null) {
                return instance;
            }
            instance = new SerialPictureManagerImpl();
        }
        return instance;
    }

    @Override
    public void loadSerialPicturesBySerialUuid(final String serialUuid) {
        new QueryAllSerialPicturesBySerialUuidOperation(serialUuid).callback(new Operation.Callback<List<SerialPicturePo>, Void>() {
            @Override
            public void onSuccessMainThread(List<SerialPicturePo> serialPicturePos) {
                if (serialPicturePos == null || serialPicturePos.size() == 0) {
                    return;
                }
                List<SerialPicture> serialPictures = new ArrayList<>();
                for (SerialPicturePo serialPicturePo : serialPicturePos) {
                    SerialPicture serialPicture = new SerialPicture();
                    serialPicture.uuid = serialPicturePo.uuid;
                    serialPicture.name = serialPicturePo.name;
                    serialPicture.serialUuid = serialPicturePo.serialUuid;
                    serialPicture.easyData = serialPicturePo.easyData;
                    serialPicture.mediumData = serialPicturePo.mediumData;
                    serialPicture.hardData = serialPicturePo.hardData;
                    serialPictures.add(serialPicture);
                }
                SerialPicturesLoadEvent event = new SerialPicturesLoadEvent(Result.Success);
                event.serialUuid = serialUuid;
                event.serialPictures = serialPictures;
                bus.post(event);
            }
        }).enqueue();
    }

    @Override
    public void getSerialPictureByUuid(String serialPictureUuid) {
        new GetSerialPictureByUuidOperation(serialPictureUuid).callback(new Operation.Callback<SerialPicturePo, Void>() {
            @Override
            public void onSuccess(SerialPicturePo serialPicturePo) {
                SerialPicture serialPicture = new SerialPicture();
                serialPicture.uuid = serialPicturePo.uuid;
                serialPicture.name = serialPicturePo.name;
                serialPicture.serialUuid = serialPicturePo.serialUuid;
                serialPicture.easyData = serialPicturePo.easyData;
                serialPicture.mediumData = serialPicturePo.mediumData;
                serialPicture.hardData = serialPicturePo.hardData;

                SerialPictureLoadEvent event = new SerialPictureLoadEvent(Result.Success);
                event.serialPicture = serialPicture;
                bus.post(event);
            }
        }).enqueue();
    }

    @Override
    public void updateSerialPicture(SerialPicture serialPicture) {
        new UpdateSerialPictureOperation(serialPicture).enqueue();
    }
}
