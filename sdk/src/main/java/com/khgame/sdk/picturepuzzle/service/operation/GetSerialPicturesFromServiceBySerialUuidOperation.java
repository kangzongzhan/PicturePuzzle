package com.khgame.sdk.picturepuzzle.service.operation;

import com.khgame.sdk.picturepuzzle.operation.Operation;
import com.khgame.sdk.picturepuzzle.service.ApiProvider;
import com.khgame.sdk.picturepuzzle.service.SerialService;
import com.khgame.sdk.picturepuzzle.service.model.SerialPictureDto;

import java.io.IOException;
import java.util.List;

/**
 * get all serial pictures from service by serial uuid
 * Created by zkang on 2017/2/25.
 */

public class GetSerialPicturesFromServiceBySerialUuidOperation extends Operation<List<SerialPictureDto>, Void> {

    private String uuid;

    public GetSerialPicturesFromServiceBySerialUuidOperation(String uuid) {
        this.uuid = uuid;
    }

    @Override
    protected void doWork() {
        SerialService serialService = ApiProvider.getSerialService();
        try {
            List<SerialPictureDto> list = serialService.getSerialPicturesByUuid(uuid).execute().body();
            postSuccess(list);
        } catch (IOException e) {
            postFailure(null);
        }
    }
}
