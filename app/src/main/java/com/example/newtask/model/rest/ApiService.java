package com.example.newtask.model.rest;

import com.example.newtask.model.TaxiList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

@GET(".")
    Call<TaxiList> getTaxies(
            @Query("p1Lat")String p1Lat,
            @Query("p1Lon")String p1Lon,
            @Query("p2Lat")String p2Lat,
            @Query("p2Lon")String p2Lon
            );
}
