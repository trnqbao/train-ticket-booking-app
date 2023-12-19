package com.java.trainticketbookingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ticket {

    private int id = 0;
    private String start = "";
    private String destination = "";
    private String price = "";
    private String totalTime = "";
    private String departureTime = "";
    private String arrivalTime = "";
    private String trainID = "";
    private String date = "";

    public Ticket(int id, String start, String destination, String price, String totalTime, String departureTime, String arrivalTime, String trainID, String date) {
        this.id = id;
        this.start = start;
        this.destination = destination;
        this.price = price;
        this.totalTime = totalTime;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.trainID = trainID;
        this.date = date;
    }

    public Ticket() {
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public void setTrainID(String trainID) {
        this.trainID = trainID;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public String getTrainID() {
        return trainID;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
