package com.emmo.rideshare;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditProfileActivity extends AppCompatActivity {
    private EditText vname, nname, email, street, streetnr, city, zip;
    private Button save;
    private AlertDialog exitConfirmationDialog;

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
        email = findViewById(R.id.profile_email);
        street = findViewById(R.id.profile_address_str);
        streetnr = findViewById(R.id.profile_address_nr);
        zip = findViewById(R.id.profile_address_plz);
        city = findViewById(R.id.profile_address_city);
        save = findViewById(R.id.update_btn);

        loadUser();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        exitConfirmationDialog = new AlertDialog.Builder(this)
                .setMessage("Möchten Sie die Aktivität wirklich verlassen?")
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish(); // Beenden Sie die Aktivität, wenn "Ja" ausgewählt wurde
                    }
                }).setNegativeButton("Nein", null).create();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exitConfirmationDialog.show();
    }

    public void loadUser(){

    }
}