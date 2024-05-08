package com.emmo.rideshare;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserDatabase {
    public void readUser(String id){
        DatabaseGlobal databaseGlobal = new DatabaseGlobal();
        databaseGlobal.readUserFromDatabaseById(id).thenAccept(user -> {
            // User erfolgreich ausgelesen
            // Zugriff auf die Werte des ausgelesenen Users
            // Weitere Werte...
        }).exceptionally(ex -> {
            // Fehler beim Auslesen des Rides
            return null;
        });
    }

    public boolean checkUserExists(String email) {
        DatabaseGlobal database = new DatabaseGlobal();
        AtomicBoolean userExists = new AtomicBoolean(false);

        database.checkUserExists(email, userExists::set);

        return userExists.get();
    }
}
