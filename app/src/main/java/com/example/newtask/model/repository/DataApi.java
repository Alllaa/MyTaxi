package com.example.newtask.model.repository;

import android.util.Log;

import com.example.newtask.SingleLiveData;
import com.example.newtask.model.Taxi;
import com.example.newtask.model.TaxiList;
import com.example.newtask.model.rest.ApiService;
import com.example.newtask.model.rest.GetTaxi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class DataApi {
    private static DataApi dataApi;
    private List<Taxi>data;
    SingleLiveData<List<Taxi>> singleLiveData =new SingleLiveData<>();
    public static DataApi getInstance()
    {
        if (dataApi == null) {
            dataApi = new DataApi();
        }
        return dataApi;
    }

    public SingleLiveData<List<Taxi>>getCarsFromServer()
    {
        ApiService apiService;
        apiService = GetTaxi.getCars().create(ApiService.class);

        Call<TaxiList> call = apiService.getTaxies("30.129610","30.864947","29.824722","31.519720");
        call.enqueue(new Callback<TaxiList>() {
            @Override
            public void onResponse(Call<TaxiList> call, Response<TaxiList> response) {
                if (!response.isSuccessful()) {
                    //  Log.d("Number","num of page is = "+ num);
                    Log.d("DataApi:", "Error in response");
                    return;
                }
                data = response.body().getResults();
                singleLiveData.setValue(data);
                Log.d("DataApi", "Total number of Movies data : " + response.body().getResults().size());

            }

            @Override
            public void onFailure(Call<TaxiList> call, Throwable t) {
                Log.d("DataApi:", "Failure in response");

            }
        });

        return singleLiveData;
    }
}
