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
        // Erzeuge ein CompletableFuture, um das Ergebnis asynchron zu erhalten
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Verwende die checkUserCredentials-Methode aus DatabaseGlobal
        DatabaseGlobal databaseGlobal = new DatabaseGlobal();
        // Setze das Ergebnis des Checks im CompletableFuture
        databaseGlobal.checkUserCredentials(email, password, future::complete);

        try {
            // Warte auf das Ergebnis des CompletableFuture und gib den boolean-Wert zurück
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            // Gib false zurück, falls ein Fehler auftritt
            return false;
        }
    }

    public boolean checkUserExists(String email) {
        DatabaseGlobal database = new DatabaseGlobal();
        AtomicBoolean userExists = new AtomicBoolean(false);

        // Aufruf der Methode zum Überprüfen des Benutzers
        // Setze den Wert von userExists entsprechend
        database.checkUserExists(email, userExists::set);

        // Gib den boolean-Wert zurück
        return userExists.get();
    }
}
