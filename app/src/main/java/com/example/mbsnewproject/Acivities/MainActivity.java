package com.example.mbsnewproject.Acivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mbsnewproject.R;


public class MainActivity extends AppCompatActivity {
    private Button buttonPerformanceLogs;
    private Button buttonLetsGO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonPerformanceLogs = findViewById(R.id.buttonPerformanceLogs);
        buttonLetsGO = findViewById(R.id.buttonLetsGO);
        buttonPerformanceLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Klickevent für den buttonPerformanceLogs Button
                Intent intent = new Intent(MainActivity.this, PerformanceLogsActivity.class);
                startActivity(intent);
            }
        });

        buttonLetsGO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Klickevent für den buttonLetsGO Button
                Intent intent = new Intent(MainActivity.this, LetsGoActivity.class);
                startActivity(intent);
            }
        });


    }
}