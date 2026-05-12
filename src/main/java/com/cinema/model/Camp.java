package com.cinema.model;

public class Camp {
    int id;
    String location;

    public Camp(int id, String location) {
        this.id = id;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Camp{" +
                "id=" + id +
                ", location='" + location + '\'' +
                '}';
    }
}
