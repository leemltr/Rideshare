package com.emmo.rideshare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView vname, nname, email, street, streetnr, zip, city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();

        vname = findViewById(R.id.profile_vname);
        nname = findViewById(R.id.profile_nname);
        email = findViewById(R.id.profile_email);
        street = findViewById(R.id.profile_address_str);
        streetnr = findViewById(R.id.profile_address_nr);
        zip = findViewById(R.id.profile_address_plz);
        city = findViewById(R.id.profile_address_city);

        loadUser();

        setDoubleTapListener(vname);
        setDoubleTapListener(nname);
        setDoubleTapListener(email);
        setDoubleTapListener(street);
        setDoubleTapListener(streetnr);
        setDoubleTapListener(zip);
        setDoubleTapListener(city);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setDoubleTapListener(TextView textView) {
        textView.setOnTouchListener(new View.OnTouchListener() {
            private final GestureDetector gestureDetector = new GestureDetector(ProfileActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(@NonNull MotionEvent e) {
                    // Öffne die gewünschte Aktivität hier
                    Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                    startActivity(intent);
                    return true;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    public void loadUser(){
        DatabaseGlobal database = new DatabaseGlobal();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            String emailString = currentUser.getEmail();
            database.readUserFromDatabase(emailString).thenAccept(user -> {
                vname.setText(user.getFirstname());
                nname.setText(user.getLastname());
                email.setText(user.getEmail());
                zip.setText(user.getZip());
                city.setText(user.getCity());
                street.setText(user.getStreet());
                streetnr.setText(user.getStreetnumber());
            }).exceptionally(ex -> {
                ex.printStackTrace();
                return null;
            });
        }
    }
}