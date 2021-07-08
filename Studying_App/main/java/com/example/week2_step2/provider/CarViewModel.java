package com.example.week2_step2.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CarViewModel extends AndroidViewModel {
    private CarRepository mRepository;
    private LiveData<List<Car>> mAllCars;

    public CarViewModel(@NonNull Application application) {
        super(application);
        mRepository = new CarRepository(application);
        mAllCars = mRepository.getAllCars();
    }

    public LiveData<List<Car>> getAllCars() {
        return mAllCars;
    }

    public void insert(Car car) {
        mRepository.insert(car);
    }

    public void deleteAll(){
        mRepository.deleteAll();
    }

    public LiveData<List<Car>> getCarsByMaker(String maker_input, String year_input, String price_input){
        return mRepository.getCarsByMaker(maker_input, year_input, price_input);
    }

}
