package com.khgame.picturepuzzle.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.khgame.picturepuzzle.data.ClassicPicture;
import com.khgame.picturepuzzle.data.Serial;
import com.khgame.picturepuzzle.data.SerialPicture;

@Database(entities = {ClassicPicture.class, Serial.class, SerialPicture.class}, version = 10, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract ClassicPictureDao classicPictureDao();
    public abstract SerialDao serialDao();
    public abstract SerialPictureDao serialPictureDao();

    private static final Object sLock = new Object();

    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "NewPicturePuzzle.db")
                        .build();
            }
            return INSTANCE;
        }
    }
}
