package com.khgame.sdk.picturepuzzle.service.operation;

import com.khgame.sdk.picturepuzzle.operation.Operation;
import com.khgame.sdk.picturepuzzle.service.ApiProvider;
import com.khgame.sdk.picturepuzzle.service.SerialService;
import com.khgame.sdk.picturepuzzle.service.model.SerialDto;

import java.io.IOException;
import java.util.List;

/**
 * Created by Kisha Deng on 2/20/2017.
 */

public class GetReviewSerialsFromServiceOperation extends Operation<List<SerialDto>, Void>{
    @Override
    protected void doWork() {
        SerialService serialService = ApiProvider.getSerialService();
        try {
            List<SerialDto> list = serialService.getSerialsForReview().execute().body();
            postSuccess(list);
        }catch (IOException e) {
            postFailure(null);
        }
    }
}
