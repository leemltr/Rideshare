package com.emmo.rideshare;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText vname, nname, street, streetnr, city, zip;
    private String originalVname, originalNname, originalStreet, originalStreetNr, originalCity, originalZip;
    private Button save, cancel;
    private FirebaseAuth mAuth;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.edit_profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        vname = findViewById(R.id.profile_vname);
        nname = findViewById(R.id.profile_nname);
        street = findViewById(R.id.profile_address_str);
        streetnr = findViewById(R.id.profile_address_nr);
        zip = findViewById(R.id.profile_address_plz);
        city = findViewById(R.id.profile_address_city);
        save = findViewById(R.id.update_btn_save);
        cancel = findViewById(R.id.update_btn_cancel);

        mAuth = FirebaseAuth.getInstance();
        loadUser();

        originalVname = vname.getText().toString();
        originalNname = nname.getText().toString();
        originalStreet = street.getText().toString();
        originalStreetNr = streetnr.getText().toString();
        originalCity = city.getText().toString();
        originalZip = zip.getText().toString();

        save.setOnClickListener(v -> updateUser());

        cancel.setOnClickListener(v -> {
            Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
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


    private void loadUser(){
        DatabaseGlobal database = new DatabaseGlobal();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            String emailString = currentUser.getEmail();
            database.readUserFromDatabase(emailString).thenAccept(user -> {
                vname.setText(user.getFirstname());
                nname.setText(user.getLastname());
                zip.setText(user.getZip());
                city.setText(user.getCity());
                street.setText(user.getStreet());
                streetnr.setText(user.getStreetnumber());
                id = user.getId();
            }).exceptionally(ex -> {
                ex.printStackTrace();
                return null;
            });
        }
    }

    private void updateUser(){
        if ((originalVname == null || !originalVname.equals(vname.getText().toString())) ||
                (originalNname == null || !originalNname.equals(nname.getText().toString())) ||
                (originalStreet == null || !originalStreet.equals(street.getText().toString())) ||
                (originalStreetNr == null || !originalStreetNr.equals(streetnr.getText().toString())) ||
                (originalCity == null || !originalCity.equals(city.getText().toString())) ||
                (originalZip == null || !originalZip.equals(zip.getText().toString()))) {
            User user = new User();
            user.setId(id);
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser != null) {
                String emailString = currentUser.getEmail();
                user.setEmail(emailString);
            }
            if ((originalVname == null && vname.getText() != null) || !originalVname.equals(vname.getText().toString())) {
                user.setFirstname(vname.getText().toString());
            }
            if ((originalNname == null && nname.getText() != null) || !originalNname.equals(nname.getText().toString())) {
                user.setLastname(nname.getText().toString());
            }

            if ((originalStreet == null && street.getText() != null) || !originalStreet.equals(street.getText().toString())) {
                user.setStreet(street.getText().toString());
            }

            if ((originalStreetNr == null && streetnr.getText() != null) || !originalStreetNr.equals(streetnr.getText().toString())) {
                user.setStreetnumber(streetnr.getText().toString());
            }

            if ((originalCity == null && city.getText() != null) || !originalCity.equals(city.getText().toString())) {
                user.setCity(city.getText().toString());
            }

            if ((originalZip == null && zip.getText() != null) || !originalZip.equals(zip.getText().toString())) {
                user.setZip(zip.getText().toString());
            }

            DatabaseGlobal database = new DatabaseGlobal();
            database.updateUserInDatabase(user, new DatabaseGlobal.OnUserUpdateListener() {
                @Override
                public void onUserUpdateSuccess() {
                    Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onUserUpdateFailure() {
                    Toast.makeText(EditProfileActivity.this, "Änderungen wurden nicht gespeichert", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Keine Änderungen vorgenommen
            Toast.makeText(this, "Keine Änderungen vorgenommen", Toast.LENGTH_SHORT).show();
        }
    }
}