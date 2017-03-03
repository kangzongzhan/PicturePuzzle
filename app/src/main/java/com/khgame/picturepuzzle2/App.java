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
import com.khgame.picturepuzzle.operation.CopyAssetsToDiskOperation;

import java.util.UUID;

/**
 * Created by zkang on 2017/2/18.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SettingManager.Initialize(this);
        DBManager.initialize(this);

        int openTimes = SettingManager.Instance().getInt("OpenTimes", 0);
        SettingManager.Instance().setInt("OpenTimes", ++openTimes);
    }



}
