package com.emmo.rideshare;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    public void writeToDatabaseUser(NewUser newUser) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        String userId = myRef.push().getKey();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", userId);
        userMap.put("email", newUser.getEmail());
        userMap.put("password", newUser.getPassword());
        userMap.put("firstname", newUser.getFirstname());
        userMap.put("lastname", newUser.getLastname());
        userMap.put("hochschule", newUser.getHochschule());
        userMap.put("zip", newUser.getZip());
        userMap.put("city", newUser.getCity());
        userMap.put("street", newUser.getStreet());
        userMap.put("streetnumber", newUser.getStreetnumber());

        assert userId != null;
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
        DatabaseReference usersRef = database.getReference("users"); // Pfad in deiner Datenbankstruktur

        // Führe eine Abfrage aus, um den Benutzer mit der angegebenen E-Mail-Adresse zu finden
        usersRef.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Datenänderungen wurden empfangen
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Iteriere über die gefundenen Benutzer
                    User user = snapshot.getValue(User.class);
                    // Hier kannst du mit dem gefundenen Benutzer arbeiten
                    assert user != null;
                    System.out.println("Benutzer gefunden: " + user.getId() + " " + user.getFirstname() + " " + user.getLastname());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Fehler beim Lesen der Daten
                System.out.println("Fehler beim Lesen der Daten: " + databaseError.getMessage());
            }
        });
    }

    public interface UserIdCallback {
        void onUserIdReceived(int userId);
        void onFailure(String errorMessage);
    }

    public void getUserIdFromEmail(String userEmail, UserIdCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        usersRef.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        int userId = user.getId();
                        callback.onUserIdReceived(userId);
                        return; // Beendet die Schleife nach dem Finden des Benutzers
                    }
                }
                // Kein Benutzer mit der angegebenen E-Mail-Adresse gefunden
                callback.onFailure("Benutzer mit E-Mail-Adresse nicht gefunden");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Fehler beim Lesen der Daten
                callback.onFailure(databaseError.getMessage());
            }
        });
    }

    /*
    public void readUserFromDatabase(int userId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users"); // Pfad in deiner Datenbankstruktur

        // Führe eine Abfrage aus, um den Benutzer mit der angegebenen ID zu finden
        usersRef.orderByChild("id").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Datenänderungen wurden empfangen
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Iteriere über die gefundenen Benutzer
                    User user = snapshot.getValue(User.class);
                    // Hier kannst du mit dem gefundenen Benutzer arbeiten
                    assert user != null;
                    System.out.println("Benutzer gefunden: " + user.getId() + " " + user.getFirstname() + " " + user.getLastname());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Fehler beim Lesen der Daten
                System.out.println("Fehler beim Lesen der Daten: " + databaseError.getMessage());
            }
        });
    }
     */

    public CompletableFuture<User> readUserFromDatabase(int userId) {
        CompletableFuture<User> future = new CompletableFuture<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        usersRef.child(String.valueOf(userId)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    future.complete(user);
                } else {
                    future.completeExceptionally(new RuntimeException("Benutzer mit dieser ID nicht gefunden"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(new RuntimeException(databaseError.getMessage()));
            }
        });

        return future;
    }

    public void updateUserInDatabase(User user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");
        String userIdString = String.valueOf(user.getId()); //Muss als String weitergegeben werden

        Map<String, Object> updateUserMap = new HashMap<>();
        updateUserMap.put("email", user.getEmail());
        updateUserMap.put("password", user.getPassword());
        updateUserMap.put("firstname", user.getFirstname());
        updateUserMap.put("lastname", user.getLastname());
        updateUserMap.put("hochschule", user.getHochschule());
        updateUserMap.put("zip", user.getZip());
        updateUserMap.put("city", user.getCity());
        updateUserMap.put("street", user.getStreet());
        updateUserMap.put("streetnumber", user.getStreetnumber());

        // Aktualisiere den Benutzer in der Datenbank
        usersRef.child(userIdString).updateChildren(updateUserMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Erfolgreich aktualisiert
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Fehler beim Aktualisieren
                    }
                });
    }

    public void deleteUserFromDatabase(String userId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        // Verweise auf den Benutzer anhand seiner ID und entferne ihn
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
    RIDE
    *
    *
    *
     */

    public void writeToDatabaseRide(NewRide newRide) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ride");
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

        assert rideId != null;
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

    public interface RideCallback {
        void onRideLoaded(Ride ride);
        void onRideNotFound();
        void onFailure(String message);
    }

    public interface RidesByUserCallback {
        void onRidesLoaded(List<Ride> rides);
        void onFailure(String message);
    }


    public void readRideById(String rideId, final RideCallback callback) {
        DatabaseReference ridesRef = FirebaseDatabase.getInstance().getReference("ride").child(rideId);

        ridesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Ride ride = dataSnapshot.getValue(Ride.class);
                    callback.onRideLoaded(ride);
                } else {
                    callback.onRideNotFound();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Fehler beim Lesen der Daten
                callback.onFailure(databaseError.getMessage());
            }
        });
    }

    public void readRidesByUserId(int userId, final RidesByUserCallback callback) {
        DatabaseReference ridesRef = FirebaseDatabase.getInstance().getReference("ride");

        Query query = ridesRef.orderByChild("idPerson").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Ride> rides = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Ride ride = snapshot.getValue(Ride.class);
                    rides.add(ride);
                }
                callback.onRidesLoaded(rides);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Fehler beim Lesen der Daten
                callback.onFailure(databaseError.getMessage());
            }
        });
    }

    public CompletableFuture<Ride> readRideFromDatabase(String rideId) {
        CompletableFuture<Ride> future = new CompletableFuture<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ridesRef = database.getReference("ride");

        ridesRef.child(rideId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Ride ride = dataSnapshot.getValue(Ride.class);
                if (ride != null) {
                    future.complete(ride);
                } else {
                    future.completeExceptionally(new RuntimeException("Ride mit dieser ID nicht gefunden"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(new RuntimeException(databaseError.getMessage()));
            }
        });

        return future;
    }


    //Sinnvoll NewRide statt Ride zu benutzen?
    public void updateRideInDatabase(String rideId, NewRide updatedRide) {
        DatabaseReference ridesRef = FirebaseDatabase.getInstance().getReference("ride").child(rideId);

        Map<String, Object> updatedRideMap = new HashMap<>();
        updatedRideMap.put("startZip", updatedRide.getStartZip());
        updatedRideMap.put("startCity", updatedRide.getStartCity());
        updatedRideMap.put("startStreet", updatedRide.getStartStreet());
        updatedRideMap.put("startNumber", updatedRide.getStartNumber());
        updatedRideMap.put("startName", updatedRide.getStartName());
        updatedRideMap.put("endZip", updatedRide.getEndZip());
        updatedRideMap.put("endCity", updatedRide.getEndCity());
        updatedRideMap.put("endStreet", updatedRide.getEndStreet());
        updatedRideMap.put("endNumber", updatedRide.getEndNumber());
        updatedRideMap.put("endName", updatedRide.getEndName());
        updatedRideMap.put("notes", updatedRide.getNotes());

        ridesRef.updateChildren(updatedRideMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Erfolgreich aktualisiert
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Fehler beim Aktualisieren
                    }
                });
    }

    public void deleteRideFromDatabase(String rideId) {
        DatabaseReference ridesRef = FirebaseDatabase.getInstance().getReference("ride").child(rideId);

        ridesRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Erfolgreich gelöscht
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Fehler beim Löschen
                    }
                });
    }

    public void deleteRidesByUserIdFromDatabase(int userId) {
        DatabaseReference ridesRef = FirebaseDatabase.getInstance().getReference("ride");

        Query query = ridesRef.orderByChild("idPerson").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Erfolgreich gelöscht
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Fehler beim Löschen
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Fehler beim Lesen der Daten
            }
        });
    }


}
