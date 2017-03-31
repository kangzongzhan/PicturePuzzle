package com.khgame.sdk.picturepuzzle.db.operation;

import android.content.ContentValues;

import com.khgame.sdk.picturepuzzle.core.DisorderUtil;
import com.khgame.sdk.picturepuzzle.core.GameLevel;
import com.khgame.sdk.picturepuzzle.core.Point;
import com.khgame.sdk.picturepuzzle.db.table.ClassicPictureTable;
import com.khgame.sdk.picturepuzzle.db.table.SerialPictureTable;
import com.khgame.sdk.picturepuzzle.model.SerialPicture;

import java.util.List;

/**
 * Created by zkang on 2017/2/19.
 */

public class UpdateSerialPictureOperation extends DBOperation<SerialPicture, Void> {
    private SerialPicture serialPicture;
    private List<Point> gameData;

    public UpdateSerialPictureOperation(SerialPicture serialPicture, List<Point> gameData) {
        this.serialPicture = serialPicture;
        this.gameData = gameData;
    }

    @Override
    protected void doWork() {
        int gameLevel = GameLevel.getLevel(gameData);
        String col = null;
        switch (gameLevel) {
            case GameLevel.EASY:
                col = ClassicPictureTable.Cols.EASYDATA;
                serialPicture.easyData = DisorderUtil.encode(gameData);
                break;
            case GameLevel.MEDIUM:
                col = ClassicPictureTable.Cols.MEDIUMDATA;
                serialPicture.mediumData = DisorderUtil.encode(gameData);
                break;
            case GameLevel.HARD:
                col = ClassicPictureTable.Cols.HARDDATA;
                serialPicture.hardData = DisorderUtil.encode(gameData);
                break;
        }

        ContentValues values = new ContentValues();
        values.put(col, DisorderUtil.encode(gameData));
        int rows = db.update(SerialPictureTable.NAME, values, SerialPictureTable.Cols.UUID + "='" + serialPicture.uuid + "'", null);
        if(rows == 0) {
            postFailure(null);
        } else {
            postSuccess(null);
        }
    }
}
