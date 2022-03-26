package com.example.dchelper.admin.venue;

public class Venue {
    private  String name;
    private  String id;

    public Venue(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public Venue() {
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
