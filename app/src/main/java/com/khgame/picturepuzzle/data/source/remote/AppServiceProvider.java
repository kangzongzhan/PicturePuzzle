package com.khgame.picturepuzzle.data.source.remote;

import com.khgame.picturepuzzle.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppServiceProvider {

    private static volatile AppServiceProvider INSTANCE = null;

    private Retrofit retrofit = null;
    private SerialService serialService = null;

    private static final String BASE_URL = "http://kzz.oss-cn-shenzhen.aliyuncs.com/picturepuzzle/api/";

    private AppServiceProvider(){}

    public static AppServiceProvider getInstance() {
        if (INSTANCE == null) {
            synchronized (AppServiceProvider.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppServiceProvider();
                    INSTANCE.initRetrofit();
                }
            }
        }
        return INSTANCE;
    }

    public SerialService getSerialService() {
        if (serialService == null) {
            synchronized (AppServiceProvider.class) {
                if (serialService == null) {
                    serialService = retrofit.create(SerialService.class);
                }
            }
        }
        return serialService;
    }


    private void initRetrofit() {
        OkHttpClient httpClient = new OkHttpClient();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient = new OkHttpClient.Builder().addInterceptor(logging).build();
        }
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(httpClient)
                .build();
    }

}
