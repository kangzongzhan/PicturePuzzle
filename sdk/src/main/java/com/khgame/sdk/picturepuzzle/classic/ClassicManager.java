package com.khgame.sdk.picturepuzzle.classic;

/**
 * Created by Kisha Deng on 2/27/2017.
 */

public interface ClassicManager {
    /**
     * when open this app first time
     * we should copy default picture to local
     * and insert them to db
     */
    public void initialize();
}
