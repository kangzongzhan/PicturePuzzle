package com.khgame.picturepuzzle2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.khgame.picturepuzzle.model.BitmapEntry;
import com.khgame.picturepuzzle.model.ClassicPicture;
import com.khgame.picturepuzzle.operation.LoadPictureOperation;
import com.khgame.picturepuzzle.operation.Operation;
import com.khgame.picturepuzzle.core.DisorderUtil;
import com.khgame.picturepuzzle.core.GameLevel;
import com.khgame.picturepuzzle.core.Point;
import com.khgame.picturepuzzle.db.operation.GetClassicPictureByIdOperation;
import com.khgame.picturepuzzle.db.operation.UpdateClassicPictureOperation;
import com.khgame.picturepuzzle2.ui.view.GameView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassicGameActivity extends AppCompatActivity {

    private long id;
    private int gameLevel;
    private ClassicPicture picture;
    private Bitmap bitmap;
    @BindView(R.id.gameview)
    GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_game);
        ButterKnife.bind(this);
        gameView.setGameOverListener(gameOverListener);

        gameLevel = getIntent().getIntExtra("GameLevel", GameLevel.EASY);
        id = getIntent().getLongExtra("ID", 0);
        initGameData();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(gameView.isStarted()) {
            List<Point> gameData = gameView.getGameData();
            new UpdateClassicPictureOperation(id, gameData).enqueue();
        }
    }

    private void initGameData() {
        new GetClassicPictureByIdOperation(id).callback(new Operation.Callback<ClassicPicture, Void>(){
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
            List<Point> gameData = gameView.getGameData();
            new UpdateClassicPictureOperation(id, gameData).enqueue();
            Snackbar.make(gameView, "Game Over", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    };

}
