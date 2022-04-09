package com.example.dchelper.scholar.booking;

public class Slot implements Comparable<Slot>{
    private String owner;
    private String start_time;
    private String end_time;
    private String venue;
    private String date;
    private String status;
    private String mode;
    private String currentDateAndTime;
    public Slot() {
    }

    public Slot(String owner, String start_time, String end_time, String venue, String date, String status, String mode, String currentDateAndTime) {
        this.owner = owner;
        this.start_time = start_time;
        this.end_time = end_time;
        this.venue = venue;
        this.date = date;
        this.status = status;
        this.mode= mode;
        this.currentDateAndTime = currentDateAndTime;
    }

    public Slot(String owner, String start_time, String end_time, String venue, String date, String status) {
        this.owner = owner;
        this.start_time = start_time;
        this.end_time = end_time;
        this.venue = venue;
        this.date = date;
        this.status = status;
    }

    public Slot(String owner, String start_time, String end_time, String venue, String date, String status, String mode) {
        this.owner = owner;
        this.start_time = start_time;
        this.end_time = end_time;
        this.venue = venue;
        this.date = date;
        this.status = status;
        this.mode=mode;
    }

    public Slot(String owner, String userStartTime, String userEndTime, String venue, String date) {
        this.owner = owner;
        this.start_time = userStartTime;
        this.end_time = userEndTime;
        this.venue = venue;
        this.date = date;
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

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setCurrentDateAndTime(String currentDateAndTime) {
        this.currentDateAndTime = currentDateAndTime;
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

    public String getMode() {return mode;}

    public String getCurrentDateAndTime() {
        return currentDateAndTime;
    }

    @Override
    public int compareTo(Slot slot) {
        String s1= start_time;
        String s2= slot.getStart_time();
        String[] token1=s1.split(":");
        String[] token2=s2.split(":");
        int ss1 = Integer.parseInt(token1[0])*60+Integer.parseInt(token1[1]);
        int ss2 = Integer.parseInt(token2[0])*60+Integer.parseInt(token2[1]);
        return ss1-ss2;
    }
}
