package com.example.mbsnewproject.Acivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
//import android.widget.Toolbar;

import com.example.mbsnewproject.R;
import com.example.mbsnewproject.View.RouteAdapter;
import com.example.mbsnewproject.DB.RouteDBHelper;
import com.example.mbsnewproject.Tracking.RouteData;
import com.example.mbsnewproject.DB.SharedPreferenceHelper;
import com.google.gson.Gson;

public class PerformanceLogsActivity extends AppCompatActivity {

    private RouteDBHelper routeDBHelper;
    private RecyclerView routeRecyclerView;
    private RouteAdapter routeAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_logs);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Performance Log");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);

        routeDBHelper = new SharedPreferenceHelper(this);
        routeRecyclerView = findViewById(R.id.routeRecyclerView);
        routeAdapter = new RouteAdapter(routeDBHelper.loadAll());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        routeRecyclerView.setLayoutManager(layoutManager);
        routeRecyclerView.setAdapter(routeAdapter);

        routeAdapter.setOnDeleteClickListener(position -> {
            RouteData selectedRoute = routeAdapter.getItem(position);
            routeDBHelper.remove(selectedRoute.getRouteTitle() + ": " + selectedRoute.getDateTime());
            routeAdapter.removeItem(position);
        });

        routeAdapter.setOnItemClickListener(position -> {
            RouteData selectedRoute = routeAdapter.getItem(position);
            Gson gson = new Gson();
            String myRouteAsJson = gson.toJson(selectedRoute);


            Intent intent = new Intent(PerformanceLogsActivity.this, TitleDetailsActivity.class);
            intent.putExtra("selectedRoute", myRouteAsJson);
            startActivity(intent);
        });
    }

}