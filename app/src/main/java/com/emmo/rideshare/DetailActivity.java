package com.emmo.rideshare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView startStreet, startNumber, startCity, startZip, endStreet, endNumber, endCity, endZip, notes, date, time, fname, lname, email;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        startStreet = findViewById(R.id.detail_street_von);
        startNumber = findViewById(R.id.detail_hnr_von);
        startCity = findViewById(R.id.detail_city_von);
        startZip = findViewById(R.id.detail_zip_von);
        endStreet = findViewById(R.id.detail_street_nach);
        endNumber = findViewById(R.id.detail_hnr_nach);
        endCity = findViewById(R.id.detail_city_nach);
        endZip = findViewById(R.id.detail_zip_nach);
        date = findViewById(R.id.detail_date);
        time = findViewById(R.id.detail_time);
        notes = findViewById(R.id.detail_notes);
        fname = findViewById(R.id.detail_vname);
        lname = findViewById(R.id.detail_nname);
        email = findViewById(R.id.detail_email);

        Intent intent = getIntent();
        if (intent != null) {
            Ride ride = intent.getParcelableExtra("ride");
            if (ride != null) {
                startStreet.setText(ride.getStartStreet());
                startNumber.setText(ride.getStartNumber());
                startCity.setText(ride.getStartCity());
                startZip.setText(ride.getStartZip());
                endStreet.setText(ride.getEndStreet());
                endNumber.setText(ride.getEndNumber());
                endCity.setText(ride.getEndCity());
                endZip.setText(ride.getEndZip());
                notes.setText(ride.getNotes());
                String[] dateTimeParts = ride.getDate_time().split("_");
                String dateValue = dateTimeParts[0];
                String timeValue = dateTimeParts[1];
                date.setText(dateValue);
                time.setText(timeValue);
            }
        }
        loadUserData();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(DetailActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void loadUserData(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseGlobal database = new DatabaseGlobal();
        if(currentUser != null) {
            String emailString = currentUser.getEmail();
            email.setText(emailString);
            database.readUserFromDatabase(emailString).thenAccept(user -> {
                fname.setText(user.getFirstname());
                lname.setText(user.getLastname());
            }).exceptionally(ex -> {
                ex.printStackTrace();
                return null;
            });

        }
    }
}