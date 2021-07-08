package com.example.week2_step2.provider;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class CarRepository {

    private CarDao mCarDao;
    private LiveData<List<Car>> mAllCars;

    CarRepository(Application application) {
        CarDatabase db = CarDatabase.getDatabase(application);
        mCarDao = db.CarDao();
        mAllCars = mCarDao.getAllCars();
    }
    LiveData<List<Car>> getAllCars() {
        return mAllCars;
    }
    void insert(Car car) {
        CarDatabase.databaseWriteExecutor.execute(() -> mCarDao.addCar(car));
    }

    void deleteAll(){
        CarDatabase.databaseWriteExecutor.execute(()->{
            mCarDao.deleteAllCars();
        });
    }

    LiveData<List<Car>> getCarsByMaker(String maker_input, String year_input, String price_input){
        return mCarDao.getCarsByQuery(maker_input, year_input, price_input);
    }

}
