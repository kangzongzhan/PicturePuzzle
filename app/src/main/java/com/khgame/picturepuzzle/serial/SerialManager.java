package com.khgame.picturepuzzle.serial;

import com.khgame.picturepuzzle.model.Serial;

/**
 * Created by zkang on 2017/2/26.
 */

public interface SerialManager {


    /**
     * load all serial from db and network
     */
    void loadSerials();

    /**
     *
     */
    void install(Serial serial);

    /**
     * get serial by serial uuid
     */
    void getSerialBySerialUuid(String serialUuid);

    /**
     * update serial filter
     */
    void updateSerialFilter(String appStore);

    /**
     * update serial
     */
    void updateSerial(Serial serial);

    /**
     * is this version passed app store review
     */
    boolean hasPassedReview();

    /**
     * set this version has passed the review
     */
    void setPassedReview();
}
