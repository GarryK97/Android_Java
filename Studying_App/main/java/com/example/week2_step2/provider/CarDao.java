package com.example.week2_step2.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CarDao {

    @Query("select * from cars")
    LiveData<List<Car>> getAllCars();

    @Insert
    void addCar(Car Car);

    @Query("delete FROM cars")
    void deleteAllCars();

    @Query("SELECT * FROM cars WHERE carMaker = :maker_input AND carYear = :year_input AND carPrice = :price_input")
    LiveData<List<Car>> getCarsByQuery(String maker_input, String year_input, String price_input);
}
