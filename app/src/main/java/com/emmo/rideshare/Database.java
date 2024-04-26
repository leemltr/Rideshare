package com.emmo.rideshare;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

        String strSQL2 = "CREATE TABLE person("     //Eventuell noch Bild hinzufügen?
                +"id INTEGER PRIMARY KEY AUTOINCREMENT,"
                +"email TEXT NOT NULL,"
                +"password TEXT NOT NULL,"
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

    public void createUser(NewUser newUser){
        SQLiteDatabase db = this.getWritableDatabase();
        String email = newUser.getEmail().replace("'","((%))");
        String password = newUser.getPassword().replace("'","((%))");
        String firstname = newUser.getFirstname().replace("'","((%))");
        String lastname = newUser.getLastname().replace("'","((%))");
        String zip = newUser.getZip().replace("'","((%))");
        String city = newUser.getCity().replace("'","((%))");
        String street = newUser.getStreet().replace("'","((%))");
        String streetnumber = newUser.getStreetnumber().replace("'","((%))");
        String strSQL = "INSERT INTO person"
                +" (email, password, firstname, lastname, zip, city, street, streetnumber) VALUES ('"
                + email + "','" + password + "','" + firstname + "','" + lastname + "','" + zip + "','" + city + "','" + street + "','" + streetnumber + "')";
        db.execSQL(strSQL);
    }

    public void createRide(NewRide newRide) {

    }

    // Fahrt wird gelöscht
    public void deleteRide(int id) {
        String strSQL = "DELETE FROM ride WHERE id = " + id;
        this.getWritableDatabase().execSQL(strSQL);
    }

}
