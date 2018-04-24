package com.simple.heinika.javagankio.http;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private Retrofit.Builder mBuilder;
    private final String gankIOBaseUrl = "http://gank.io/api/";
    private Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .serializeNulls()
            .create();
    private GankIOService gankIOService;
    private static ServiceGenerator serviceGenerator;

    private ServiceGenerator(){}

    public static ServiceGenerator getInstance(){
        if(serviceGenerator == null){
            serviceGenerator = new ServiceGenerator();
        }
        return serviceGenerator;
    }

    private void initRetrofitBuilder(){
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        okHttpBuilder.connectTimeout(5, TimeUnit.SECONDS);

        mBuilder = new Retrofit.Builder()
                .client(okHttpBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(gankIOBaseUrl);
    }


    public GankIOService getGankIOService(){
        if(gankIOService ==null){
            initRetrofitBuilder();
            gankIOService = mBuilder.build().create(GankIOService.class);
        }
        return gankIOService;
    }
}
