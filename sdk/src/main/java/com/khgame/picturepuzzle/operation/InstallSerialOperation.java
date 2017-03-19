package com.khgame.picturepuzzle.operation;

import com.khgame.picturepuzzle.db.operation.DeleteSerialPicturesByUuid;
import com.khgame.picturepuzzle.db.operation.InsertSerialOperation;
import com.khgame.picturepuzzle.db.operation.InsertSerialPictureOperation;
import com.khgame.picturepuzzle.model.Serial;
import com.khgame.picturepuzzle.serial.SerialInstallEvent;
import com.khgame.picturepuzzle.service.model.SerialPictureDto;
import com.khgame.picturepuzzle.service.operation.GetSerialPictureByUuid;

import java.util.Collections;
import java.util.List;

/**
 * Created by zkang on 2017/3/17.
 */

public class InstallSerialOperation extends Operation<Void, Void> {
    private Serial serial;
    public InstallSerialOperation(Serial serial) {
        this.serial = serial;
    }
    @Override
    protected void doWork() {

        new GetSerialPictureByUuid(serial.uuid).callback(new Operation.Callback<List<SerialPictureDto>, Void>() {
            @Override
            public void onSuccess(List<SerialPictureDto> serialPictureDtos) {
                new DeleteSerialPicturesByUuid(serial.uuid).execute();
                for (SerialPictureDto serialPictureDto: serialPictureDtos) {
                    new DownloadPictureOperation(serialPictureDto.uuid, serialPictureDto.url).execute();
                    new InsertSerialPictureOperation(serialPictureDto, serial.uuid).execute();
                    int progress = serialPictureDtos.indexOf(serialPictureDto) / serialPictureDtos.size();
                    InstallSerialOperation.this.postProgress(progress);
                }
                new InsertSerialOperation(serial).execute();
                postSuccess(null);
            }
        }).enqueue();



    }
}
