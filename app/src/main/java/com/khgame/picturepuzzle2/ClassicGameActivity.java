package com.khgame.picturepuzzle2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.khgame.picturepuzzle.common.Operation;
import com.khgame.picturepuzzle.core.DisorderUtil;
import com.khgame.picturepuzzle.core.GameLevel;
import com.khgame.picturepuzzle.core.Point;
import com.khgame.picturepuzzle.db.model.ClassicPicture;
import com.khgame.picturepuzzle.db.operation.GetClassicPictureByIdOperation;
import com.khgame.picturepuzzle.db.operation.UpdateClassicPictureOperation;
import com.khgame.picturepuzzle2.operation.LoadLocalPicture;
import com.khgame.picturepuzzle2.ui.view.GameView;
import com.nostra13.universalimageloader.core.ImageLoader;

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
                new LoadLocalPicture(picture.localPath).callback(new Operation.Callback<Bitmap, String>() {
                    @Override
                    public void onSuccessMainThread(Bitmap bitmap) {
                        ClassicGameActivity.this.bitmap = bitmap;
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
