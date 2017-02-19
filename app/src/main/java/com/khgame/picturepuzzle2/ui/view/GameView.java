package com.khgame.picturepuzzle2.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.khgame.picturepuzzle.core.DisorderUtil;
import com.khgame.picturepuzzle.core.Point;
import com.khgame.picturepuzzle2.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.khgame.picturepuzzle.core.GameLevel.xNums;
import static com.khgame.picturepuzzle.core.GameLevel.yNums;

/**
 * Created by KangZongZhan on 2017/1/18.
 */

public class GameView extends FrameLayout {

    private List<Point> gameData;
    private Bitmap bitmap;

    private int xOffset;
    private int yOffset;
    private int unitWidth;
    private int unitHeight;
    private boolean isStarted = false;
    private boolean isSwipping = false;
    private int MIN_DISTANCE = 30;
    private int ANIMATION_DURATION = 150;
    private Map<Point, View> viewMap = new HashMap<>();
    private GameOverListener gameOverListener = DefaultListener;

    public GameView(Context context) {
        super(context);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
    }

    public void start(List<Point> gameData, Bitmap bitmap) {
        if(gameData == null) {
            throw new NullPointerException("GameData cannot be null");
        }
        if(bitmap == null) {
            throw new NullPointerException("Bitmap cannot be null");
        }
        this.gameData = gameData;
        this.bitmap = bitmap;
        this.isStarted = true;
        this.invalidate();
        this.post(new Runnable() {
                @Override
                public void run() {
                    calculateOffset();
                    animateIn();
                }
            });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private float downX, downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            downX = event.getX();
            downY = event.getY();
            return true;
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            float distanceX = Math.abs(downX - event.getX());
            float distanceY = Math.abs(downY - event.getY());
            if(distanceX < MIN_DISTANCE && distanceY < MIN_DISTANCE) {
                return super.onTouchEvent(event);
            }
            if(distanceX > distanceY) { // 左右滑动
                if(downX > event.getX()) {
                    swipLeft();
                } else {
                    swipRight();
                }
            } else { // 上下滑动
                if(downY > event.getY()) {
                    swipUp();
                } else {
                    swipDown();
                }
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void swipUp() {
        if(isSwipping) {
            return;
        }

        View targetView = downViewOfEmpty();
        if(targetView == null) {
            return;
        }
        gameData = DisorderUtil.swipUp(gameData);
        targetView.animate().yBy(-unitHeight).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isSwipping = false;
                if(gameOver()) {
                    gameOverListener.onGameOver();
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                isSwipping = true;
            }
        }).setDuration(ANIMATION_DURATION).start();

    }
    private void swipDown() {
        if(isSwipping) {
            return;
        }

        View targetView = upViewOfEmpty();
        if(targetView == null) {
            return;
        }
        gameData = DisorderUtil.swipDown(gameData);
        targetView.animate().yBy(unitHeight).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isSwipping = false;
                if(gameOver()) {
                    gameOverListener.onGameOver();
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                isSwipping = true;
            }
        }).setDuration(ANIMATION_DURATION).start();

    }
    private void swipLeft() {
        if(isSwipping) {
            return;
        }

        View targetView = rightViewOfEmpty();
        if(targetView == null) {
            return;
        }
        gameData = DisorderUtil.swipLeft(gameData);
        targetView.animate().xBy(-unitWidth).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isSwipping = false;
                if(gameOver()) {
                    gameOverListener.onGameOver();
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                isSwipping = true;
            }
        }).setDuration(ANIMATION_DURATION).start();


    }
    private void swipRight() {
        if(isSwipping) {
            return;
        }

        View targetView = leftViewOfEmpty();
        if(targetView == null) {
            return;
        }
        gameData = DisorderUtil.swipRight(gameData);
        targetView.animate().xBy(unitWidth).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isSwipping = false;
                if(gameOver()) {
                    gameOverListener.onGameOver();
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                isSwipping = true;
            }
        }).setDuration(ANIMATION_DURATION).start();


    }

    public void animateIn() {
        this.removeAllViews();
        viewMap.clear();
        final int w = bitmap.getWidth() / xNums(gameData);
        final int h = bitmap.getHeight() / yNums(gameData);
        for(Point realPoint : gameData) {
            Point nowPoint = new Point();
            nowPoint.x = gameData.indexOf(realPoint) % xNums(gameData);
            nowPoint.y = gameData.indexOf(realPoint) / xNums(gameData);

            Bitmap b = null;
            if(realPoint.y != yNums(gameData)) {
                b = Bitmap.createBitmap(bitmap, realPoint.x * w, realPoint.y * h, w, h);
            }
            addView(nowPoint, realPoint, b);
        }
    }
    private void addView(Point nowPoint, Point realPoint, Bitmap bitmap) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        LayoutParams lp = new LayoutParams(unitWidth, unitHeight);
        lp.leftMargin = xOffset + nowPoint.x * unitWidth;
        lp.topMargin = yOffset + nowPoint.y * unitHeight;
        addView(imageView, lp);
        viewMap.put(realPoint, imageView);
    }

    private void calculateOffset() {

        final int xN = xNums(gameData); // 图片分割片数
        final int yN = yNums(gameData);

        final int bitmapW = bitmap.getWidth(); // 图片尺寸
        final int bitmapH = bitmap.getHeight();

        final double maxW = getMeasuredWidth(); // 容器尺寸
        final double maxH = getMeasuredHeight();

        int bitmapW_ = bitmapW;
        int bitmapH_ = bitmapH * (yN + 1) / yN;

        double scale;

        if (maxH / bitmapH_ > maxW / bitmapW_) {
            scale = maxW / bitmapW_;
        } else {
            scale = maxH / bitmapH_;
        }

        xOffset = (int) (maxW - bitmapW_ * scale) / 2;
        yOffset = (int) (maxH - bitmapH_ * scale) / 2;

        unitWidth = (int) (bitmapW * scale / xN);
        unitHeight = (int) (bitmapH * scale / yN);
    }


    private View upViewOfEmpty() {
        int indexOfDownEmpty = indexOfEmpty(gameData) - xNums(gameData);
        if(!isValideIndex(indexOfDownEmpty)) {
            return null;
        }
        Point upPointOfEmpty = gameData.get(indexOfDownEmpty);
        return viewMap.get(upPointOfEmpty);
    }
    private View downViewOfEmpty() {
        int indexOfDownEmpty = indexOfEmpty(gameData) + xNums(gameData);
        if(!isValideIndex(indexOfDownEmpty)) {
            return null;
        }
        Point downPointOfEmpty = gameData.get(indexOfDownEmpty);
        return viewMap.get(downPointOfEmpty);
    }
    private View leftViewOfEmpty() {
        int indexOfDownEmpty = indexOfEmpty(gameData) - 1;
        if(!isValideIndex(indexOfDownEmpty) || indexOfEmpty(gameData) % xNums(gameData) == 0) {
            return null;
        }
        Point leftPointOfEmpty = gameData.get(indexOfDownEmpty);
        return viewMap.get(leftPointOfEmpty);
    }
    private View rightViewOfEmpty() {
        int indexOfDownEmpty = indexOfEmpty(gameData) + 1;
        if(!isValideIndex(indexOfDownEmpty) || (indexOfEmpty(gameData) + 1) % xNums(gameData) == 0) {
            return null;
        }
        Point rightPointOfEmpty = gameData.get(indexOfDownEmpty);
        return viewMap.get(rightPointOfEmpty);
    }

    private int indexOfEmpty(List<Point> gameData) {
        Point ponit = new Point();
        ponit.x = 0;
        ponit.y = yNums(gameData);
        return gameData.indexOf(ponit);
    }

    private boolean isValideIndex(int index) {
        if(index < 0 || index >= gameData.size()) {
            return false;
        }
        return true;
    }
    private boolean gameOver() {
        boolean gameOver = true;
        for(Point point: gameData) {
            int index = gameData.indexOf(point);
            gameOver = index == (xNums(gameData) * point.y + point.x);
        }
        return gameOver;
    }

    public boolean isStarted() {
        return isStarted;
    }
    public List<Point> getGameData() {
        return gameData;
    }
    public void setGameOverListener(GameOverListener listener) {
        this.gameOverListener = listener;
    }
    public interface GameOverListener {
        void onGameOver();
    }
    private static GameOverListener DefaultListener = new GameOverListener() {
        @Override
        public void onGameOver() {
        }
    };
}
