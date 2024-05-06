package com.emmo.rideshare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView search;

    private FirebaseAuth mAuth;

    public void initializeUser(){
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        search = findViewById(R.id.search);

        ArrayList<Ride> data = prepareData();
        adapter = new AdapterView(data);
        adapter = new AdapterView(data, new OnItemClickListener() {
            @Override
            public void onItemClick(Ride ride) {
                // Hier wird Ihre Methode aufgerufen, wenn auf ein Element geklickt wird
                // z.B. Methode zur Anzeige von Details der ausgewählten Fahrt
            }
        });
        recyclerView.setAdapter(adapter);
    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private ArrayList<Ride> prepareData() {
        // Hier bereiten Sie die Daten vor, die Sie in der RecyclerView anzeigen möchten
        // Zum Beispiel können Sie eine ArrayList<MyDataModel> zurückgeben
        // mit Instanzen Ihrer Datenklasse MyDataModel
        return null;
    }
}

