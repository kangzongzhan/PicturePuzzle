package com.khgame.picturepuzzle;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.facebook.stetho.Stetho;
import com.khgame.picturepuzzle.base.Application;
import com.khgame.picturepuzzle.common.SettingManager;
import com.khgame.picturepuzzle.data.source.local.AppDatabase;
import com.khgame.picturepuzzle.db.DBManager;
import io.fabric.sdk.android.Fabric;


public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        SettingManager.Initialize(this);
        DBManager.initialize(this);
        Stetho.initializeWithDefaults(this);
        int openTimes = SettingManager.Instance().getInt("OpenTimes", 0);
        SettingManager.Instance().setInt("OpenTimes", ++openTimes);
    }

    public static AppDatabase getAppDatabase() {
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "Database.db")
                .build();
    }

}
