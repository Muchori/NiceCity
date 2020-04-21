package com.example.nicedigitalcity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class NewBookingsActivity extends ViewBookingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_new_bookings);
        setContentLayout(R.layout.activity_new_bookings);
        setTitle("New Bookings");
    }
    @Override
    protected void onResume() {
        super.onResume();
        setSelected(R.id.navigation_new_bookings);
    }
}
