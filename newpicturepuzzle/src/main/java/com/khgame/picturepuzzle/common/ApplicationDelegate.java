package com.khgame.picturepuzzle.common;

import android.app.Application;
import android.util.Log;

import com.khgame.picturepuzzle.util.StethoUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ApplicationDelegate {
    private Application application;
    @Inject
    public ApplicationDelegate(Application application) {
        this.application = application;
    }
    public void onCreate() {
        StethoUtils.initialize(application);
    }
}
