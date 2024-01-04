package com.java.trainticketbookingapp.Model;

import androidx.annotation.NonNull;

public class Ticket {
    private int id;
    private String start;
    private String destination;
    private String price;
    private String totalTime;
    private String departureTime;
    private String arrivalTime;
    private String trainID;
    private String departureDate;
    private String arrivalDate;
    private String ticketCode;


    public Ticket() {
    }

    public Ticket(int id, String start, String destination, String price, String totalTime, String departureTime, String arrivalTime, String trainID, String departureDate, String arrivalDate, String ticketCode) {
        this.id = id;
        this.start = start;
        this.destination = destination;
        this.price = price;
        this.totalTime = totalTime;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.trainID = trainID;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.ticketCode = ticketCode;
    }

    public Ticket(int id, String start, String destination, String price, String totalTime, String departureTime, String arrivalTime, String trainID, String departureDate, String ticketCode) {
        this.id = id;
        this.start = start;
        this.destination = destination;
        this.price = price;
        this.totalTime = totalTime;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.trainID = trainID;
        this.departureDate = departureDate;
        this.arrivalDate = "";
        this.ticketCode = ticketCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
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

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    @NonNull
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
                ", departureDate='" + departureDate + '\'' +
                ", arrivalDate='" + arrivalDate + '\'' +
                ", ticketCode='" + ticketCode + '\'' +
                '}';
    }
}
