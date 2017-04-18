package com.khgame.sdk.picturepuzzle.service;

import com.khgame.sdk.picturepuzzle.service.model.SerialDto;
import com.khgame.sdk.picturepuzzle.service.model.SerialPictureDto;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Kisha Deng on 2/20/2017.
 */

public interface SerialService {

    @GET("serials")
    Call<List<SerialDto>> getAllSerials();

    @GET("serialsForReview")
    Call<List<SerialDto>> getSerialsForReview();

    @GET("serial/{serialUuid}")
    Call<List<SerialPictureDto>> getSerialPicturesByUuid(@Path(value = "serialUuid") String uuid);

    @GET("filter/{appStore}")
    Call<Map<String, Boolean>> getFilterByStore(@Path(value = "appStore") String appStore);

}
