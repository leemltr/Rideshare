package com.emmo.rideshare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private TextView vname, nname, email, street, streetnr, zip, city;
    private Button logout;

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
        logout = findViewById(R.id.btnLogout);

        loadUser();

        setDoubleTapListener(vname);
        setDoubleTapListener(nname);
        setDoubleTapListener(email);
        setDoubleTapListener(street);
        setDoubleTapListener(streetnr);
        setDoubleTapListener(zip);
        setDoubleTapListener(city);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
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


    public void signOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
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
                email.setText(emailString);
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