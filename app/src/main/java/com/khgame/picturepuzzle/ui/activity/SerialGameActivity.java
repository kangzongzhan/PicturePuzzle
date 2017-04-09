package com.khgame.picturepuzzle.ui.activity;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;

import com.khgame.sdk.picturepuzzle.base.SquaredActivity;
import com.khgame.sdk.picturepuzzle.common.BitmapManager;
import com.khgame.sdk.picturepuzzle.common.BitmapManagerImpl;
import com.khgame.sdk.picturepuzzle.common.Result;
import com.khgame.sdk.picturepuzzle.core.DisorderUtil;
import com.khgame.sdk.picturepuzzle.core.GameLevel;
import com.khgame.sdk.picturepuzzle.core.Point;
import com.khgame.sdk.picturepuzzle.events.BitmapLoadEvent;
import com.khgame.sdk.picturepuzzle.model.SerialPicture;
import com.khgame.picturepuzzle.R;
import com.khgame.picturepuzzle.ui.view.GameView;
import com.khgame.sdk.picturepuzzle.serial.SerialPictureLoadEvent;
import com.khgame.sdk.picturepuzzle.serial.SerialPictureManager;
import com.khgame.sdk.picturepuzzle.serial.SerialPictureManagerImpl;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Kisha Deng on 2/28/2017.
 */

public class SerialGameActivity extends SquaredActivity {

    public static final String SRIALPICTURE_UUID = "SERIALPICTURE_UUID";
    public static final String SRIALPICTURE_PRIMARY_COLOR = "SERIALPICTURE_PRIMARY_COLOR";
    public static final String SRIALPICTURE_SECONDARY_COLOR = "SERIALPICTURE_SECONDARY_COLOR";

    public static final String GAME_LEVEL = "GAME_LEVEL";

    private SerialPictureManager serialPictureManager = SerialPictureManagerImpl.getInstance();
    private BitmapManager bitmapManager = BitmapManagerImpl.getInstance();

    private String uuid;
    private int gameLevel;
    private int primaryColor;
    private int secondaryColor;

    private Bitmap bitmap;
    private SerialPicture serialPicture;

    @BindView(R.id.gameview)
    GameView gameView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_game);
        ButterKnife.bind(this);

        uuid = getIntent().getStringExtra(SerialGameActivity.SRIALPICTURE_UUID);
        gameLevel = getIntent().getIntExtra(SerialGameActivity.GAME_LEVEL, GameLevel.EASY);
        primaryColor = getIntent().getIntExtra(SerialGameActivity.SRIALPICTURE_PRIMARY_COLOR, getResources().getColor(R.color.colorPrimary));
        secondaryColor = getIntent().getIntExtra(SerialGameActivity.SRIALPICTURE_SECONDARY_COLOR, getResources().getColor(R.color.colorAccent));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        updateTheme(primaryColor, secondaryColor);
        updateFabImage();
        gameView.setGameListener(gameListener);

        serialPictureManager.getSerialPictureByUuid(uuid);
        bitmapManager.loadBitmapByUuid(uuid);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tryToStartGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateSerialPicture();
    }

    @OnClick(R.id.fab)
    void onClickFab() {
        updateSerialPicture();
        gameView.end();
        switch (gameLevel) {
            case GameLevel.EASY:
                gameLevel = GameLevel.MEDIUM;
                break;
            case GameLevel.MEDIUM:
                gameLevel = GameLevel.HARD;
                break;
            case GameLevel.HARD:
                gameLevel = GameLevel.EASY;
                break;
        }
        updateFabImage();
        tryToStartGame();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) @SuppressWarnings("unused") // invoked by event bus
    public void onEventMainThread(SerialPictureLoadEvent event) {
        if (event.result == Result.Success && event.serialPicture.uuid.equals(uuid)) {
            this.serialPicture = event.serialPicture;
            tryToStartGame();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) @SuppressWarnings("unused") // invoked by event bus
    public void onEventMainThread(BitmapLoadEvent event) {
        if (event.result == Result.Success && event.uuid.equals(uuid)) {
            this.bitmap = event.bitmap;
            tryToStartGame();
        }
    }


    private void tryToStartGame() {
        if (!hasResumed()) {
            return;
        }
        if (gameView.isStarted()) {
            return;
        }
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        if (serialPicture == null) {
            return;
        }
        startGame();
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

    private GameView.GameListener gameListener = new GameView.GameListener() {
        @Override
        public void onGameStart() {

        }

        @Override
        public void onGameOver() {
            updateSerialPicture();
            Snackbar.make(gameView, "Game Over", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

        @Override
        public void onGameEnd() {
            updateSerialPicture();
        }
    };

    private void updateSerialPicture() {
        if(gameView.isStarted()) {
            List<Point> gameData = gameView.getGameData();
            switch (GameLevel.getLevel(gameData)) {
                case GameLevel.EASY:
                    serialPicture.easyData = DisorderUtil.encode(gameData);
                    break;
                case GameLevel.MEDIUM:
                    serialPicture.mediumData = DisorderUtil.encode(gameData);
                    break;
                case GameLevel.HARD:
                    serialPicture.hardData = DisorderUtil.encode(gameData);
                    break;
            }
            serialPictureManager.updateSerialPicture(serialPicture);
        }
    }

    private void updateTheme(int primaryColor, int secondaryColor) {
        setWindowStatusBarColor(primaryColor);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(primaryColor));
        fab.setBackgroundTintList(ColorStateList.valueOf(secondaryColor));
    }

    private void updateFabImage() {
        switch (gameLevel) {
            case GameLevel.EASY:
                fab.setImageResource(R.drawable.ic_one);
                break;
            case GameLevel.MEDIUM:
                fab.setImageResource(R.drawable.ic_two);
                break;
            case GameLevel.HARD:
                fab.setImageResource(R.drawable.ic_three);
                break;
        }
    }

}
