package com.khgame.sdk.picturepuzzle.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.khgame.sdk.picturepuzzle.db.table.ClassicPictureTable;
import com.khgame.sdk.picturepuzzle.db.table.SerialPictureTable;
import com.khgame.sdk.picturepuzzle.db.table.SerialTable;

/**
 * Created by Kisha Deng on 2/12/2017.
 */

public class DBManager {

    private static DBManager instance;

    private static Context mContext;
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "picture-puzzle-db";

    public static DBManager getInstance() {
        synchronized (DBManager.class) {
            if (instance == null)
                instance = new DBManager();
        }
        return instance;
    }

    public static SQLiteDatabase getBD() {
        return getInstance().sqLiteOpenHelper.getWritableDatabase();
    }

    public static void initialize(Context context) {
        mContext = context;
    }

    private SQLiteOpenHelper sqLiteOpenHelper = new SQLiteOpenHelper(mContext, DB_NAME, null, DB_VERSION) {
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(ClassicPictureTable.CREATESQL);
            sqLiteDatabase.execSQL(SerialTable.CREATESQL);
            sqLiteDatabase.execSQL(SerialPictureTable.CREATESQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    };
}
