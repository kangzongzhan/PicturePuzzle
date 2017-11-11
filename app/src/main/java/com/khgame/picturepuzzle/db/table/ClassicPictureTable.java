package com.khgame.picturepuzzle.db.table;

/**
 * Created by zkang on 2017/2/17.
 */

public class ClassicPictureTable {

    public static final String NAME = "ClassPicture";
    public static class Cols {
        public static final String ID = "_id"; // Integer, Primary key, Autoincrement
        public static final String UUID = "uuid"; // Text, Not Null
        public static final String EASYDATA = "easyData"; // Text, UnNullable
        public static final String MEDIUMDATA = "mediumData"; // Text, UnNullable
        public static final String HARDDATA = "hardData"; // Text, UnNullable
    }

    public final static String CREATESQL = "create table " + NAME + "( " +
            Cols.ID + " integer primary key autoincrement, " +
            Cols.UUID + " text, " +
            Cols.EASYDATA + " text, " +
            Cols.MEDIUMDATA + " text, " +
            Cols.HARDDATA + " text" +
            ")";
}
