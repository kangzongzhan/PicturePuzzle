package com.khgame.picturepuzzle.data.source.remote;

import com.khgame.picturepuzzle.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteServiceProvider {

    private static volatile RemoteServiceProvider INSTANCE = null;

    private volatile Retrofit retrofit = null;
    private volatile SerialService serialService = null;
    private volatile ClassicService classicService = null;

    private static final String BASE_URL = "http://kzz.oss-cn-shenzhen.aliyuncs.com/picturepuzzle/api/";

    private RemoteServiceProvider(){}

    public static RemoteServiceProvider getInstance() {
        if (INSTANCE == null) {
            synchronized (RemoteServiceProvider.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RemoteServiceProvider();
                    INSTANCE.initRetrofit();
                }
            }
        }
        return INSTANCE;
    }

    public SerialService getSerialService() {
        if (serialService == null) {
            synchronized (RemoteServiceProvider.class) {
                if (serialService == null) {
                    serialService = retrofit.create(SerialService.class);
                }
            }
        }
        return serialService;
    }

    public ClassicService getClassicService() {
        if (classicService == null) {
            synchronized (RemoteServiceProvider.class) {
                if (classicService == null) {
                    classicService = retrofit.create(ClassicService.class);
                }
            }
        }
        return classicService;
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
