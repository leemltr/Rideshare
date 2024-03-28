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
                +"name TEXT NOT NULL"
                +")";
        db.execSQL(strSQL1);

        String strSQL2 = "CREATE TABLE person("     //Eventuell noch Adresse hinzufügen?
                +"id INTEGER PRIMARY KEY AUTOINCREMENT,"
                +"firstname TEXT NOT NULL,"
                +"lastname TEXT NOT NULL"
                +")";
        db.execSQL(strSQL2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    // Fahrt wird gelöscht
    public void deleteRide(int id) {
        String strSQL = "DELETE FROM ride WHERE id = " + id;
        this.getWritableDatabase().execSQL(strSQL);
    }

}
