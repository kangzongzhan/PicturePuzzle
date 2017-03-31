package com.khgame.sdk.picturepuzzle.serial;

import com.khgame.sdk.picturepuzzle.model.Serial;
import com.khgame.sdk.picturepuzzle.model.SerialPicture;

import java.util.List;

/**
 * Created by zkang on 2017/2/26.
 */

public interface SerialManager {


    /**
     * load all serial from db and network
     */
    public void loadSerials();


    /**
     * get all serials
     */
    public List<Serial> getSerials();

    /**
     * get serial by uuid
     */
    public Serial getSerialByUuid(String uuid);

    /**
     * when select a serial and going to start game
     */
    public void startSerial(Serial serial);

    /**
     * get the serial, maybe null if never select a serial to start
     */
    public Serial getCurrentSerial();

    /**
     * update current serial
     */

    public void updateCurrentSerial();

    /**
     * get current serial picture list
     */
    public List<SerialPicture> getCurrentSerialPictureList();

    /**
     * should be invoked when exit the serial
     */
    public void endSerial();

    /**
     * load all serial pictures by serial uuid from db
     */
    public void loadSerialPicturesBySerialUuid(String uuid);
}
