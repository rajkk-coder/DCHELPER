package com.example.dchelper.scholar;

public class Slot {
    private String owner;
    private String start_time;
    private String end_time;
    private String venue;
    private String date;
    private String status;

    public Slot() {
    }



    public String getOwner() {
        return owner;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getVenue() {
        return venue;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public Slot(String owner, String start_time, String end_time, String venue, String date, String status) {
        this.owner = owner;
        this.start_time = start_time;
        this.end_time = end_time;
        this.venue = venue;
        this.date = date;
        this.status = status;
    }
}
