package com.silverorange.videoplayer.RetrofitApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    // Init Retrofit for api calling

    private static Retrofit retrofit;

    private static final String BASE_URL = "http://10.0.0.3:4000/";

    public static Retrofit ApiClient() {

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.connectTimeout(5, TimeUnit.MINUTES);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient httpClient = builder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build();

        return retrofit;

    }

}