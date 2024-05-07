package com.emmo.rideshare;

import java.util.List;

public class RideDatabase {
    public void readRide(String id){
        DatabaseGlobal databaseGlobal = new DatabaseGlobal();
        databaseGlobal.readRideFromDatabase(id).thenAccept(ride -> {
            // Ride erfolgreich ausgelesen
            // Zugriff auf die Werte des ausgelesenen Rides
            //String startZip = ride.getStartZip();
            //String startCity = ride.getStartCity();
            // Weitere Werte...
        }).exceptionally(ex -> {
            // Fehler beim Auslesen des Rides
            return null;
        });
    }

    public void findRidebyId(String userId){
        DatabaseGlobal databaseGlobal = new DatabaseGlobal();
        databaseGlobal.findRidesByUserId(userId, new DatabaseGlobal.OnRidesFoundListener() {
            @Override
            public void onSuccessRides(List<Ride> rides) {

            }
        });

    }
}
