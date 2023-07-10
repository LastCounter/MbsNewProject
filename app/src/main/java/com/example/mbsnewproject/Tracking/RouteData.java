package com.example.mbsnewproject.Tracking;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

/**
 * Eine Klasse zur Repräsentation von Routendaten.
 */
public class RouteData {
    private String routeTitle;
    private String dateTime;
    private String timeNeeded;
    private ArrayList<GeoPoint> geoPoints;
    private double distance;

    /**
     * Konstruktor der RouteData-Klasse.
     * @param routeTitle Der Titel der Route.
     * @param dateTime Das Datum und die Uhrzeit der Aufzeichnung.
     * @param timeNeeded Die benötigte Zeit für die Route.
     * @param distance Die Distanz der Route.
     * @param geoPoints Die Liste der GeoPoints, die die Route repräsentieren.
     */
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
