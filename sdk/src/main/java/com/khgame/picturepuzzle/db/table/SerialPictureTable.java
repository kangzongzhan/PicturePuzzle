package com.khgame.picturepuzzle.db.table;

/**
 * Created by zkang on 2017/2/17.
 */

public class SerialPictureTable {
    public static final String NAME = "SerialPicture";

    public static class Cols {
        public static final String ID = "_id"; // Integer, Primary Key, Autoincrement
        public static final String UUID = "uuid"; // Text
        public static final String NAME = "name"; // Text
        public static final String SERIALID = "serialId"; // Text
        public static final String LOCALPATH = "localPath"; // Text
        public static final String URL = "url"; // Text
        public static final String EASYDATA = "easyData"; // Text
        public static final String MEDIUMDATA = "mediumData"; // Text
        public static final String HARDDATA = "hardData"; // Text
    }

    public static final String CREATESQL = "create table " + NAME + "( " +
            Cols.ID + " integer primary key autoincrement, " +
            Cols.UUID + " text, " +
            Cols.NAME + " text, " +
            Cols.SERIALID + " integer, " +
            Cols.LOCALPATH + " text, " +
            Cols.URL + " text, " +
            Cols.EASYDATA + " text, " +
            Cols.MEDIUMDATA + " text, " +
            Cols.HARDDATA + " text" +
            ")";
}
