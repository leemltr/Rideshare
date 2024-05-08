package com.emmo.rideshare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    private Button googleMaps;

    private String decodeEmail(String encodedEmail) {
        String decodedEmail = encodedEmail.replace("-dot-", ".")
                .replace("-hash-", "#")
                .replace("-dollar-", "$")
                .replace("-leftBracket-", "[")
                .replace("-rightBracket-", "]");

        return decodedEmail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
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
        googleMaps = findViewById(R.id.detail_btngooglemaps);

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
                email.setText(ride.getEmail());
            }
        }
        loadUserData();

        googleMaps.setOnClickListener(v -> {
            String startStreetString = startStreet.getText().toString();
            String startNumberString = startNumber.getText().toString();
            String startCityString = startCity.getText().toString();
            String startZipString = startZip.getText().toString();
            String startAddress = startStreetString + " " + startNumberString + ", " + startCityString + ", " + startZipString;

            String endStreetString = endStreet.getText().toString();
            String endNumberString = endNumber.getText().toString();
            String endCityString = endCity.getText().toString();
            String endZipString = endZip.getText().toString();
            String endAddress = endStreetString + " " + endNumberString + ", " + endCityString + ", " + endZipString;

            Uri uri = Uri.parse("https://www.google.com/maps/dir/" + startAddress + "/" + endAddress);
            Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
            intent2.setPackage("com.google.android.apps.maps");
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent2);
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        MenuHelper.inflateMenu(menu, inflater);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return MenuHelper.handleMenuItemClick(item, this);
    }

    private void loadUserData(){
        DatabaseGlobal database = new DatabaseGlobal();
        String emailString = email.getText().toString();
        database.readUserFromDatabase(emailString).thenAccept(user -> {
            fname.setText(user.getFirstname());
            lname.setText(user.getLastname());
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });


    }
}