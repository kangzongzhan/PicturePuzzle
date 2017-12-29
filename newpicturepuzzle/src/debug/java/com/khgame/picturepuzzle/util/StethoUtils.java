package com.khgame.picturepuzzle.util;

import android.content.Context;

import com.facebook.stetho.Stetho;

public class StethoUtils {
    public static void initialize(Context context) {
        Stetho.initializeWithDefaults(context);
    }
}
