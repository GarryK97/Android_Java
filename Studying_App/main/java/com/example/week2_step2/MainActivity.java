package com.example.week2_step2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.icu.number.Scale;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.week2_step2.provider.Car;
import com.example.week2_step2.provider.CarViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;

//    public static final String KEY_MAKER  = "maker";
//    public static final String KEY_MODEL = "model";
//    public static final String KEY_YEAR = "year";
//    public static final String KEY_COLOR = "color";
//    public static final String KEY_SEATS = "seats";
//    public static final String KEY_PRICE = "price";
//
//    public static final String FILENAME = "AutoShowRoomData";

    EditText ip_maker;
    EditText ip_model;
    EditText ip_year;
    EditText ip_color;
    EditText ip_seats;
    EditText ip_price;

//    SharedPreferences myData;
//    ArrayList<String> cars_List = new ArrayList<>();
//    ArrayAdapter carsAdapter;

    CarViewModel mCarViewModel;
    CarRecyclerAdapter mCarRecyclerAdapter;

    DatabaseReference carRef;

    // Creating and setting the action of sms_Broadcast_Receiver in MainActivity
    public class sms_Broadcast_Receiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(SMSReceiver.SMS_KEY);

            StringTokenizer token = new StringTokenizer(msg, ";");
            String maker = token.nextToken();
            String model = token.nextToken();
            String year = token.nextToken();
            String color = token.nextToken();
            String seats = token.nextToken();
            String price = token.nextToken();

            ip_maker.setText(maker);
            ip_model.setText(model);
            ip_year.setText(year);
            ip_color.setText(color);
            ip_seats.setText(seats);
            ip_price.setText(price);
        }
    };

    public class drawer_Menu_Listener implements NavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.drawer_AddCar){
                addNewCar(null);}
            else if (id == R.id.drawer_RemoveCar) {
//                cars_List.remove(cars_List.size()-1);
//                carsAdapter.notifyDataSetChanged();
            }
            else if (id == R.id.drawer_RemoveAll) {
                clear_all(null);
            }
            else if (id == R.id.drawer_ListAll){
                // List All items with Recycler Views
                Intent rv_intent = new Intent(MainActivity.this, RvActivity.class);
//                rv_intent.putExtra("cars_list", cars_List);
                MainActivity.this.startActivity(rv_intent);
            }
            else if (id == R.id.drawer_close_btn){
                finish();
            }

            drawerLayout.closeDrawers();
            return true;
        }
    }

    private class CarGestureDetector extends GestureDetector.SimpleOnGestureListener{

        @Override
        public void onLongPress(MotionEvent e) {
            clear_all(null);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (ip_price.getText().toString().equals("")){
                ip_price.setText("0");
            }

            int new_value = Integer.parseInt(ip_price.getText().toString()) - (int) distanceX;
            if (new_value >= 0)
                ip_price.setText(new_value + "");

            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(velocityX) > 1000)
                moveTaskToBack(true);
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            ip_maker.setText("BMW");
            ip_model.setText("X7");
            ip_year.setText("2010");
            ip_color.setText("Red");
            ip_seats.setText("7");
            ip_price.setText("1500");

            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (ip_seats.getText().toString().equals("")){
                ip_seats.setText("1");
            }
            else {
                int new_value = Integer.parseInt(ip_seats.getText().toString()) + 1;
                ip_seats.setText(new_value + "");
            }
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer);

        ip_maker = findViewById(R.id.inputMaker);
        ip_model = findViewById(R.id.inputModel);
        ip_year = findViewById(R.id.inputYear);
        ip_color = findViewById(R.id.inputColor);
        ip_seats = findViewById(R.id.inputSeats);
        ip_price = findViewById(R.id.inputPrice);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);

        sms_Broadcast_Receiver sms_Receiver = new sms_Broadcast_Receiver();

        IntentFilter sms_Filter = new IntentFilter(SMSReceiver.SMS_FILTER); // Accept only SMS_FILTER
        registerReceiver(sms_Receiver, sms_Filter); // Register receiver that accepts SMS_FILTER

        // ListView
//        ListView cars_ListView = findViewById(R.id.cars_ListView);
//        carsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, cars_List);
//        cars_ListView.setAdapter(carsAdapter);

        // RecyclerView using Database
        mCarRecyclerAdapter = new CarRecyclerAdapter();

        mCarViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        mCarViewModel.getAllCars().observe(this, newData -> {
            mCarRecyclerAdapter.setData(newData);
            mCarRecyclerAdapter.notifyDataSetChanged();
        });

        // Adding toolbar code and add actionbar supports
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        // Adding button in Actionbar(Toolbar) for drawer layout (Navigation view)
        drawerLayout = findViewById(R.id.drawer_Layout);
        ActionBarDrawerToggle d_toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(d_toggle);
        d_toggle.syncState();

        // Setting the listener for navigation view
        NavigationView drawer_menu = findViewById(R.id.nv_drawer);
        drawer_menu.setNavigationItemSelectedListener(new drawer_Menu_Listener());

        // Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        carRef = database.getReference("Car");

        // Gesture Detector
        GestureDetector detector = new GestureDetector(this, new CarGestureDetector());
        View main_layout = findViewById(R.id.main_layout);
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.option_menu, menu);
//        return super.onCreateOptionsMenu(menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.option_clear) {
            clear_all(null);
        }
        return true;
    }

    @Override
    protected void onStart(){
        super.onStart();

//        myData = getSharedPreferences(FILENAME, 0);
//
//        ip_maker.setText(myData.getString(KEY_MAKER, ""));
//        ip_model.setText(myData.getString(KEY_MODEL, ""));
//        ip_year.setText(myData.getString(KEY_YEAR, ""));
//        ip_color.setText(myData.getString(KEY_COLOR, ""));
//        ip_seats.setText(myData.getString(KEY_SEATS, ""));
//        ip_price.setText(myData.getString(KEY_PRICE, ""));


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

//        String maker = ip_maker.getText().toString();
//        String model = ip_model.getText().toString();
//        String year = ip_year.getText().toString();
//        String color = ip_color.getText().toString();
//        String seats = ip_seats.getText().toString();
//        String price = ip_price.getText().toString();
//
//        outState.putString(KEY_MAKER, maker);
//        outState.putString(KEY_MODEL, model);
//        outState.putString(KEY_YEAR, year);
//        outState.putString(KEY_COLOR, color);
//        outState.putString(KEY_SEATS, seats);
//        outState.putString(KEY_PRICE, price);


    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

//        String maker = savedInstanceState.getString(KEY_MAKER);
//        String model = savedInstanceState.getString(KEY_MODEL);
//        String year = savedInstanceState.getString(KEY_YEAR);
//        String color = savedInstanceState.getString(KEY_COLOR);
//        String seats = savedInstanceState.getString(KEY_SEATS);
//        String price = savedInstanceState.getString(KEY_PRICE);
//
//        ip_maker.setText(maker);
//        ip_model.setText(model);
//        ip_year.setText(year);
//        ip_color.setText(color);
//        ip_seats.setText(seats);
//        ip_price.setText(price);


    }

    public void addNewCar(View view){
        String maker = ip_maker.getText().toString();
        String model = ip_model.getText().toString();
        int year = Integer.parseInt(ip_year.getText().toString());
        String color = ip_color.getText().toString();
        int seats = Integer.parseInt(ip_seats.getText().toString());
        int price = Integer.parseInt(ip_price.getText().toString());

//        myData = getSharedPreferences(FILENAME, 0);
//        SharedPreferences.Editor myDataEditor = myData.edit();
//        myDataEditor.putString(KEY_MAKER, maker);
//        myDataEditor.putString(KEY_MODEL, model);
//        myDataEditor.putString(KEY_YEAR, year);
//        myDataEditor.putString(KEY_COLOR, color);
//        myDataEditor.putString(KEY_SEATS, seats);
//        myDataEditor.putString(KEY_PRICE, price);
//
//        myDataEditor.apply();
//
//        cars_List.add(maker + " | " + model);
//        carsAdapter.notifyDataSetChanged();

        Car car = new Car(maker, model, year, color, seats, price);
        mCarViewModel.insert(car);
        carRef.push().setValue(car);

        // screen pop up
        String end_text = "We added a new car ("+ maker +")";
        Toast.makeText(this, end_text, Toast.LENGTH_SHORT).show();
    }

    public void clear_all(View view){
        ip_maker.setText("");
        ip_model.setText("");
        ip_year.setText("");
        ip_color.setText("");
        ip_seats.setText("");
        ip_price.setText("");

//        SharedPreferences.Editor myEditor = myData.edit();
//        myEditor.clear().apply();

        mCarViewModel.deleteAll();
        carRef.removeValue();

        Toast.makeText(this, "Cleared All", Toast.LENGTH_SHORT).show();
    }

    public void query_car(View view){
        TextView query_maker_tv = findViewById(R.id.query_maker);
        TextView query_year_tv = findViewById(R.id.query_year);
        TextView query_price_tv = findViewById(R.id.query_price);

        String query_maker = query_maker_tv.getText().toString();
        String query_year = query_year_tv.getText().toString();
        String query_price = query_price_tv.getText().toString();

        ArrayList<String> query_value = new ArrayList<>();
        query_value.add(query_maker);
        query_value.add(query_year);
        query_value.add(query_price);

        mCarViewModel.getCarsByMaker(query_maker, query_year, query_price).observe(this, newData ->{
            mCarRecyclerAdapter.setData(newData);
            mCarRecyclerAdapter.notifyDataSetChanged();
        });

        // List All items with Recycler Views
        Intent rv_intent = new Intent(MainActivity.this, RvActivity.class);
        rv_intent.putExtra("query_values", query_value);
        MainActivity.this.startActivity(rv_intent);
    }

}

