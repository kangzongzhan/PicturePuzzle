package com.khgame.picturepuzzle.service;

import com.khgame.picturepuzzle.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kisha Deng on 2/20/2017.
 */

public class ApiProvider {

    private static Retrofit getDefaultRetrofit() {
        OkHttpClient httpClient = new OkHttpClient();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient = new OkHttpClient.Builder().addInterceptor(logging).build();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://kzz.oss-cn-shenzhen.aliyuncs.com/picturepuzzle/api/")
                .client(httpClient)
                .build();

        return retrofit;
    }

    public static SerialService getSerialService() {
        SerialService service = getDefaultRetrofit().create(SerialService.class);
        return service;
    }
}
