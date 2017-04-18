package com.khgame.sdk.picturepuzzle.service.operation;

import com.khgame.sdk.picturepuzzle.operation.Operation;
import com.khgame.sdk.picturepuzzle.service.ApiProvider;
import com.khgame.sdk.picturepuzzle.service.SerialService;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Kisha Deng on 2/20/2017.
 */

public class GetFilterByStoreName extends Operation<Map<String, Boolean>, Void> {

    private String storeName;
    public GetFilterByStoreName(String name) {
        this.storeName = name;
    }

    @Override
    protected void doWork() {
        SerialService serialService = ApiProvider.getSerialService();
        try {
            Map<String, Boolean> map = serialService.getFilterByStore(storeName).execute().body();
            postSuccess(map);
        } catch (IOException e) {
            postFailure(null);
        }
    }
}
