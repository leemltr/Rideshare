package com.emmo.rideshare;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Ride implements Parcelable {
    private String id;
    private String idPerson;
    private String startZip;
    private String startCity;
    private String startStreet;
    private String startNumber;
    private String startName;
    private String endZip;
    private String endCity;
    private String endStreet;
    private String endNumber;
    private String endName;
    private String date_time;
    private String notes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(String idPerson) {
        this.idPerson = idPerson;
    }

    public String getStartZip() {
        return startZip;
    }

    public void setStartZip(String startZip) {
        this.startZip = startZip;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getStartStreet() {
        return startStreet;
    }

    public void setStartStreet(String startStreet) {
        this.startStreet = startStreet;
    }

    public String getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(String startNumber) {
        this.startNumber = startNumber;
    }

    public String getStartName() {
        return startName;
    }

    public void setStartName(String startName) {
        this.startName = startName;
    }

    public String getEndZip() {
        return endZip;
    }

    public void setEndZip(String endZip) {
        this.endZip = endZip;
    }

    public String getEndCity() {
        return endCity;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

    public String getEndStreet() {
        return endStreet;
    }

    public void setEndStreet(String endStreet) {
        this.endStreet = endStreet;
    }

    public String getEndNumber() {
        return endNumber;
    }

    public void setEndNumber(String endNumber) {
        this.endNumber = endNumber;
    }

    public String getEndName() {
        return endName;
    }

    public void setEndName(String endName) {
        this.endName = endName;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Ride() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(idPerson);
        dest.writeString(startCity);
        dest.writeString(startZip);
        dest.writeString(startStreet);
        dest.writeString(startName);
        dest.writeString(startNumber);
        dest.writeString(endCity);
        dest.writeString(endZip);
        dest.writeString(endStreet);
        dest.writeString(endName);
        dest.writeString(endNumber);
        dest.writeString(date_time);
        dest.writeString(notes);
    }

    protected Ride(Parcel in) {
        id = in.readString();
        idPerson = in.readString();
        startCity = in.readString();
        startZip = in.readString();
        startStreet = in.readString();
        startName = in.readString();
        startNumber = in.readString();
        endCity = in.readString();
        endZip = in.readString();
        endStreet = in.readString();
        endName = in.readString();
        endNumber = in.readString();
        date_time = in.readString();
        notes = in.readString();
    }

    public static final Creator<Ride> CREATOR = new Creator<Ride>() {
        @Override
        public Ride createFromParcel(Parcel in) {
            return new Ride(in);
        }

        @Override
        public Ride[] newArray(int size) {
            return new Ride[size];
        }
    };
}
