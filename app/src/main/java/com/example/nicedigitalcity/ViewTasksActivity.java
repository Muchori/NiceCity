package com.example.nicedigitalcity;


import android.os.Bundle;

public class ViewTasksActivity extends ViewBookingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_view_tasks);
        setContentLayout(R.layout.activity_view_tasks);
        setTitle("View Tasks");
    }
    @Override
    protected void onResume() {
        super.onResume();
        setSelected(R.id.navigation_tasks);
    }
}
