package com.emmo.rideshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

import java.util.ArrayList;
import java.util.List;

public class myRidesActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private AdapterView adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseAuth mAuth;
    private ArrayList<Ride> ridesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_rides);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.myRide), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        getRidesList();

        ArrayList<Ride> data = prepareData();
        adapter = new AdapterView(data);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((OnItemClickListener) ride -> {
            Intent intent = new Intent(myRidesActivity.this, DetailActivity.class);
            intent.putExtra("ride", ride); // Ride-Objekt übergeben
            startActivity(intent);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(myRidesActivity.this, LoginActivity.class);
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

    private void getRidesList(){
        DatabaseGlobal database = new DatabaseGlobal();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String emailString = currentUser.getEmail();
        database.findRidesByEmail(emailString, new DatabaseGlobal.OnRidesFoundListener() {
            @Override
            public void onSuccessRides(List<Ride> rides) {
                updateRecyclerView(rides);
            }
        });
    }

    private void updateRecyclerView(List<Ride> newSearchResults) {
        ridesList.clear();
        ridesList.addAll(newSearchResults);
        adapter.notifyDataSetChanged();
    }
}