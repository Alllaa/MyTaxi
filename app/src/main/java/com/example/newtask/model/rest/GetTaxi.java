package com.example.newtask.model.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetTaxi {
    public static final String BASE_URL = "https://fake-poi-api.mytaxi.com/";

    public static Retrofit retrofit = null;

    public static Retrofit getCars()
    {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}