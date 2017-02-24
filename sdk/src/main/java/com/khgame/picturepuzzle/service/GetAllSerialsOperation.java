package com.khgame.picturepuzzle.service;

import com.khgame.picturepuzzle.common.Operation;
import com.khgame.picturepuzzle.service.model.Serial;

import java.io.IOException;
import java.util.List;

/**
 * Created by Kisha Deng on 2/20/2017.
 */

public class GetAllSerialsOperation extends Operation<List<Serial>, Void>{
    @Override
    protected void doWork() {
        SerialService serialService = ApiProvider.getSerialService();
        try {
            List<Serial> list = serialService.getAllSerials().execute().body();
            postSuccess(list);
        }catch (IOException e) {
            postFailure(null);
        }
    }
}
