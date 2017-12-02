package com.khgame.picturepuzzle.classic;

import android.content.Context;

import com.khgame.picturepuzzle.model.ClassicPicture;

/**
 * Created by Kisha Deng on 2/27/2017.
 */

public interface ClassicPictureManager {
    /**
     * when open this app first time
     * we should copy default picture to local
     * and insert them to db
     */
    void initialize(Context context);

    /**
     * get all classic pictures
     */
    void getAllClassicPictures();

    /**
     * get classic picture by uuid
     */
    void getClassicPictureByUuid(String uuid);

    /**
     * update class picture
     */
    void updateClassicPicture(ClassicPicture classicPicture);
}
