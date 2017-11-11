package com.khgame.picturepuzzle.ui.activity;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.khgame.picturepuzzle.ui.view.CountDownView;
import com.khgame.picturepuzzle.base.SquaredActivity;
import com.khgame.picturepuzzle.classic.ClassicPictureManager;
import com.khgame.picturepuzzle.classic.ClassicPictureManagerImpl;
import com.khgame.picturepuzzle.common.BitmapManager;
import com.khgame.picturepuzzle.common.BitmapManagerImpl;
import com.khgame.picturepuzzle.common.Result;
import com.khgame.picturepuzzle.events.BitmapLoadEvent;
import com.khgame.picturepuzzle.events.ClassicPictureLoadEvent;
import com.khgame.picturepuzzle.model.ClassicPicture;
import com.khgame.picturepuzzle.core.DisorderUtil;
import com.khgame.picturepuzzle.core.GameLevel;
import com.khgame.picturepuzzle.core.Point;
import com.khgame.picturepuzzle.R;
import com.khgame.picturepuzzle.ui.view.GameView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClassicGameActivity extends SquaredActivity {

    public static final String CLASSICPICTURE_UUID = "CLASSICPICTURE_UUID";
    public static final String GAME_LEVEL = "GAME_LEVEL";

    private ClassicPictureManager classicPictureManager = ClassicPictureManagerImpl.getInstance();
    private BitmapManager bitmapManager = BitmapManagerImpl.getInstance();
    private String uuid;
    private int gameLevel;

    private ClassicPicture classicPicture;
    private Bitmap bitmap;

    @BindView(R.id.gameview)
    GameView gameView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.countdown)
    CountDownView countDownView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_game);
        ButterKnife.bind(this);

        uuid = getIntent().getStringExtra(ClassicGameActivity.CLASSICPICTURE_UUID);
        gameLevel = getIntent().getIntExtra(ClassicGameActivity.GAME_LEVEL, GameLevel.EASY);
        gameView.setGameListener(gameListener);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        updateFabImage();
        updateTheme(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent));
        countDownView.setColor(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent));
        countDownView.setDuration(8000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        classicPictureManager.getClassicPictureByUuid(uuid);
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
        updateClassicPicture();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_game_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.tips) {
            showTips();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    void onClickFab() {
        updateClassicPicture();
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
        getIntent().putExtra(GAME_LEVEL, gameLevel);
        updateFabImage();
        tryToStartGame();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) @SuppressWarnings("unused") // invoked by event bus
    public void onEventMainThread(ClassicPictureLoadEvent event) {
        Log.d("EventMainThread", "ClassicPictureLoadEvent");
        if (event.result == Result.Success && TextUtils.equals(event.classicPicture.uuid, uuid)) {
            classicPicture = event.classicPicture;
            tryToStartGame();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) @SuppressWarnings("unused") // invoked by event bus
    public void onEventMainThread(BitmapLoadEvent event) {
        if (event.result == Result.Success && event.uuid.equals(uuid)) {
            bitmap = event.bitmap;
            tryToStartGame();
        }
    }

    private void tryToStartGame() {
        if (!hasResumed()) {
            Log.d("ClassicTryStartGame", "Activity has not resumed");
            return;
        }
        if (gameView.isStarted()) {
            Log.d("ClassicTryStartGame", "Game has started");
            return;
        }
        if (bitmap == null || bitmap.isRecycled()) {
            Log.d("ClassicTryStartGame", "Bitmap is invalid");
            return;
        }
        if (classicPicture == null) {
            Log.d("ClassicTryStartGame", "ClassicPicture is invalid");
            return;
        }
        startGame();
    }

    private void startGame() {
        List<Point> gameData = null;
        switch (gameLevel) {
            case GameLevel.EASY:
                gameData = DisorderUtil.decode(classicPicture.easyData);
                break;
            case GameLevel.MEDIUM:
                gameData = DisorderUtil.decode(classicPicture.mediumData);
                break;
            case GameLevel.HARD:
                gameData = DisorderUtil.decode(classicPicture.hardData);
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
            updateClassicPicture();
            Snackbar.make(gameView, "Game Over", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

        @Override
        public void onGameEnd() {
            updateClassicPicture();
        }
    };

    private void updateClassicPicture() {
        if (gameView.isStarted()) {
            List<Point> gameData = gameView.getGameData();
            switch (GameLevel.getLevel(gameData)) {
                case GameLevel.EASY:
                    classicPicture.easyData = DisorderUtil.encode(gameData);
                    break;
                case GameLevel.MEDIUM:
                    classicPicture.mediumData = DisorderUtil.encode(gameData);
                    break;
                case GameLevel.HARD:
                    classicPicture.hardData = DisorderUtil.encode(gameData);
                    break;
            }
            classicPictureManager.updateClassicPicture(classicPicture);
        }
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

    private void updateTheme(int primaryColor, int secondaryColor) {
        setWindowStatusBarColor(primaryColor);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(primaryColor));
        fab.setBackgroundTintList(ColorStateList.valueOf(secondaryColor));
    }

    private void showTips() {
        if (gameView.isShowingPicture()) {
            return;
        }

        gameView.moveToRealPoint();

        Animation scaleOut = AnimationUtils.loadAnimation(this, R.anim.scale_out);
        scaleOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                countDownView.setTimeOutListener(() -> tipsEnd());
                countDownView.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fab.setVisibility(View.INVISIBLE);
                Animation scaleIn = AnimationUtils.loadAnimation(ClassicGameActivity.this, R.anim.scale_in);
                scaleIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        countDownView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                countDownView.startAnimation(scaleIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fab.startAnimation(scaleOut);
    }

    private void tipsEnd() {

        if (!gameView.isShowingPicture()) {
            return;
        }

        gameView.moveToNowPoint();

        Animation scaleOut = AnimationUtils.loadAnimation(this, R.anim.scale_out);
        scaleOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                countDownView.setVisibility(View.INVISIBLE);
                Animation scaleIn = AnimationUtils.loadAnimation(ClassicGameActivity.this, R.anim.scale_in);
                scaleIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        fab.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                fab.startAnimation(scaleIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        countDownView.startAnimation(scaleOut);
    }

}
