package com.emmo.rideshare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView searchStart, searchEnd;
    private int startValue, endValue;
    private FirebaseAuth mAuth;

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
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        searchStart = findViewById(R.id.searchStart);
        searchEnd = findViewById(R.id.searchEnd);

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

        searchStart.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Aktion ausführen, wenn die Suche durchgeführt wird
                startValue = checkContent(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Aktion ausführen, wenn sich der Suchtext ändert
                // Hier kannst du den newText verwenden, um zu reagieren, wenn der Benutzer etwas eingibt
                // Du kannst beispielsweise den Text für eine Filterung verwenden
                // Wenn du die RecyclerView aktualisieren möchtest, rufe hier die Methode zum Aktualisieren der Daten auf
                startValue = checkContent(newText);
                return false;
            }
        });

        searchEnd.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Aktion ausführen, wenn die Suche durchgeführt wird
                endValue = checkContent(query);
                if(endValue == startValue) {
                    searchRides();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Aktion ausführen, wenn sich der Suchtext ändert
                endValue = checkContent(newText);
                if(endValue == startValue) {
                    searchRides();
                }
                return false;
            }
        });

    }


    //Ist User angemeldet?
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

    private void searchRides(){
        if(startValue == 1 && endValue == 1) {
            //Nach Postleitzahlen suchen

        } else if(startValue == 2 && endValue == 2) {
            //Nach Städten suchen

        }
    }
}

