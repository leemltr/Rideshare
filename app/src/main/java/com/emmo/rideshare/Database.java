package com.emmo.rideshare;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="Rideshare.db";
    private static final int DATABASE_VERSION=1;

    public Database (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String strSQL1 = "CREATE TABLE ride("   //Adresse für Start + Ende, eventuell in Teil splitten?
                +"id INTEGER PRIMARY KEY AUTOINCREMENT,"
                +"idPerson INTEGER NOT NULL,"
                +"startZIP TEXT NOT NULL,"
                +"startCity TEXT NOT NULL,"
                +"startStreet TEXT NOT NULL,"
                +"startNumber TEXT NOT NULL,"
                +"startName TEXT,"
                +"endZIP TEXT NOT NULL,"
                +"endCity TEXT NOT NULL,"
                +"endStreet TEXT NOT NULL,"
                +"endNumber TEXT NOT NULL,"
                +"endName TEXT,"
                +"notes TEXT"
                +")";
        db.execSQL(strSQL1);

        String strSQL2 = "CREATE TABLE person("     //Eventuell noch Adresse hinzufügen? + Bild
                +"id INTEGER PRIMARY KEY AUTOINCREMENT,"
                +"firstname TEXT NOT NULL,"
                +"lastname TEXT NOT NULL,"
                +"zip TEXT NOT NULL,"
                +"city TEXT NOT NULL,"
                +"street TEXT NOT NULL,"
                +"streetnumber TEXT NOT NULL,"
                +"lastname TEXT NOT NULL"
                +")";
        db.execSQL(strSQL2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public void createUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        String firstname = user.getFirstname().replace("'","((%))");
        String lastname = user.getLastname().replace("'","((%))");
        String zip = user.getZip().replace("'","((%))");
        String city = user.getCity().replace("'","((%))");
        String street = user.getStreet().replace("'","((%))");
        String streetnumber = user.getStreetnumber().replace("'","((%))");
        String strSQL = "INSERT INTO person"
                +" (firstname, lastname, zip, city, street, streetnumber) VALUES ('"
                + firstname + "','" + lastname + "','" + zip + "','" + city + "','" + street + "','" + streetnumber + "')";
        db.execSQL(strSQL);
    }

    public void createRide(Ride ride) {

    }

    // Fahrt wird gelöscht
    public void deleteRide(int id) {
        String strSQL = "DELETE FROM ride WHERE id = " + id;
        this.getWritableDatabase().execSQL(strSQL);
    }

}
