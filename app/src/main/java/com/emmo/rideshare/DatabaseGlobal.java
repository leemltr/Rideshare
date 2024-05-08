package com.emmo.rideshare;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DatabaseGlobal {

    /*
    *
    *
    *
    USER
    *
    *
    *
     */

    private String encodeEmail(String email) {
        String encodedEmail = email.replace(".", "-dot-")
                .replace("#", "-hash-")
                .replace("$", "-dollar-")
                .replace("[", "-leftBracket-")
                .replace("]", "-rightBracket-");
        return encodedEmail;
    }

    public void writeToDatabaseUser(NewUser newUser, OnUserSavedListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        String userId = myRef.push().getKey();
        DatabaseReference userEmailsRef = database.getReference("userEmails");

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", userId);
        userMap.put("email", encodeEmail(newUser.getEmail()));
        userMap.put("firstname", newUser.getFirstname());
        userMap.put("lastname", newUser.getLastname());
        if (newUser.getHochschule() != null) {
            userMap.put("hochschule", newUser.getHochschule());
        }
        if (newUser.getZip() != null) {
            userMap.put("zip", newUser.getZip());
        }
        if (newUser.getCity() != null) {
            userMap.put("city", newUser.getCity());
        }
        if (newUser.getStreet() != null) {
            userMap.put("street", newUser.getStreet());
        }
        if (newUser.getStreetnumber() != null) {
            userMap.put("streetnumber", newUser.getStreetnumber());
        }

        assert userId != null;
        myRef.child(userId).setValue(userMap)
                .addOnSuccessListener(aVoid -> {
                    userEmailsRef.child(encodeEmail(newUser.getEmail())).setValue(userId);
                    listener.onUserSaved();
                })
                .addOnFailureListener(e -> {
                    // Fehler beim Anlegen
                });
    }

    // Rückruffunktion für die Benachrichtigung über das abgeschlossene Speichern des Benutzers
    public interface OnUserSavedListener {
        void onUserSaved();
    }

    public void checkUserExists(String userEmail, OnUserExistsListener listener) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Führt eine Abfrage aus, um den Benutzer mit der angegebenen E-Mail-Adresse zu finden
        usersRef.orderByChild("email").equalTo(encodeEmail(userEmail)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean userExists = dataSnapshot.exists();

                if (listener != null) {
                    listener.onUserExists(userExists);
                } else {
                    listener.onUserExists(userExists);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Fehler beim Lesen der Daten: " + databaseError.getMessage());

                if (listener != null) {
                    listener.onUserExists(false);
                }
            }
        });
    }

    // Listener-Interface für die Rückgabe des Ergebnisses
    public interface OnUserExistsListener {
        void onUserExists(boolean userExists);
    }

    public CompletableFuture<User> readUserFromDatabase(String email) {
        CompletableFuture<User> future = new CompletableFuture<>();
        DatabaseReference userEmailsRef = FirebaseDatabase.getInstance().getReference("userEmails");

        // Benutzer-ID anhand der E-Mail-Adresse suchen
        userEmailsRef.child(encodeEmail(email)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userId = dataSnapshot.getValue(String.class);
                if (userId != null) {
                    // Wenn eine Benutzer-ID gefunden wurde, werdem die Benutzerdaten anhand der ID gesucht
                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                    usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            if (user != null) {
                                future.complete(user);
                            } else {
                                future.completeExceptionally(new RuntimeException("Benutzer mit dieser E-Mail-Adresse nicht gefunden"));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            future.completeExceptionally(new RuntimeException(databaseError.getMessage()));
                        }
                    });
                } else {
                    future.completeExceptionally(new RuntimeException("Benutzer mit dieser E-Mail-Adresse nicht gefunden"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(new RuntimeException(databaseError.getMessage()));
            }
        });

        return future;
    }

    public interface OnUserUpdateListener {
        void onUserUpdateSuccess();
        void onUserUpdateFailure();
    }

    public void updateUserInDatabase(User user, OnUserUpdateListener listener){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");
        String userIdString = user.getId();
        String userEmail = user.getEmail();

        Query query = usersRef.orderByChild("email").equalTo(encodeEmail(userEmail));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String userId = snapshot.getKey();
                        DatabaseReference userToUpdateRef = usersRef.child(userId);

                        if (user.getFirstname() != null) {
                            userToUpdateRef.child("firstname").setValue(user.getFirstname());
                        }
                        if (user.getLastname() != null) {
                            userToUpdateRef.child("lastname").setValue(user.getLastname());
                        }
                        if (user.getStreet() != null) {
                            userToUpdateRef.child("street").setValue(user.getStreet());
                        }
                        if (user.getStreetnumber() != null) {
                            userToUpdateRef.child("streetnumber").setValue(user.getStreetnumber());
                        }
                        if (user.getCity() != null) {
                            userToUpdateRef.child("city").setValue(user.getCity());
                        }
                        if (user.getZip() != null) {
                            userToUpdateRef.child("zip").setValue(user.getZip());
                        }
                    }
                    listener.onUserUpdateSuccess();
                } else {
                    System.out.println("Benutzer mit der E-Mail-Adresse nicht gefunden");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Fehlerbehandlung
            }
        });
    }


    //NICHT IN BENUTZUNG
    public void deleteUserFromDatabase(String userId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        // Verweist auf den Benutzer anhand seiner ID und entfernt ihn
        usersRef.child(userId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Benutzer erfolgreich gelöscht
                    System.out.println("Benutzer erfolgreich gelöscht");
                })
                .addOnFailureListener(e -> {
                    // Fehler beim Löschen des Benutzers
                    System.out.println("Fehler beim Löschen des Benutzers: " + e.getMessage());
                });
    }

    /*
    *
    *
    *
    *
    *
    *
    *
    *
    *
    RIDE
    *
    *
    *
    *
    *
    *
    *
    *
     */

    public void writeToDatabaseRide(NewRide newRide, OnRideSaveListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ride");
        String rideId = myRef.push().getKey();
        String dateTime = newRide.getDate() + "_" + newRide.getTime();

        Map<String, Object> rideMap = new HashMap<>();
        rideMap.put("id", rideId);
        rideMap.put("email", newRide.getEmail());
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
        rideMap.put("date_time", dateTime);
        rideMap.put("notes", newRide.getNotes());

        assert rideId != null;
        myRef.child(rideId).setValue(rideMap)
                .addOnSuccessListener(aVoid -> {
                    // Erfolgreich angelegt
                    System.out.println("User erfolgreich angelegt");
                    listener.onRideSaved();
                })
                .addOnFailureListener(e -> {
                    // Fehler beim Anlegen
                    System.out.println("User wurde nicht angelegt");
                    listener.onFailure();
                });
    }

    public interface OnRideSaveListener{
        void onRideSaved();
        void onFailure();
    }

    public interface RideCallback {
        void onSuccessSingleRide(Ride ride);
        void onNotFound();
        void onFailure(String message);
    }

    public void findRidesByEmail(String email, OnRidesFoundListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ridesRef = database.getReference("ride");

        ridesRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Ride> ridesList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Ride ride = snapshot.getValue(Ride.class);
                    ridesList.add(ride);
                }
                listener.onSuccessRides(ridesList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled event
            }
        });
    }

    public interface OnRidesFoundListener {
        void onSuccessRides(List<Ride> rides);
    }

    public void findRideByDateTime(String date, String time, OnRidesFoundListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ride");

        Query query = myRef.orderByChild("date_time").startAt(date + "_" + time);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Ride> rides = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Ride ride = snapshot.getValue(Ride.class);
                    if (ride != null) {
                        rides.add(ride);
                    }
                }
                listener.onSuccessRides(rides);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Behandlung von Datenbankfehlern
            }
        });
    }

    public void findRideByDateTimeAndZips(String date, String time, String startZip, String endZip, OnRidesFoundListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ride");

        Query query = myRef.orderByChild("date_time").startAt(date + "_" + time);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Ride> rides = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Ride ride = snapshot.getValue(Ride.class);
                    if (ride != null && ride.getStartZip().equals(startZip) && ride.getEndZip().equals(endZip)) {
                        rides.add(ride);
                    }
                }
                listener.onSuccessRides(rides);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Behandlung von Datenbankfehlern
            }
        });
    }

    public void findRideByDateTimeAndCities(String date, String time, String startCity, String endCity, OnRidesFoundListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ride");

        Query query = myRef.orderByChild("date_time").startAt(date + "_" + time);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Ride> rides = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Ride ride = snapshot.getValue(Ride.class);
                    if (ride != null && ride.getStartCity().equals(startCity) && ride.getEndCity().equals(endCity)) {
                        rides.add(ride);
                    }
                }
                listener.onSuccessRides(rides);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Behandlung von Datenbankfehlern
            }
        });
    }

    public void updateRideInDatabase(String rideId, NewRide updatedRide) {
        DatabaseReference ridesRef = FirebaseDatabase.getInstance().getReference("ride").child(rideId);

        // Lese die alten Werte aus der Datenbank
        ridesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Alte Werte vorhanden
                    Ride oldRide = dataSnapshot.getValue(Ride.class);
                    assert oldRide != null;

                    // Aktualisiere nur die Felder, die in updatedRide vorhanden sind
                    if (updatedRide.getStartZip() != null) {
                        oldRide.setStartZip(updatedRide.getStartZip());
                    }
                    if (updatedRide.getStartCity() != null) {
                        oldRide.setStartCity(updatedRide.getStartCity());
                    }
                    if (updatedRide.getStartStreet() != null) {
                        oldRide.setStartStreet(updatedRide.getStartStreet());
                    }
                    if (updatedRide.getStartNumber() != null) {
                        oldRide.setStartNumber(updatedRide.getStartNumber());
                    }
                    if (updatedRide.getStartName() != null) {
                        oldRide.setStartName(updatedRide.getStartName());
                    }
                    if (updatedRide.getEndZip() != null) {
                        oldRide.setEndZip(updatedRide.getEndZip());
                    }
                    if (updatedRide.getEndCity() != null) {
                        oldRide.setEndCity(updatedRide.getEndCity());
                    }
                    if (updatedRide.getEndStreet() != null) {
                        oldRide.setEndStreet(updatedRide.getEndStreet());
                    }
                    if (updatedRide.getEndNumber() != null) {
                        oldRide.setEndNumber(updatedRide.getEndNumber());
                    }
                    if (updatedRide.getEndName() != null) {
                        oldRide.setEndName(updatedRide.getEndName());
                    }
                    if (updatedRide.getDate() != null && updatedRide.getTime() != null) {
                        String dateTime = updatedRide.getDate() + "_" + updatedRide.getTime();
                        oldRide.setDate_time(dateTime);
                    }
                    if (updatedRide.getNotes() != null) {
                        oldRide.setNotes(updatedRide.getNotes());
                    }

                    // Schreibt die aktualisierten Werte zurück in die Datenbank
                    ridesRef.setValue(oldRide)
                            .addOnSuccessListener(aVoid -> {
                                // Erfolgreich aktualisiert
                            })
                            .addOnFailureListener(e -> {
                                // Fehler beim Aktualisieren
                            });
                } else {
                    // Das Ride mit der angegebenen rideId existiert nicht
                    System.out.println("Diese Fahrt existiert nicht");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Fehler beim Lesen der Daten
            }
        });
    }


    public void deleteRideFromDatabase(String rideId) {
        DatabaseReference ridesRef = FirebaseDatabase.getInstance().getReference("ride").child(rideId);

        ridesRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Erfolgreich gelöscht
                })
                .addOnFailureListener(e -> {
                    // Fehler beim Löschen
                });
    }

    public void deleteRidesByEmailFromDatabase(String email) {
        DatabaseReference ridesRef = FirebaseDatabase.getInstance().getReference("ride");

        Query query = ridesRef.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue()
                            .addOnSuccessListener(aVoid -> {
                                // Erfolgreich gelöscht
                            })
                            .addOnFailureListener(e -> {
                                // Fehler beim Löschen
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Fehler beim Lesen der Daten
            }
        });
    }


}
