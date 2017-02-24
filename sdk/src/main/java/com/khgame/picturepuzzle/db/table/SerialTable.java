package com.khgame.picturepuzzle.db.table;

/**
 * Created by zkang on 2017/2/17.
 */

public class SerialTable {

    public static final String NAME = "Serial";

    public static class Cols {
        public static final String ID = "_id"; // Integer, Primary key, Autoincrement
        public static final String UUID = "uuid"; // Text
        public static final String NAME = "name"; // Text
        public static final String GAMELEVEL = "game_level"; // Integer
        public static final String LOCALCOVERPATH = "local_cover_path"; // Integer
        public static final String NETWORKCOVERPATH = "network_cover_path"; // Integer
        public static final String THEMECOLOR = "theme_color"; // Text
    }

    public static final String CREATESQL = "create table " + NAME + "( " +
            Cols.ID + " integer primary key autoincrement, " +
            Cols.UUID + " text, " +
            Cols.NAME + " text, " +
            Cols.GAMELEVEL + " text, " +
            Cols.LOCALCOVERPATH + " text, " +
            Cols.NETWORKCOVERPATH + " text, " +
            Cols.THEMECOLOR + " text" +
            ")";
}
