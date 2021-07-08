package com.example.week2_step2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.week2_step2.provider.Car;

import java.util.ArrayList;
import java.util.List;

public class CarRecyclerAdapter extends RecyclerView.Adapter<CarRecyclerAdapter.ViewHolder> {

    List<Car> car_data = new ArrayList<>();

    public CarRecyclerAdapter(){
    }

    public void setData(List<Car> carList){
        this.car_data = carList;
    }

    @NonNull
    @Override
    public CarRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_cardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CarRecyclerAdapter.ViewHolder holder, int position) {
        holder.tv_car.setText(car_data.get(position).getMaker() +" | "+ car_data.get(position).getModel());
        holder.tv_caryear.setText(car_data.get(position).getYear()+"");
    }

    @Override
    public int getItemCount() {
        return car_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public View itemView;
        public TextView tv_car;
        public  TextView tv_caryear;

        public ViewHolder(View itemView){
            super(itemView);
            this.itemView = itemView;
            this.tv_car = itemView.findViewById(R.id.cv_tv_car);
            this.tv_caryear = itemView.findViewById(R.id.cv_tv_caryear);
        }
    }
}
