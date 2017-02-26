package com.khgame.picturepuzzle.operation;

import android.graphics.Bitmap;
import android.util.Log;

import com.khgame.picturepuzzle.base.Application;
import com.khgame.picturepuzzle.db.operation.InsertSerialPictureOperation;
import com.khgame.picturepuzzle.service.model.SerialPictureDto;
import com.khgame.picturepuzzle.service.operation.GetSerialPictureByUuid;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.IoUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by zkang on 2017/2/25.
 */

public class InstallSerialOperation extends Operation<Void, Void> {

    private String uuid;

    public InstallSerialOperation(String uuid){
        this.uuid = uuid;
    }

    @Override
    protected void doWork() {
        new GetSerialPictureByUuid(uuid).callback(new Callback<List<SerialPictureDto>, Void>() {
            @Override
            public void onSuccess(List<SerialPictureDto> serialPictureDtos) {

                // load all image and save to local
                for(SerialPictureDto serialPictureDto: serialPictureDtos) {
                    String url = serialPictureDto.url;
                    Bitmap bitmap = ImageLoader.getInstance().loadImageSync(url);
                    File localFile = Application.PictureFile(uuid);
                    FileOutputStream fileOutputStream = null;
                    try {
                        fileOutputStream = new FileOutputStream(localFile);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                        fileOutputStream.flush();
                    } catch (Exception e) {
                        Log.w("InstallSerialOperation", "save picture fail");
                    } finally {
                        IoUtils.closeSilently(fileOutputStream);
                    }
                    InstallSerialOperation.this.postProgress(serialPictureDtos.indexOf(serialPictureDto) / serialPictureDtos.size());
                }

                // insert to db
                for(SerialPictureDto serialPictureDto: serialPictureDtos) {
                    new InsertSerialPictureOperation(serialPictureDto).execute();
                }

                postSuccess(null);
            }
        }).enqueue();
    }


}
