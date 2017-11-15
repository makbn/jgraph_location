package io.github.makbn;

import java.sql.Timestamp;
import java.util.ArrayList;

public class LocationVertex{

    private int id;

    private double lat;

    private double lon;

    private String date;

    private String time;

    private int postManId;

    private String pCity;

    private boolean delivered=false;

    public LocationVertex(int id, double lat, double lon, String date, String time, int postManId, String pCity, boolean delivered) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.date = date;
        this.time = time;
        this.postManId = postManId;
        this.pCity = pCity;
        this.delivered = delivered;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPostManId() {
        return postManId;
    }

    public void setPostManId(int postManId) {
        this.postManId = postManId;
    }

    public String getpCity() {
        return pCity;
    }

    public void setpCity(String pCity) {
        this.pCity = pCity;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }
}
