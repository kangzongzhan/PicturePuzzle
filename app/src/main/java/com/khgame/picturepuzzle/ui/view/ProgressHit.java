package com.khgame.picturepuzzle.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.khgame.sdk.picturepuzzle.core.DisorderUtil;
import com.khgame.sdk.picturepuzzle.core.Point;
import com.khgame.picturepuzzle.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.khgame.sdk.picturepuzzle.core.GameLevel.xNums;

/**
 * Created by zkang on 2017/3/5.
 */

public class ProgressHit extends FrameLayout {

    @BindView(R.id.easy)
    View easy;

    @BindView(R.id.medium)
    View medium;

    @BindView(R.id.hard)
    View hard;

    public ProgressHit(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.progress_hint, this);
        ButterKnife.bind(this);
    }

    public ProgressHit(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.progress_hint, this);
        ButterKnife.bind(this);
    }

    public ProgressHit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.progress_hint, this);
        ButterKnife.bind(this);
    }

    public void setGameData(String easyData, String mediumData, String hardData) {
        if (gameOver(easyData)) {
            easy.setBackgroundResource(R.drawable.hint_finish);
        } else {
            easy.setBackgroundResource(R.drawable.hint_unfinish);
        }

        if (gameOver(mediumData)) {
            medium.setBackgroundResource(R.drawable.hint_finish);
        } else {
            medium.setBackgroundResource(R.drawable.hint_unfinish);
        }

        if (gameOver(hardData)) {
            hard.setBackgroundResource(R.drawable.hint_finish);
        } else {
            hard.setBackgroundResource(R.drawable.hint_unfinish);
        }
    }

    private boolean gameOver(String data) {
        List<Point> gameData = DisorderUtil.decode(data);
        boolean gameOver = true;
        for (Point point: gameData) {
            int index = gameData.indexOf(point);
            gameOver = gameOver && (index == (xNums(gameData) * point.y + point.x));
        }
        return gameOver;
    }


}
