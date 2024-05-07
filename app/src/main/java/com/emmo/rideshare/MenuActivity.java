package com.emmo.rideshare;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MenuActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.menu_Rideshare: {
                        Toast.makeText(MenuActivity.this, "Rideshare selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.menu_profile: {
                        Toast.makeText(MenuActivity.this, "Profile selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.menu_profile_edit: {
                        Toast.makeText(MenuActivity.this, "Profile edit selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.menu_myrides: {
                        Toast.makeText(MenuActivity.this, "My Rides selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.menu_logout: {
                        Toast.makeText(MenuActivity.this, "Logout selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else super.onBackPressed();
    }
}