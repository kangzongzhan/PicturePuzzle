package com.khgame.picturepuzzle.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.LinearInterpolator;

import com.khgame.picturepuzzle.R;
import com.khgame.picturepuzzle.common.UIUtils;

/**
 * Created by zkang on 2017/4/16.
 */

public class CountDownView extends View {

    private long duration = 8000;
    private float progress = 0f; // 0 - 100
    private int color1;
    private int color2;
    private Paint paint;
    private boolean isStarted = false;
    private TimeOutListener listener;

    public CountDownView(Context context) {
        super(context);
        this.setBackgroundResource(R.drawable.circle_background);
        setOutLine();
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundResource(R.drawable.circle_background);
        setOutLine();
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setBackgroundResource(R.drawable.circle_background);
        setOutLine();
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setTimeOutListener(TimeOutListener listener) {
        this.listener = listener;
    }

    public void setColor(int primary, int secondary) {
        this.color1 = primary;
        this.color2 = secondary;
        this.setBackgroundTint(primary);
        initPaint();
    }

    public void start() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "progress", 100f);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener((animation) -> {
            this.setProgress(animation.getAnimatedFraction());
            this.invalidate();
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setProgress(100f);
                if (listener != null) {
                    listener.timeOut();
                }
            }
        });
        animator.setDuration(duration);
        animator.start();
    }

    float[] center = new float[2];
    float radius;
    @Override
    protected void onDraw(Canvas canvas) {
        initPoints();
        canvas.clipPath(getClipPath());
        canvas.drawCircle(center[0], center[1], radius, paint);
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(color2);
        paint.setAntiAlias(true);
    }

    private void initPoints() {
        center = getCoors();
        radius = getRadius();
    }

    private float[] getCoors() {
        float[] coors = new float[2];
        coors[0] = getPaddingLeft() + (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / 2;
        coors[1] = getPaddingTop() + (getMeasuredHeight() - getPaddingTop() - getPaddingBottom()) / 2;
        return coors;
    }

    private float getRadius() {
        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

        if (width > height) {
            return (float) height / 2;
        } else {
            return (float) width / 2;
        }
    }

    private Path getClipPath() {
        float degree = 3.6f * progress;
        double radians = Math.PI * degree / 180;
        Path path = new Path();
        path.moveTo(center[0], center[1]);
        path.lineTo(center[0], center[1] - radius);
        if (degree < 45f) {
            path.lineTo((float) (center[0] + Math.tan(radians) * radius), center[1] - radius);
        } else if (degree >= 45f && degree < 135f) {
            path.lineTo(center[0] + radius, center[1] - radius);
            path.lineTo(center[0] + radius, center[1] - (float) Math.tan(0.5 * Math.PI - radians) * radius);
        } else if (degree >= 135f && degree < 225f) {
            path.lineTo(center[0] + radius, center[1] - radius);
            path.lineTo(center[0] + radius, center[1] + radius);
            path.lineTo(center[0] + (float) Math.tan(Math.PI - radians) * radius, center[1] + radius);
        } else if (degree >= 225f && degree < 315f) {
            path.lineTo(center[0] + radius, center[1] - radius);
            path.lineTo(center[0] + radius, center[1] + radius);
            path.lineTo(center[0] - radius, center[1] + radius);
            path.lineTo(center[0] - radius, center[1] + (float) Math.tan(1.5 * Math.PI - radians) * radius);
        } else if (degree >= 315f) {
            path.lineTo(center[0] + radius, center[1] - radius);
            path.lineTo(center[0] + radius, center[1] + radius);
            path.lineTo(center[0] - radius, center[1] + radius);
            path.lineTo(center[0] - radius, center[1] - radius);
            path.lineTo((float) (center[0] + Math.tan(radians) * radius), center[1] - radius);
        }
        path.close();
        return path;
    }

    @TargetApi(21)
    private void setOutLine() {
        if (UIUtils.hasLollipop())
            this.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
    }
    @TargetApi(21)
    private void setBackgroundTint(int color) {
        if (UIUtils.hasLollipop())
            this.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    public interface TimeOutListener {
        void timeOut();
    }
}
