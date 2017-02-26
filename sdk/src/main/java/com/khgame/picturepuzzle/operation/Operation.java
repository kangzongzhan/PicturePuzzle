package com.khgame.picturepuzzle.operation;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

/**
 * Created by zkang on 2017/2/17.
 */

/**
 * @param <S> the result type when success
 * @param <F> the result type when failure
 */
public abstract class Operation<S, F> implements Runnable {

    private Executor executor = AsyncTask.THREAD_POOL_EXECUTOR;
    private Handler H = new Handler(Looper.getMainLooper());
    protected Callback callback = NullCallback;

    @Override
    public void run() {
        doWork();
    }

    public void enqueue() {
        executor.execute(this);
    }

    public void execute() {
         doWork();
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

    private static Callback NullCallback = new Callback<>();


}
