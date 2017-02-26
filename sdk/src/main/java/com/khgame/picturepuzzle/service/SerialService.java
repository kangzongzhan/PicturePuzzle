package com.khgame.picturepuzzle.service;

import com.khgame.picturepuzzle.service.model.SerialDto;
import com.khgame.picturepuzzle.service.model.SerialPictureDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Kisha Deng on 2/20/2017.
 */

public interface SerialService {

    @GET("serials")
    Call<List<SerialDto>> getAllSerials();

    @GET("serial/{serialUuid}")
    Call<List<SerialPictureDto>> getSerialPicturesByUuid(@Path(value = "serialUuid") String uuid);
}