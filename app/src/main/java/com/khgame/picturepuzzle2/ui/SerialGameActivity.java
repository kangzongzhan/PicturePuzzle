package com.khgame.picturepuzzle2.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;

import com.khgame.picturepuzzle.base.SquaredActivity;
import com.khgame.picturepuzzle.core.DisorderUtil;
import com.khgame.picturepuzzle.core.GameLevel;
import com.khgame.picturepuzzle.core.Point;
import com.khgame.picturepuzzle.db.operation.UpdateSerialPictureOperation;
import com.khgame.picturepuzzle.model.BitmapEntry;
import com.khgame.picturepuzzle.model.SerialPicture;
import com.khgame.picturepuzzle.operation.LoadPictureOperation;
import com.khgame.picturepuzzle.operation.Operation;
import com.khgame.picturepuzzle.serial.SerialManager;
import com.khgame.picturepuzzle.serial.SerialManagerImpl;
import com.khgame.picturepuzzle2.R;
import com.khgame.picturepuzzle2.ui.view.GameView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kisha Deng on 2/28/2017.
 */

public class SerialGameActivity extends SquaredActivity {

    private String uuid;
    private int gameLevel;
    private SerialPicture serialPicture;
    private Bitmap bitmap;

    private SerialManager serialManager = SerialManagerImpl.getInstance();

    @BindView(R.id.gameview)
    GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uuid = getIntent().getStringExtra("uuid");
        setContentView(R.layout.activity_classic_game);
        ButterKnife.bind(this);
        gameView.setGameOverListener(gameOverListener);
        gameLevel = serialManager.getCurrentSerial().gameLevel;
        serialPicture = getSerialPictureByUuid(uuid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initGameData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(gameView.isStarted()) {
            List<Point> gameData = gameView.getGameData();
            new UpdateSerialPictureOperation(serialPicture, gameData).enqueue();
        }
    }

    private void initGameData() {

        new LoadPictureOperation(serialPicture.uuid, serialPicture.networkPath).callback(new Operation.Callback<BitmapEntry, Void>() {
            @Override
            public void onSuccessMainThread(BitmapEntry bitmapEntry) {
                super.onSuccessMainThread(bitmapEntry);
                bitmap = bitmapEntry.bitmap;
                startGame();
            }
        }).enqueue();
    }

    private void startGame() {
        List<Point> gameData = null;
        switch (gameLevel) {
            case GameLevel.EASY:
                gameData = DisorderUtil.decode(serialPicture.easyData);
                break;
            case GameLevel.MEDIUM:
                gameData = DisorderUtil.decode(serialPicture.mediumData);
                break;
            case GameLevel.HARD:
                gameData = DisorderUtil.decode(serialPicture.hardData);
                break;
        }
        gameView.start(gameData, bitmap);
    }

    private GameView.GameOverListener gameOverListener = new GameView.GameOverListener() {
        @Override
        public void onGameOver() {
            List<Point> gameData = gameView.getGameData();
            new UpdateSerialPictureOperation(serialPicture
                    , gameData).enqueue();
            Snackbar.make(gameView, "Game Over", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    };


    private SerialPicture getSerialPictureByUuid(String uuid) {
        List<SerialPicture> serialPictures = serialManager.getCurrentSerialPictureList();
        for(SerialPicture serialPicture:serialPictures) {
            if(serialPicture.uuid.equals(uuid)) {
                return serialPicture;
            }
        }
        return null;
    }
}
