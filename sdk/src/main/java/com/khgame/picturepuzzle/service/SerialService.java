package com.khgame.picturepuzzle.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Kisha Deng on 2/20/2017.
 */

public interface SerialService {

    @GET("serials")
    Call<List<Serial>> getAllSerials();
}
