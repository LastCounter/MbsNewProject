package com.example.mbsnewproject.Tracking;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        RouteData other = (RouteData) obj;

        return Objects.equals(routeTitle, other.routeTitle) &&
                Objects.equals(dateTime, other.dateTime) &&
                Objects.equals(timeNeeded, other.timeNeeded) &&
                Objects.equals(geoPoints, other.geoPoints) &&
                distance == other.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(routeTitle, dateTime, timeNeeded, geoPoints, distance);
    }

}
