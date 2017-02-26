package com.khgame.picturepuzzle.service.operation;

import com.khgame.picturepuzzle.operation.Operation;
import com.khgame.picturepuzzle.service.ApiProvider;
import com.khgame.picturepuzzle.service.SerialService;
import com.khgame.picturepuzzle.service.model.SerialDto;

import java.io.IOException;
import java.util.List;

/**
 * Created by Kisha Deng on 2/20/2017.
 */

public class GetAllSerialsOperation extends Operation<List<SerialDto>, Void>{
    @Override
    protected void doWork() {
        SerialService serialService = ApiProvider.getSerialService();
        try {
            List<SerialDto> list = serialService.getAllSerials().execute().body();
            postSuccess(list);
        }catch (IOException e) {
            postFailure(null);
        }
    }
}
