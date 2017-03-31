package com.khgame.picturepuzzle.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.khgame.sdk.picturepuzzle.base.SquaredActivity;
import com.khgame.sdk.picturepuzzle.model.BitmapEntry;
import com.khgame.sdk.picturepuzzle.model.ClassicPicture;
import com.khgame.sdk.picturepuzzle.operation.LoadPictureOperation;
import com.khgame.sdk.picturepuzzle.operation.Operation;
import com.khgame.sdk.picturepuzzle.core.DisorderUtil;
import com.khgame.sdk.picturepuzzle.core.GameLevel;
import com.khgame.sdk.picturepuzzle.core.Point;
import com.khgame.sdk.picturepuzzle.db.operation.GetClassicPictureByIdOperation;
import com.khgame.sdk.picturepuzzle.db.operation.UpdateClassicPictureOperation;
import com.khgame.picturepuzzle.R;
import com.khgame.picturepuzzle.ui.view.GameView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClassicGameActivity extends SquaredActivity {

    private String uuid;
    private int gameLevel;
    private ClassicPicture picture;
    private Bitmap bitmap;

    @BindView(R.id.gameview)
    GameView gameView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_game);
        ButterKnife.bind(this);
        gameView.setGameOverListener(gameOverListener);

        gameLevel = getIntent().getIntExtra("GameLevel", GameLevel.EASY);
        uuid = getIntent().getStringExtra("uuid");
        initGameData();
        updateFabImage();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(gameView.isStarted()) {
            saveDataToDB();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    void onClickFab() {
        saveDataToDB();

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
        startGame();
    }

    private void initGameData() {
        new GetClassicPictureByIdOperation(uuid).callback(new Operation.Callback<ClassicPicture, Void>(){
            @Override
            public void onSuccess(ClassicPicture picture) {
                ClassicGameActivity.this.picture = picture;

                new LoadPictureOperation(picture.uuid, null).callback(new Operation.Callback<BitmapEntry, Void>() {
                    @Override
                    public void onSuccessMainThread(BitmapEntry bitmapEntry) {
                        ClassicGameActivity.this.bitmap = bitmapEntry.bitmap;
                        startGame();
                    }
                }).execute();
            }
        }).enqueue();
    }

    private void startGame() {
        List<Point> gameData = null;
        switch (gameLevel) {
            case GameLevel.EASY:
                gameData = DisorderUtil.decode(picture.easyData);
                break;
            case GameLevel.MEDIUM:
                gameData = DisorderUtil.decode(picture.mediumData);
                break;
            case GameLevel.HARD:
                gameData = DisorderUtil.decode(picture.hardData);
                break;
        }
        gameView.start(gameData, bitmap);
    }

    private GameView.GameOverListener gameOverListener = new GameView.GameOverListener() {
        @Override
        public void onGameOver() {
            saveDataToDB();
            Snackbar.make(gameView, "Game Over", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    };


    private void saveDataToDB() {
        switch (gameLevel) {
            case GameLevel.EASY:
                picture.easyData = DisorderUtil.encode(gameView.getGameData());
                break;
            case GameLevel.MEDIUM:
                picture.mediumData = DisorderUtil.encode(gameView.getGameData());
                break;
            case GameLevel.HARD:
                picture.hardData = DisorderUtil.encode(gameView.getGameData());
                break;
        }
        new UpdateClassicPictureOperation(picture).enqueue();
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
