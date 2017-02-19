package com.khgame.picturepuzzle2;

import android.app.Application;
import android.util.Log;

import com.khgame.picturepuzzle.common.Operation;
import com.khgame.picturepuzzle.common.SettingManager;
import com.khgame.picturepuzzle.core.DisorderUtil;
import com.khgame.picturepuzzle.core.GameLevel;
import com.khgame.picturepuzzle.db.DBManager;
import com.khgame.picturepuzzle.db.model.ClassicPicture;
import com.khgame.picturepuzzle.db.operation.InsertClassicPictureOperation;
import com.khgame.picturepuzzle2.operation.CopyAssetsToDiskOperation;

import java.io.File;

/**
 * Created by zkang on 2017/2/18.
 */

public class App extends Application {
    public static final String TAG = "App";

    public static Application application = null;

    @Override
    public void onCreate() {
        super.onCreate();
        App.application = this;
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
            File destination = new File(ClassicPictureDir(), pictureName);
            new CopyAssetsToDiskOperation(assets, destination).callback(new Operation.Callback<File, Void>() {
                @Override
                public void onSuccess(final File file) {
                    Log.d(TAG, "copyToDisk File:" + pictureName + ", Success!");
                    ClassicPicture picture = new ClassicPicture();
                    picture.localPath = file.getAbsolutePath();
                    picture.easyData = DisorderUtil.newDisorderString(GameLevel.EASY);
                    picture.mediumData = DisorderUtil.newDisorderString(GameLevel.MEDIUM);
                    picture.hardData = DisorderUtil.newDisorderString(GameLevel.HARD);

                    new InsertClassicPictureOperation(picture).callback(new Operation.Callback<ClassicPicture, Void>() {
                        @Override
                        public void onSuccess(ClassicPicture picture) {
                            Log.d(TAG, "insertToDB, File:" + file.getName() + ", Success!");
                        }

                        @Override
                        public void onFailure(Void aVoid) {
                            Log.e(TAG, "insertToDB, File:" + file.getName() + ", Failure!");
                        }
                    }).enqueue();
                }
            }).enqueue();
        }
    }

    public static File ClassicPictureDir() {
        File dir = new File(application.getCacheDir(), "classic");
        if(!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File SerialCoverPictureDir() {
        return new File(application.getCacheDir().getAbsolutePath() + "/serial/covers");
    }


    public static File SerialPictureDir(String serialName) {
        return new File(application.getCacheDir().getAbsolutePath() + "/serial/" + serialName);
    }
}
