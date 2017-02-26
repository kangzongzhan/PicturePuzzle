package com.khgame.picturepuzzle.serial;

import com.khgame.picturepuzzle.model.Serial;

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
     * download all serial picture and save as local
     */
    public void install(Serial serial);

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
     * should be invoked when exit the serial
     */
    public void endSerial();

    /**
     * load all serial pictures by serial uuid from db
     */
    public void loadSerialPicturesBySerialUuid(String uuid);
}
