package com.emmo.rideshare;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserDatabase {
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

    public boolean checkUser(String email, String password) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        DatabaseGlobal databaseGlobal = new DatabaseGlobal();
        databaseGlobal.checkUserCredentials(email, password, future::complete);

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkUserExists(String email) {
        DatabaseGlobal database = new DatabaseGlobal();
        AtomicBoolean userExists = new AtomicBoolean(false);

        database.checkUserExists(email, userExists::set);

        return userExists.get();
    }
}
