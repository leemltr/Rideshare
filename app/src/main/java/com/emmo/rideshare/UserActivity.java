package com.emmo.rideshare;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

    public boolean checkUser(String email, String password) {
        // Erzeuge ein CompletableFuture, um das Ergebnis asynchron zu erhalten
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Verwende die checkUserCredentials-Methode aus DatabaseGlobal
        DatabaseGlobal databaseGlobal = new DatabaseGlobal();
        databaseGlobal.checkUserCredentials(email, password, new DatabaseGlobal.OnCheckUserListener() {
            @Override
            public void onCheckUser(boolean userExists) {
                // Setze das Ergebnis des Checks im CompletableFuture
                future.complete(userExists);
            }
        });

        try {
            // Warte auf das Ergebnis des CompletableFuture und gib den boolean-Wert zurück
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            // Gib false zurück, falls ein Fehler auftritt
            return false;
        }
    }
}
