package com.khgame.sdk.picturepuzzle.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.khgame.sdk.picturepuzzle.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kisha Deng on 2/20/2017.
 */

public class ApiProvider {

    public static SerialService getSerialService() {
        OkHttpClient httpClient;
        if (true) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient = new OkHttpClient.Builder().addInterceptor(logging).build();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://kzz.oss-cn-shenzhen.aliyuncs.com/picturepuzzle/api/")
                .client(httpClient)
                .build();

        SerialService service = retrofit.create(SerialService.class);
        return service;
    }
}
