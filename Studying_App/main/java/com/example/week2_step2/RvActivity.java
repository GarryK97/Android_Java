package com.example.week2_step2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.week2_step2.provider.CarViewModel;

import java.util.ArrayList;

public class RvActivity extends AppCompatActivity {

    RecyclerView rv_car;
    RecyclerView.LayoutManager rv_car_layout;

    CarRecyclerAdapter mCarRecyclerAdapter;
    CarViewModel mCarViewModel;

//    ArrayList<String> cars_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_recyclerview);

//        Intent get_rv_intent = getIntent();
//        cars_list = get_rv_intent.getStringArrayListExtra("cars_list");

        rv_car = findViewById(R.id.car_RecyclerView);

        rv_car_layout = new LinearLayoutManager(this);
        rv_car.setLayoutManager(rv_car_layout);

        mCarRecyclerAdapter = new CarRecyclerAdapter();
        rv_car.setAdapter(mCarRecyclerAdapter);

        if (getIntent() == null || getIntent().getExtras() == null) {
            mCarViewModel = new ViewModelProvider(this).get(CarViewModel.class);
            mCarViewModel.getAllCars().observe(this, newData -> {
                mCarRecyclerAdapter.setData(newData);
                mCarRecyclerAdapter.notifyDataSetChanged();
            });
        }
        else {
            ArrayList<String> query_values = getIntent().getStringArrayListExtra("query_values");
            mCarViewModel = new ViewModelProvider(this).get(CarViewModel.class);
            mCarViewModel.getCarsByMaker(query_values.get(0), query_values.get(1), query_values.get(2)).observe(this, newData ->{
                mCarRecyclerAdapter.setData(newData);
                mCarRecyclerAdapter.notifyDataSetChanged();
            });
        }
    }
}
