package com.emmo.rideshare;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserDatabase {

    public boolean checkUserExists(String email) {
        DatabaseGlobal database = new DatabaseGlobal();
        AtomicBoolean userExists = new AtomicBoolean(false);

        database.checkUserExists(email, userExists::set);

        return userExists.get();
    }
}
