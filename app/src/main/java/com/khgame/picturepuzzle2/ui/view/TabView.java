package com.khgame.picturepuzzle2.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.khgame.picturepuzzle2.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zkang on 2017/1/7.
 */

public class TabView extends FrameLayout {

    @BindView(R.id.left)
    TextView leftButton;

    @BindView(R.id.right)
    TextView rightButton;

    private OnTabSelectListener onTabSelectListener;

    public TabView(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.tab_view, null);
        addView(view);
        ButterKnife.bind(this);
        selectLeft();
    }


    @OnClick(R.id.left)
    public  void selectLeft(){
        leftButton.setSelected(true);
        rightButton.setSelected(false);
        if(this.onTabSelectListener != null) {
            onTabSelectListener.onLeftSelect();
        }
    }

    @OnClick(R.id.right)
    public void selectRight() {
        rightButton.setSelected(true);
        leftButton.setSelected(false);
        if(this.onTabSelectListener != null) {
            onTabSelectListener.onRightSelect();
        }
    }

    public void setOnTabClickListener(OnTabSelectListener l) {
        this.onTabSelectListener = l;
    }

    public interface OnTabSelectListener {
        public void onLeftSelect();
        public void onRightSelect();
    }

}
