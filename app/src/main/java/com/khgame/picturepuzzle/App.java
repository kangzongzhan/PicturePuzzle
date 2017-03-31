package com.khgame.picturepuzzle;

import com.khgame.sdk.picturepuzzle.base.Application;
import com.khgame.sdk.picturepuzzle.common.SettingManager;
import com.khgame.sdk.picturepuzzle.db.DBManager;

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
