package com.khgame.picturepuzzle.serial;

import com.khgame.picturepuzzle.model.SerialPicture;

/**
 * Created by zkang on 2017/4/8.
 */

public interface SerialPictureManager {
    void loadSerialPicturesBySerialUuid(String serialUuid);
    void getSerialPictureByUuid(String serialPictureUuid);
    void updateSerialPicture(SerialPicture serialPicture);
}
