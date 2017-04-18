package com.khgame.sdk.picturepuzzle.operation;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Executor;

/**
 * Created by zkang on 2017/2/17.
 */

/**
 * @param <S> the result type when success
 * @param <F> the result type when failure
 */
public abstract class Operation<S, F> implements Runnable {

    private static final String TAG = "Operation";

    private long beginTime;
    private Executor executor = AsyncTask.THREAD_POOL_EXECUTOR;
    private static final Handler H = new Handler(Looper.getMainLooper());
    protected Callback callback = NullCallback;

    @Override
    public void run() {
        Log.v(TAG, this.getClass().getSimpleName() + " begin.");
        this.beginTime = System.currentTimeMillis();
        doWork();
        long costTime = System.currentTimeMillis() - this.beginTime;
        Log.v(TAG, this.getClass().getSimpleName() + "end, cost time:" + costTime);
    }

    public Operation enqueue() {
        Log.v(TAG, "Operation enqueue : " + this.getClass().getSimpleName());
        executor.execute(this);
        return this;
    }

    public void execute() {
        Log.v(TAG, this.getClass().getSimpleName() + " begin.");
        this.beginTime = System.currentTimeMillis();
        doWork();
        long costTime = System.currentTimeMillis() - this.beginTime;
        Log.v(TAG, this.getClass().getSimpleName() + "end, cost time:" + costTime);
    }

    public Operation callback(Callback<S, F> callback) {
        this.callback = callback;
        return this;
    }

    protected abstract void doWork();

    // call this in when do work and result is success
    // this will help you return result in work thread and main thread both
    protected void postSuccess(final S s) {
        callback.onSuccess(s);
        H.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccessMainThread(s);
            }
        });
    }

    // call this in when do work and result is failure
    // this will help you return result in work thread and main thread both
    protected void postFailure(final F f) {
        callback.onFailure(f);
        H.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailureMainThread(f);
            }
        });
    }

    protected void postProgress(final int progress) {
        callback.onProgress(progress);
        H.post(new Runnable() {
            @Override
            public void run() {
                callback.onProgress(progress);
            }
        });
    }


    public static class Callback<S, F> {
        public void onSuccess(S s){};
        public void onFailure(F f){};
        public void onProgress(int progress){};
        public void onSuccessMainThread(S s){};
        public void onFailureMainThread(F f){};
        public void onProgressMainThread(int progress){};
    }

    private static final Callback NullCallback = new Callback<>();


}
