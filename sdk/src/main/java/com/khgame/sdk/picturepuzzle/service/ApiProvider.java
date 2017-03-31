package com.khgame.sdk.picturepuzzle.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kisha Deng on 2/20/2017.
 */

public class ApiProvider {

    public static SerialService getSerialService() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://kzz.oss-cn-shenzhen.aliyuncs.com/picturepuzzle/api/")
                .build();

        SerialService service = retrofit.create(SerialService.class);
        return service;
    }
}
