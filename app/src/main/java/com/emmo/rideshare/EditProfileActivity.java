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
    private Button save;
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
        vname = findViewById(R.id.profile_vname);
        nname = findViewById(R.id.profile_nname);
        street = findViewById(R.id.profile_address_str);
        streetnr = findViewById(R.id.profile_address_nr);
        zip = findViewById(R.id.profile_address_plz);
        city = findViewById(R.id.profile_address_city);
        save = findViewById(R.id.update_btn);

        mAuth = FirebaseAuth.getInstance();
        loadUser();

        originalVname = vname.getText().toString();
        originalNname = nname.getText().toString();
        originalStreet = street.getText().toString();
        originalStreetNr = streetnr.getText().toString();
        originalCity = city.getText().toString();
        originalZip = zip.getText().toString();

        vname.addTextChangedListener(createTextWatcher(vname, () -> originalVname = vname.getText().toString()));
        nname.addTextChangedListener(createTextWatcher(nname, () -> originalNname = nname.getText().toString()));
        street.addTextChangedListener(createTextWatcher(street, () -> originalStreet = street.getText().toString()));
        streetnr.addTextChangedListener(createTextWatcher(streetnr, () -> originalStreetNr = streetnr.getText().toString()));
        city.addTextChangedListener(createTextWatcher(city, () -> originalCity = city.getText().toString()));
        zip.addTextChangedListener(createTextWatcher(zip, () -> originalZip = zip.getText().toString()));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
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

    private TextWatcher createTextWatcher(EditText editText, Runnable onChange) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nicht benötigt
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Nicht benötigt
            }

            @Override
            public void afterTextChanged(Editable s) {
                onChange.run();
            }
        };
    }

    private void updateUser(){
        if (!originalVname.equals(vname.getText().toString()) ||
                !originalNname.equals(nname.getText().toString()) ||
                !originalStreet.equals(street.getText().toString()) ||
                !originalStreetNr.equals(streetnr.getText().toString()) ||
                !originalCity.equals(city.getText().toString()) ||
                !originalZip.equals(zip.getText().toString())) {
            User user = new User();
            user.setId(id);
            if (!originalVname.equals(vname.getText().toString())) {
                user.setFirstname(vname.getText().toString());
            }
            if (!originalNname.equals(nname.getText().toString())) {
                user.setLastname(nname.getText().toString());
            }
            if (!originalStreet.equals(street.getText().toString())) {
                user.setStreet(street.getText().toString());
            }
            if (!originalStreetNr.equals(streetnr.getText().toString())) {
                user.setStreetnumber(streetnr.getText().toString());
            }
            if (!originalCity.equals(city.getText().toString())) {
                user.setCity(city.getText().toString());
            }
            if (!originalZip.equals(zip.getText().toString())) {
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