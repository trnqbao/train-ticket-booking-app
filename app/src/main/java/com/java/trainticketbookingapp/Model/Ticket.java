package com.java.trainticketbookingapp.Model;

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
    private String departureStation = "";


    public Ticket(int id, String start, String destination, String price, String totalTime, String departureTime, String arrivalTime, String trainID, String date, String departureStation) {
        this.id = id;
        this.start = start;
        this.destination = destination;
        this.price = price;
        this.totalTime = totalTime;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.trainID = trainID;
        this.date = date;
        this.departureStation = departureStation;
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

    public String getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(String departureStation) {
        this.departureStation = departureStation;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", start='" + start + '\'' +
                ", destination='" + destination + '\'' +
                ", price='" + price + '\'' +
                ", totalTime='" + totalTime + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", trainID='" + trainID + '\'' +
                ", date='" + date + '\'' +
                ", departureStation='" + departureStation + '\'' +
                '}';
    }
}
