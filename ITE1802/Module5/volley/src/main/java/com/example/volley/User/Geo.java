package com.example.volley.User;

public class Geo {
    private double lat, lng;

    public Geo(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Geo() {
        this(0.0, 0.0);
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "lat:" + lat + ", long: " + lng;
    }
}
