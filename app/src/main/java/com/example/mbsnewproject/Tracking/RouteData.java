package com.example.mbsnewproject.Tracking;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class RouteData {
    private String routeTitle;
    private String dateTime;

    private String timeNeeded;
    private ArrayList<GeoPoint> geoPoints;
    private double distance;

    public RouteData(String routeTitle, String dateTime, String timeNeeded, double distance, ArrayList<GeoPoint> geoPoints){
        this.routeTitle = routeTitle;
        this.dateTime = dateTime;
        this.timeNeeded = timeNeeded;
        this.geoPoints = geoPoints;
        this.distance = distance;
    }

    public String getTimeNeeded() {
        return timeNeeded;
    }

    public String getRouteTitle() {
        return routeTitle;
    }

    public String getDateTime(){
        return dateTime;
    }

    public double getDistance() {
        return distance;
    }

    public ArrayList<GeoPoint> getGeoPoints() {
        return geoPoints;
    }
}
