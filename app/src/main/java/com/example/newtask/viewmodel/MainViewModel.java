package com.example.newtask.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.newtask.SingleLiveData;
import com.example.newtask.model.Taxi;
import com.example.newtask.model.repository.DataApi;

import java.util.List;

public class MainViewModel extends ViewModel {
    private DataApi dataApi = DataApi.getInstance();
    private SingleLiveData<List<Taxi>>liveData;

    public LiveData<List<Taxi>> getCars()
    {
        liveData = dataApi.getCarsFromServer();
        return liveData;
    }
}
