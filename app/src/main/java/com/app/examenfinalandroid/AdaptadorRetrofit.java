package com.app.examenfinalandroid;

import retrofit2.Retrofit;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdaptadorRetrofit {
    Retrofit retrofit;
    public AdaptadorRetrofit()
    {

    }

    public Retrofit getAdapter()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.3.101/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}