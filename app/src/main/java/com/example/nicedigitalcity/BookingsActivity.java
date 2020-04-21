package com.example.nicedigitalcity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class BookingsActivity extends ViewBookingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_bookings);
        setContentLayout(R.layout.activity_bookings);
        setTitle("View Bookings");
    }
    @Override
    protected void onResume() {
        super.onResume();
        setSelected(R.id.navigation_bookings);
    }
}
