package com.khgame.picturepuzzle2;

import android.util.Log;

import com.khgame.picturepuzzle.base.Application;
import com.khgame.picturepuzzle.model.ClassicPicture;
import com.khgame.picturepuzzle.operation.Operation;
import com.khgame.picturepuzzle.common.SettingManager;
import com.khgame.picturepuzzle.core.DisorderUtil;
import com.khgame.picturepuzzle.core.GameLevel;
import com.khgame.picturepuzzle.db.DBManager;
import com.khgame.picturepuzzle.db.operation.InsertClassicPictureOperation;
import com.khgame.picturepuzzle2.operation.CopyAssetsToDiskOperation;

import java.io.File;
import java.util.UUID;

/**
 * Created by zkang on 2017/2/18.
 */

public class App extends Application {
    public static final String TAG = "App";


    @Override
    public void onCreate() {
        super.onCreate();
        SettingManager.Initialize(this);
        DBManager.initialize(this);
        initializeIfFirstTime();
    }

    private void initializeIfFirstTime() {

        int openTimes = SettingManager.Instance().getInt("OpenTimes", 0);

        if(openTimes == 0) {
            copyToDisk();
        }

        SettingManager.Instance().setInt("OpenTimes", ++openTimes);
    }

    private void copyToDisk() {

        for (int i = 1; i < 10; i++) {
            final String pictureName = "default0" + i;
            String assets = "default0" + i + ".jpg";
            new CopyAssetsToDiskOperation(assets, UUID.randomUUID().toString()).callback(new Operation.Callback<ClassicPicture, Void>() {
                @Override
                public void onSuccess(final ClassicPicture classicPicture) {
                    Log.d(TAG, "copyToDisk File:" + pictureName + ", Success!");
                    classicPicture.easyData = DisorderUtil.newDisorderString(GameLevel.EASY);
                    classicPicture.mediumData = DisorderUtil.newDisorderString(GameLevel.MEDIUM);
                    classicPicture.hardData = DisorderUtil.newDisorderString(GameLevel.HARD);

                    new InsertClassicPictureOperation(classicPicture).callback(new Operation.Callback<ClassicPicture, Void>() {
                        @Override
                        public void onSuccess(ClassicPicture picture) {
                            Log.d(TAG, "insertToDB, File:" + picture.uuid + ", Success!");
                        }

                        @Override
                        public void onFailure(Void aVoid) {
                            Log.e(TAG, "insertToDB, Failure!");
                        }
                    }).enqueue();
                }
            }).enqueue();
        }
    }


}
