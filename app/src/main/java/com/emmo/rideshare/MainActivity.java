package com.emmo.rideshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterView adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Toolbar toolbar;
    private SearchView searchStart, searchEnd;
    private int startValue, endValue;
    private FirebaseAuth mAuth;
    private ArrayList<Ride> ridesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        searchStart = findViewById(R.id.searchStart);
        searchEnd = findViewById(R.id.searchEnd);

        ArrayList<Ride> data = prepareData();
        adapter = new AdapterView(data);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((OnItemClickListener) ride -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("ride", ride); // Ride-Objekt übergeben
            startActivity(intent);
        });

        searchStart.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String input) {
                startValue = checkContent(input);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                startValue = checkContent(newText);
                return false;
            }
        });

        searchEnd.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String input) {
                endValue = checkContent(input);
                if(endValue == startValue) {
                    getRidesList();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                endValue = checkContent(newText);
                if(endValue == startValue) {
                    getRidesList();
                }
                return false;
            }
        });
        startValue=0;
        endValue=0;
        getRidesList();

    }


    //Ist User angemeldet?
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
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


    private ArrayList<Ride> prepareData() {
        return ridesList;
    }


    private void getRidesList() {
        Date currentDate = new Date();
        // Datum formatieren im deutschen Format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
        String formattedDate = dateFormat.format(currentDate);

        // Uhrzeit formatieren
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = timeFormat.format(currentDate);
        DatabaseGlobal database = new DatabaseGlobal();
        if(startValue==0 && endValue==0) {
            database.findRideByDateTime(formattedDate, formattedTime, new DatabaseGlobal.OnRidesFoundListener() {
                @Override
                public void onSuccessRides(List<Ride> rides) {
                    updateRecyclerView(rides);
                }
            });
        } else if(startValue==1 && endValue ==1) {
            String startZip = searchStart.getQuery().toString();
            String endZip = searchEnd.getQuery().toString();
            database.findRideByDateTimeAndZips(formattedDate, formattedTime, startZip, endZip, new DatabaseGlobal.OnRidesFoundListener() {
                @Override
                public void onSuccessRides(List<Ride> rides) {
                    updateRecyclerView(rides);
                }
            });
        } else if(startValue==2 && endValue ==2) {
            String startCity = searchStart.getQuery().toString();
            String endCity = searchEnd.getQuery().toString();
            database.findRideByDateTimeAndCities(formattedDate, formattedTime, startCity, endCity, new DatabaseGlobal.OnRidesFoundListener() {
                @Override
                public void onSuccessRides(List<Ride> rides) {
                    updateRecyclerView(rides);
                }
            });
        }
    }

    private int checkContent(String text){
        String patternDigits = "^\\d{5}$"; //5 Zahlen
        String patternLetters = "^[a-zA-Z]+$"; //Nur Buchstaben --> Stadt

        Pattern regexDigits = Pattern.compile(patternDigits);
        Pattern regexLetters = Pattern.compile(patternLetters);

        Matcher matcherDigits = regexDigits.matcher(text);
        Matcher matcherLetters = regexLetters.matcher(text);

        // Überprüfe, ob das Muster mit dem Text übereinstimmt
        if (matcherDigits.matches()) {
            System.out.println("Der Text ist eine PLZ.");
            return 1;
        } else if (matcherLetters.matches()) {
            System.out.println("Der Text ist eine Stadt.");
            return 2;
        } else {
            System.out.println("Der Text erfüllt weder die Bedingung aus 5 Zahlen noch nur Buchstaben.");
            Toast.makeText(this, "Bitte nur eine Stadt oder eine PLZ eingeben", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    private void updateRecyclerView(List<Ride> newSearchResults) {
        ridesList.clear();
        ridesList.addAll(newSearchResults);
        adapter.notifyDataSetChanged();
    }
}

