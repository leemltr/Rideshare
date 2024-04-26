package com.emmo.rideshare;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DatabaseGlobal {

    public void writeToDatabaseUser(NewUser newUser) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("https://console.firebase.google.com/project/rideshare-9ec83/database/rideshare-9ec83-default-rtdb/data/~2F?hl=de");
        String userId = myRef.push().getKey();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", userId);
        userMap.put("email", newUser.getEmail());
        userMap.put("password", newUser.getPassword());
        userMap.put("firstname", newUser.getFirstname());
        userMap.put("lastname", newUser.getLastname());
        userMap.put("zip", newUser.getZip());
        userMap.put("city", newUser.getCity());
        userMap.put("street", newUser.getStreet());
        userMap.put("streetnumber", newUser.getStreetnumber());

        myRef.child(userId).setValue(userMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Erfolgreich angelegt
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Fehler beim Anlegen
                    }
                });
    }

    public void readUserFromDatabase(String userEmail) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("https://console.firebase.google.com/project/rideshare-9ec83/database/rideshare-9ec83-default-rtdb/data/~2F?hl=de"); // Pfad in deiner Datenbankstruktur

        // Führe eine Abfrage aus, um den Benutzer mit der angegebenen E-Mail-Adresse zu finden
        usersRef.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Datenänderungen wurden empfangen
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Iteriere über die gefundenen Benutzer
                    User user = snapshot.getValue(User.class);
                    // Hier kannst du mit dem gefundenen Benutzer arbeiten
                    System.out.println("Benutzer gefunden: " + user.getId() + " " + user.getFirstname() + " " + user.getLastname());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Fehler beim Lesen der Daten
                System.out.println("Fehler beim Lesen der Daten: " + databaseError.getMessage());
            }
        });
    }

    public void readUserFromDatabase(int userId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("https://console.firebase.google.com/project/rideshare-9ec83/database/rideshare-9ec83-default-rtdb/data/~2F?hl=de"); // Pfad in deiner Datenbankstruktur

        // Führe eine Abfrage aus, um den Benutzer mit der angegebenen ID zu finden
        usersRef.orderByChild("id").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Datenänderungen wurden empfangen
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Iteriere über die gefundenen Benutzer
                    User user = snapshot.getValue(User.class);
                    // Hier kannst du mit dem gefundenen Benutzer arbeiten
                    System.out.println("Benutzer gefunden: " + user.getId() + " " + user.getFirstname() + " " + user.getLastname());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Fehler beim Lesen der Daten
                System.out.println("Fehler beim Lesen der Daten: " + databaseError.getMessage());
            }
        });
    }

    public void writeToDatabaseRide(NewRide newRide) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("https://console.firebase.google.com/project/rideshare-9ec83/database/rideshare-9ec83-default-rtdb/data/~2F?hl=de");
        String rideId = myRef.push().getKey();

        Map<String, Object> rideMap = new HashMap<>();
        rideMap.put("id", rideId);
        rideMap.put("idPerson", newRide.getIdPerson());
        rideMap.put("startZip", newRide.getStartZip());
        rideMap.put("startCity", newRide.getStartCity());
        rideMap.put("startStreet", newRide.getStartStreet());
        rideMap.put("startNumber", newRide.getStartNumber());
        rideMap.put("startName", newRide.getStartName());
        rideMap.put("endZip", newRide.getEndZip());
        rideMap.put("endCity", newRide.getEndCity());
        rideMap.put("endStreet", newRide.getEndStreet());
        rideMap.put("endNumber", newRide.getEndNumber());
        rideMap.put("endName", newRide.getEndName());
        rideMap.put("notes", newRide.getNotes());

        myRef.child(rideId).setValue(rideMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Erfolgreich angelegt
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Fehler beim Anlegen
                    }
                });
    }

}
