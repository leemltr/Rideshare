package com.emmo.rideshare;

public class RideActivity {
    public void readRide(){
        DatabaseGlobal databaseGlobal = new DatabaseGlobal();
        databaseGlobal.readRideFromDatabase("rideId").thenAccept(ride -> {
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
}
