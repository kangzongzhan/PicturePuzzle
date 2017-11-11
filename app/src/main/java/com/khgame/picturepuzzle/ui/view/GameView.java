package com.khgame.picturepuzzle.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.khgame.picturepuzzle.R;
import com.khgame.picturepuzzle.BuildConfig;
import com.khgame.picturepuzzle.core.DisorderUtil;
import com.khgame.picturepuzzle.core.GameLevel;
import com.khgame.picturepuzzle.core.Point;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.khgame.picturepuzzle.core.GameLevel.xNums;
import static com.khgame.picturepuzzle.core.GameLevel.yNums;

/**
 * Created by KangZongZhan on 2017/1/18.
 */

public class GameView extends FrameLayout {

    private FrameLayout backLayout; // 背景
    private FrameLayout fontLayout; // 前景

    private List<Point> gameData;
    private int gameLevel;
    private int xNums, yNums;
    private Bitmap bitmap;

    private int xOffset;
    private int yOffset;
    private int unitWidth;
    private int unitHeight;
    private boolean isStarted = false;
    private boolean isAnimating = false;
    private boolean isShowingPicture = false; // is showing the real picture
    private static final int MIN_DISTANCE = 30;
    private static final int ANIMATION_DURATION = 150;
    private static final int TIPS_DURATION = 700;
    private Set<PieceViewHolder> viewSet = new HashSet<>();
    private GameListener gameListener = DefaultListener;
    private PieceViewHolder emptyView;
    private int backPieceColor = Color.GRAY;
    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        backLayout = new FrameLayout(getContext());
        addView(backLayout, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        fontLayout = new FrameLayout(getContext());
        addView(fontLayout, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public void start(List<Point> gameData, Bitmap bitmap) {
        if (gameData == null) {
            throw new NullPointerException("GameData cannot be null");
        }
        if (bitmap == null) {
            throw new NullPointerException("Bitmap cannot be null");
        }
        this.gameData = gameData;
        this.gameLevel = GameLevel.getLevel(gameData);
        this.xNums = xNums(gameData);
        this.yNums = yNums(gameData);
        this.bitmap = bitmap;
        this.isStarted = true;
        this.invalidate();
        this.post(() -> {
            calculateOffset();
            animateIn();
        });
        this.gameListener.onGameStart();
    }

    public void end() {
        this.gameData = null;
        this.bitmap = null;
        this.isStarted = false;
        this.gameListener.onGameEnd();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private float downX, downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downX = event.getX();
            downY = event.getY();
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float distanceX = Math.abs(downX - event.getX());
            float distanceY = Math.abs(downY - event.getY());
            if (distanceX < MIN_DISTANCE && distanceY < MIN_DISTANCE) {
                return super.onTouchEvent(event);
            }
            if (distanceX > distanceY) { // 左右滑动
                if (downX > event.getX()) {
                    swipLeft();
                } else {
                    swipRight();
                }
            } else { // 上下滑动
                if (downY > event.getY()) {
                    swipUp();
                } else {
                    swipDown();
                }
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void swipUp() {
        if (isAnimating || isShowingPicture) {
            return;
        }

        PieceViewHolder targetView = downViewOfEmpty();
        if (targetView == null) {
            return;
        }
        if (DisorderUtil.swipUp(gameData)) {
            targetView.swipUp();
            emptyView.swipDown();
        }
    }
    public void swipDown() {
        if (isAnimating || isShowingPicture) {
            return;
        }

        PieceViewHolder targetView = upViewOfEmpty();
        if (targetView == null) {
            return;
        }

        if (DisorderUtil.swipDown(gameData)) {
            targetView.swipDown();
            emptyView.swipUp();
        }
    }
    public void swipLeft() {
        if (isAnimating || isShowingPicture) {
            return;
        }

        PieceViewHolder targetView = rightViewOfEmpty();
        if (targetView == null) {
            return;
        }
        if (DisorderUtil.swipLeft(gameData)) {
            targetView.swipLeft();
            emptyView.swipRight();
        }
    }

    public void swipRight() {
        if (isAnimating || isShowingPicture) {
            return;
        }

        PieceViewHolder targetView = leftViewOfEmpty();
        if (targetView == null) {
            return;
        }
        if (DisorderUtil.swipRight(gameData)) {
            targetView.swipRight();
            emptyView.swipLeft();
        }
    }

    public void moveToRealPoint() {
        if (isShowingPicture || isAnimating) {
            return;
        }
        isShowingPicture = true;
        for (PieceViewHolder viewHolder : viewSet) {
            viewHolder.moveToRealPoint();
        }
    }

    public void moveToNowPoint() {
        if (!isShowingPicture || isAnimating) {
            return;
        }
        isShowingPicture = false;
        for (PieceViewHolder viewHolder : viewSet) {
            viewHolder.moveToNowPoint();
        }
    }

    public void animateIn() {
        backLayout.removeAllViews();
        fontLayout.removeAllViews();
        viewSet.clear();
        final int w = bitmap.getWidth() / xNums(gameData);
        final int h = bitmap.getHeight() / yNums(gameData);
        for (Point realPoint : gameData) {
            Point nowPoint = new Point();
            nowPoint.x = gameData.indexOf(realPoint) % xNums(gameData);
            nowPoint.y = gameData.indexOf(realPoint) / xNums(gameData);

            Bitmap b = null;
            if (realPoint.y != yNums(gameData)) {
                b = Bitmap.createBitmap(bitmap, realPoint.x * w, realPoint.y * h, w, h);
            }
            addBackView(nowPoint, realPoint);
            addFontView(nowPoint, realPoint, b);
        }
    }
    private void addFontView(Point nowPoint, Point realPoint, Bitmap bitmap) {
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.piece_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        LayoutParams lp = new LayoutParams(unitWidth, unitHeight);
        lp.leftMargin = xOffset + nowPoint.x * unitWidth;
        lp.topMargin = yOffset + nowPoint.y * unitHeight;
        fontLayout.addView(view, lp);

        PieceViewHolder viewHolder = new PieceViewHolder(view, realPoint, nowPoint);
        viewSet.add(viewHolder);

        if (realPoint.x == 0 && realPoint.y == yNums) {
            emptyView = viewHolder;
        }

    }

    private void addBackView(Point nowPoint, Point realPoint) {
        View view = new View(getContext());
        view.setBackgroundColor(backPieceColor);
        LayoutParams lp = new LayoutParams(unitWidth, unitHeight);
        lp.leftMargin = xOffset + nowPoint.x * unitWidth;
        lp.topMargin = yOffset + nowPoint.y * unitHeight;
        backLayout.addView(view, lp);
    }

    private void calculateOffset() {

        final int xN = xNums(gameData); // 图片分割片数
        final int yN = yNums(gameData);

        final int bitmapW = bitmap.getWidth(); // 图片尺寸
        final int bitmapH = bitmap.getHeight();

        final double maxW = getMeasuredWidth(); // 容器尺寸
        final double maxH = getMeasuredHeight();

        int bitmapWidth = bitmapW;
        int bitmapHeight = bitmapH * (yN + 1) / yN;

        double scale;

        if (maxH / bitmapHeight > maxW / bitmapWidth) {
            scale = maxW / bitmapWidth;
        } else {
            scale = maxH / bitmapHeight;
        }

        xOffset = (int) (maxW - bitmapWidth * scale) / 2;
        yOffset = (int) (maxH - bitmapHeight * scale) / 2;

        unitWidth = (int) (bitmapW * scale / xN);
        unitHeight = (int) (bitmapH * scale / yN);
    }


    private PieceViewHolder upViewOfEmpty() {
        final Point nowPointOfEmpty = emptyView.nowPoint;
        Point upPoint = new Point();
        upPoint.x = nowPointOfEmpty.x;
        upPoint.y = nowPointOfEmpty.y - 1;
        if (!isValidePoint(upPoint)) {
            return null;
        }
        return getViewHolderByNowPoint(upPoint);
    }

    private PieceViewHolder downViewOfEmpty() {
        final Point nowPointOfEmpty = emptyView.nowPoint;
        Point downPoint = new Point();
        downPoint.x = nowPointOfEmpty.x;
        downPoint.y = nowPointOfEmpty.y + 1;
        if (!isValidePoint(downPoint)) {
            return null;
        }
        return getViewHolderByNowPoint(downPoint);
    }

    private PieceViewHolder leftViewOfEmpty() {
        final Point nowPointOfEmpty = emptyView.nowPoint;
        Point leftPoint = new Point();
        leftPoint.x = nowPointOfEmpty.x - 1;
        leftPoint.y = nowPointOfEmpty.y;
        if (!isValidePoint(leftPoint)) {
            return null;
        }
        return getViewHolderByNowPoint(leftPoint);
    }

    private PieceViewHolder rightViewOfEmpty() {
        final Point nowPointOfEmpty = emptyView.nowPoint;
        Point rightPoint = new Point();
        rightPoint.x = nowPointOfEmpty.x + 1;
        rightPoint.y = nowPointOfEmpty.y;
        if (!isValidePoint(rightPoint)) {
            return null;
        }
        return getViewHolderByNowPoint(rightPoint);
    }

    private Point getNowPointOfEmpty(List<Point> gameData) {
        Point point = new Point();
        point.x = 0;
        point.y = yNums(gameData);
        int index = gameData.indexOf(point);

        point.x = index % xNums(gameData);
        point.y = index / xNums(gameData);

        return point;
    }

    private boolean isValidePoint(Point point) {
        int index = point.y * xNums + point.x;
        if (index < 0 || index >= gameData.size()) {
            return false;
        }

        if (point.x < 0 || point.y < 0 || point.x >= xNums || point.y > yNums) {
            return false;
        }

        return true;
    }
    private PieceViewHolder getViewHolderByRealPoint(Point realPoint) {
        for (PieceViewHolder viewHolder:viewSet) {
            if (viewHolder.realPoint.equals(realPoint)) {
                return viewHolder;
            }
        }
        return null;
    }
    private PieceViewHolder getViewHolderByNowPoint(Point nowPoint) {
        for (PieceViewHolder viewHolder:viewSet) {
            if (viewHolder.nowPoint.equals(nowPoint)) {
                return viewHolder;
            }
        }
        return null;
    }

    private boolean gameOver() {
        boolean gameOver = true;
        for (Point point: gameData) {
            int index = gameData.indexOf(point);
            gameOver = gameOver && (index == (xNums(gameData) * point.y + point.x));
        }
        return gameOver;
    }

    public boolean isStarted() {
        return isStarted;
    }
    public boolean isShowingPicture() {
        return isShowingPicture;
    }
    public List<Point> getGameData() {
        return gameData;
    }
    public void setGameListener(GameListener listener) {
        this.gameListener = listener;
    }
    public interface GameListener {
        void onGameStart(); // start
        void onGameOver(); // finish
        void onGameEnd(); // end
    }
    private static final GameListener DefaultListener = new GameListener() {
        @Override
        public void onGameStart() {

        }

        @Override
        public void onGameOver() {
        }

        @Override
        public void onGameEnd() {

        }
    };

    class PieceViewHolder {
        private View view;
        private ImageView imageView;
        private LinearLayout infoLayout;
        private TextView realText;
        private TextView nowText;
        private Point realPoint;
        private Point nowPoint;
        private PieceViewHolder(View view, Point realPoint, Point nowPoint) {
            this.view = view;
            this.imageView = (ImageView) view.findViewById(R.id.image);
            this.infoLayout = (LinearLayout) view.findViewById(R.id.piece_info);
            this.realText = (TextView) view.findViewById(R.id.realPoint);
            this.nowText = (TextView) view.findViewById(R.id.nowPoint);
            this.realPoint = realPoint;
            this.nowPoint = nowPoint;
            updateText();
        }

        private void updateText() {
            if (BuildConfig.DEBUG) {
                infoLayout.setVisibility(VISIBLE);
                realText.setText("Real:" + realPoint.toString());
                nowText.setText("Now:" + nowPoint.toString());
            } else {
                infoLayout.setVisibility(GONE);
            }
        }

        private void swipUp() {
            view.animate().yBy(-unitHeight).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    nowPoint.y--;
                    updateText();
                    isAnimating = false;
                    if (gameOver()) {
                        gameListener.onGameOver();
                    }
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    isAnimating = true;
                }
            }).setDuration(ANIMATION_DURATION).start();
        }

        private void swipDown() {
            view.animate().yBy(unitHeight).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    nowPoint.y++;
                    updateText();
                    isAnimating = false;
                    if (gameOver()) {
                        gameListener.onGameOver();
                    }
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    isAnimating = true;
                }
            }).setDuration(ANIMATION_DURATION).start();
        }

        private void swipLeft() {
            view.animate().xBy(-unitWidth).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    nowPoint.x--;
                    updateText();
                    isAnimating = false;
                    if (gameOver()) {
                        gameListener.onGameOver();
                    }
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    isAnimating = true;
                }
            }).setDuration(ANIMATION_DURATION).start();
        }

        private void swipRight() {
            view.animate().xBy(unitWidth).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    nowPoint.x++;
                    updateText();
                    isAnimating = false;
                    if (gameOver()) {
                        gameListener.onGameOver();
                    }
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    isAnimating = true;
                }
            }).setDuration(ANIMATION_DURATION).start();
        }

        private void moveToRealPoint() {
            AnimatorSet animationSet = new AnimatorSet();
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "x",  view.getX() + unitWidth * (realPoint.x - nowPoint.x));
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "y",  view.getY() + unitHeight * (realPoint.y - nowPoint.y));
            animationSet.playTogether(animator1, animator2);
            animationSet.setDuration(TIPS_DURATION);
            animationSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    isAnimating = false;
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    isAnimating = true;
                }
            });
            animationSet.start();
        }

        private void moveToNowPoint() {
            AnimatorSet animationSet = new AnimatorSet();
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "x", view.getX() + unitWidth * (nowPoint.x - realPoint.x));
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "y", view.getY() + unitHeight * (nowPoint.y - realPoint.y));
            animationSet.playTogether(animator1, animator2);
            animationSet.setDuration(TIPS_DURATION);
            animationSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    isAnimating = false;
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    isAnimating = true;
                }
            });
            animationSet.start();
        }
    }

}
