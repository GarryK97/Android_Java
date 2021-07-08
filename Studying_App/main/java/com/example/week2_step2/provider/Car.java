package com.example.week2_step2.provider;

import android.content.ContentValues;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static com.example.week2_step2.provider.Car.TABLE_NAME;


@Entity(tableName = TABLE_NAME)
public class Car {
    public static final String TABLE_NAME = "cars";

    public static final String COLUMN_ID = BaseColumns._ID;
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = COLUMN_ID)
    private int id;

    public static final String COLUMN_MAKER = "carMaker";
    @ColumnInfo(name = COLUMN_MAKER)
    private String maker;

    public static final String COLUMN_MODEL = "carModel";
    @ColumnInfo(name = COLUMN_MODEL)
    private String model;

    public static final String COLUMN_YEAR = "carYear";
    @ColumnInfo(name = COLUMN_YEAR)
    private int year;

    public static final String COLUMN_COLOR = "carColor";
    @ColumnInfo(name = COLUMN_COLOR)
    private String color;

    public static final String COLUMN_SEATS = "carSeats";
    @ColumnInfo(name = COLUMN_SEATS)
    private int seats;

    public static final String COLUMN_PRICE = "carPrice";
    @ColumnInfo(name = COLUMN_PRICE)
    private int price;

    public Car(String maker, String model, int year, String color, int seats, int price) {
        this.maker = maker;
        this.model = model;
        this.year = year;
        this.color = color;
        this.seats = seats;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
