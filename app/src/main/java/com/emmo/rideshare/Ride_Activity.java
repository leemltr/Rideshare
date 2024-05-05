package com.emmo.rideshare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Ride_Activity extends AppCompatActivity {
    EditText startCity, startZip, startStreet, startNumber, endCity, endZip, endStreet, endNumber, date, time;
    Button newRide, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ride);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ride), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        startCity = findViewById(R.id.start);
        startZip = findViewById(R.id.startPlz);
        startStreet = findViewById(R.id.startStreet);
        startNumber = findViewById(R.id.startNumber);
        endCity = findViewById(R.id.end);
        endZip = findViewById(R.id.plzZiel);
        endStreet = findViewById(R.id.zielStreet);
        endNumber = findViewById(R.id.zielNumber);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);

        newRide = findViewById(R.id.btn_newRide);
        cancel = findViewById(R.id.btnCancel);

        newRide.setOnClickListener(view -> {
            startActivity(new Intent(Ride_Activity.this, MainActivity.class));
            finish();
        });

        cancel.setOnClickListener(v -> {
            startActivity(new Intent(Ride_Activity.this, MainActivity.class));
            finish();
        });
    }
}