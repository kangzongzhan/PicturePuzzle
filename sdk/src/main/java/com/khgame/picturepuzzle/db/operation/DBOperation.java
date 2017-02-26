package com.khgame.picturepuzzle.db.operation;

import android.database.sqlite.SQLiteDatabase;

import com.khgame.picturepuzzle.operation.Operation;
import com.khgame.picturepuzzle.db.DBManager;

/**
 * Created by zkang on 2017/2/17.
 */

public abstract class DBOperation<S, F> extends Operation<S, F> {
    protected SQLiteDatabase db = DBManager.getBD();
}
