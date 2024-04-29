package com.emmo.rideshare;

public class UserActivity {
    public void readUser(int id){
        DatabaseGlobal databaseGlobal = new DatabaseGlobal();
        databaseGlobal.readUserFromDatabase(id).thenAccept(user -> {
            // User erfolgreich ausgelesen
            // Zugriff auf die Werte des ausgelesenen Users
            // Weitere Werte...
        }).exceptionally(ex -> {
            // Fehler beim Auslesen des Rides
            return null;
        });
    }
}
