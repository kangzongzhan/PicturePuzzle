package com.khgame.picturepuzzle2.sdk.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
            if(instance == null)
                instance = new DBManager();
        }
        return instance;
    }
    public static void initialize(Context context) {
        getInstance();
        mContext = context;
    }

    class MySQLiteOpenHelper extends SQLiteOpenHelper{

        public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }

    private SQLiteOpenHelper sqLiteOpenHelper = new SQLiteOpenHelper() {
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    };
}
