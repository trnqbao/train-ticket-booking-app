package com.java.trainticketbookingapp.Model;

public class Ticket {
    private String id;
    private String start;
    private String destination;
    private int price;
    private String totalTime;
    private String departureTime;
    private String arrivalTime;
    private String trainID;
    private String date;

    public Ticket(String id, String start, String destination, int price, String totalTime, String departureTime, String arrivalTime, String trainID, String date) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public String getTrainID() {
        return trainID;
    }

    public void setTrainID(String trainID) {
        this.trainID = trainID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
