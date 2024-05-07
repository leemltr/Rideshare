package com.emmo.rideshare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RideActivity extends AppCompatActivity {
    EditText startName, startCity, startZip, startStreet, startNumber, endName, endCity, endZip, endStreet, endNumber, notes, date, time;
    Button newRide, cancel;
    private FirebaseAuth mAuth;

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
        mAuth = FirebaseAuth.getInstance();

        startName = findViewById(R.id.startName);
        startCity = findViewById(R.id.start);
        startZip = findViewById(R.id.startPlz);
        startStreet = findViewById(R.id.startStreet);
        startNumber = findViewById(R.id.startNumber);
        endName = findViewById(R.id.zielName);
        endCity = findViewById(R.id.end);
        endZip = findViewById(R.id.plzZiel);
        endStreet = findViewById(R.id.zielStreet);
        endNumber = findViewById(R.id.zielNumber);
        notes = findViewById(R.id.notes);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);

        newRide = findViewById(R.id.btn_newRide);
        cancel = findViewById(R.id.btnCancel);

        newRide.setOnClickListener(view -> {
            if (validateFields()) {
                saveRide();
            } else {
                // Es gibt ungültige Eingaben, zeigen Sie eine Fehlermeldung an
                Toast.makeText(RideActivity.this, "Bitte überprüfen Sie Ihre Eingaben", Toast.LENGTH_SHORT).show();
            }

        });

        cancel.setOnClickListener(v -> {
            startActivity(new Intent(RideActivity.this, MainActivity.class));
            finish();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(RideActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean isValidZip(String text) {
        return !text.isEmpty() && text.matches("\\d{5}");
    }

    private boolean isValidCityOrStreet(String text) {
        return !text.isEmpty() && text.matches("[a-zA-Z]+");
    }

    private boolean isValidStreetNumber(String text) {
        // Überprüfen, ob die Straßennummer nicht leer ist
        return !text.isEmpty();
    }

    private boolean isValidDate(String text) {
        // Überprüfen, ob das Datum nicht leer ist und im Format dd.MM.yyyy vorliegt
        return !text.isEmpty() && text.matches("\\d{2}\\.\\d{2}\\.\\d{4}");
    }

    private boolean isValidTime(String text) {
        // Überprüfen, ob die Uhrzeit nicht leer ist und im Format HH:mm:ss vorliegt
        return !text.isEmpty() && text.matches("\\d{2}:\\d{2}:\\d{2}");
    }

    private boolean validateFields() {
        boolean isValid = true;

        if (!isValidZip(startZip.getText().toString())) {
            startZip.setError("Ungültige PLZ (5 Ziffern erforderlich)");
            isValid = false;
        }
        if (!isValidZip(endZip.getText().toString())) {
            endZip.setError("Ungültige PLZ (5 Ziffern erforderlich)");
            isValid = false;
        }
        if (!isValidCityOrStreet(startCity.getText().toString())) {
            startCity.setError("Ungültige Stadt (nur Buchstaben erlaubt)");
            isValid = false;
        }
        if (!isValidCityOrStreet(endCity.getText().toString())) {
            endCity.setError("Ungültige Stadt (nur Buchstaben erlaubt)");
            isValid = false;
        }
        if (!isValidStreetNumber(startStreet.getText().toString())) {
            startStreet.setError("Straße muss eingegeben werden");
            isValid = false;
        }
        if (!isValidStreetNumber(endStreet.getText().toString())) {
            endStreet.setError("Straße muss eingegeben werden");
            isValid = false;
        }
        if (!isValidStreetNumber(startNumber.getText().toString())) {
            startNumber.setError("Hausnummer muss eingegeben werden");
            isValid = false;
        }
        if (!isValidStreetNumber(endNumber.getText().toString())) {
            endNumber.setError("Hausnummer muss eingegeben werden");
            isValid = false;
        }
        if (!isValidDate(date.getText().toString())) {
            date.setError("Ungültiges Datum (Format: dd.MM.yyyy)");
            isValid = false;
        }
        if (!isValidTime(time.getText().toString())) {
            time.setError("Ungültige Uhrzeit (Format: HH:mm:ss)");
            isValid = false;
        }
        // Fügen Sie hier Validierungen für andere Felder hinzu

        return isValid;
    }

    private void saveRide(){
        NewRide ride = new NewRide();
        ride.setStartZip(startZip.getText().toString());
        ride.setStartCity(startCity.getText().toString());
        ride.setStartName(startName.getText().toString());
        ride.setStartStreet(startStreet.getText().toString());
        ride.setStartNumber(startNumber.getText().toString());
        ride.setEndName(endName.getText().toString());
        ride.setEndZip(endZip.getText().toString());
        ride.setEndCity(endCity.getText().toString());
        ride.setEndStreet(endStreet.getText().toString());
        ride.setEndNumber(endNumber.getText().toString());
        ride.setNotes(notes.getText().toString());
        ride.setDate(date.getText().toString());
        ride.setTime(time.getText().toString());

        DatabaseGlobal database = new DatabaseGlobal();
        database.writeToDatabaseRide(ride, new DatabaseGlobal.OnRideSaveListener() {
            @Override
            public void onRideSaved() {
                startActivity(new Intent(RideActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure() {
                Toast.makeText(RideActivity.this, "Fahrt wurde nicht gespeichert", Toast.LENGTH_SHORT).show();
            }
        });
    }
}