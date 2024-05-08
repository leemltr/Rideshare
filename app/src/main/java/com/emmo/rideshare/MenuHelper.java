package com.emmo.rideshare;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class MenuHelper {
    public static void inflateMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    public static boolean handleMenuItemClick(MenuItem item, Context context) {
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.menu_Rideshare) {
            intent = new Intent(context, RideActivity.class);
            context.startActivity(intent);
            return true;
        } else if (id == R.id.menu_profile) {
            intent = new Intent(context, ProfileActivity.class);
            context.startActivity(intent);
            return true;
        } else if (id == R.id.menu_profile_edit) {
            intent = new Intent(context, EditProfileActivity.class);
            context.startActivity(intent);
            return true;
        } else if (id == R.id.menu_myrides) {
            intent = new Intent(context, myRidesActivity.class);
            context.startActivity(intent);
            return true;
        } else if (id == R.id.menu_logout) {
            FirebaseAuth.getInstance().signOut();
            intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
            context.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }
}
