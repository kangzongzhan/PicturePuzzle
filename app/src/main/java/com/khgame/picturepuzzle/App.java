package com.khgame.picturepuzzle;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.facebook.stetho.Stetho;
import com.khgame.picturepuzzle.base.Application;
import com.khgame.picturepuzzle.common.SettingManager;
import com.khgame.picturepuzzle.db.DBManager;
import io.fabric.sdk.android.Fabric;

/**
 * Created by zkang on 2017/2/18.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        SettingManager.Initialize(this);
        DBManager.initialize(this);
        Stetho.initializeWithDefaults(this);
        int openTimes = SettingManager.Instance().getInt("OpenTimes", 0);
        SettingManager.Instance().setInt("OpenTimes", ++openTimes);
    }



}
