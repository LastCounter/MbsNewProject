package com.example.mbsnewproject.Tracking;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.mbsnewproject.DB.RouteDBHelper;
import com.example.mbsnewproject.DB.SharedPreferenceHelper;
import com.example.mbsnewproject.R;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class TrackerHandler {
    private Context context;
    private MapView mapView;
    private List<Location> locationList;
    private ArrayList<GeoPoint> geoPoints;
    private RoadManager roadManager;
    private boolean tracking;

    private long startTime;
    private long myTime;


    private RouteDBHelper routeDBHelper;


    public TrackerHandler(Context context, MapView mapView) {
        this.mapView = mapView;
        this.context = context;
        this.geoPoints = new ArrayList<>();
        this.roadManager = new OSRMRoadManager(context, "TEST_AGENT");
        ((OSRMRoadManager) roadManager).setMean(OSRMRoadManager.MEAN_BY_FOOT);
        this.routeDBHelper = new SharedPreferenceHelper(context);


    }

    private void addMarkerOnMap(Location location) {
        GeoPoint geoPoint = new GeoPoint(location);
        Log.d("MapApp", "Geopoint Lon: " + geoPoint.getLongitude() + " Lat: " + geoPoint.getLatitude());

        Marker marker = new Marker(mapView);

        marker.setPosition(geoPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle("Start point");
        marker.setIcon(ActivityCompat.getDrawable(context, R.drawable.red_marker));
        mapView.getOverlays().add(marker);
        mapView.invalidate();
    }

    public void start() {

        startTime = System.currentTimeMillis();
        tracking = true;


    }

    public void stop(String title) {
        long stopTime = System.currentTimeMillis();
        myTime = stopTime - startTime;

        int h = (int) ((myTime / 1000) / 3600);
        int m = (int) (((myTime / 1000) / 60) % 60);
        int s = (int) ((myTime / 1000) % 60);
        String timeNeeded = h + ":" + m + ":" + s;
        Log.d("MapApp", "stop: " + timeNeeded);

        tracking = false;

        if (title == null) return;


        Log.d("MapApp", "stop: " + title);
        saveRoute(title, timeNeeded);
    }

    private void saveRoute(String title, String timeNeeded) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();


        Log.d("MapApp", "saveRoute: " + myTime);
        String dateTimeAsString = formatter.format(date);
        String routeTitle = title;

        Log.d("MapApp", "saveRoute: " + calculateDistance());
        double distance = calculateDistance();

        RouteData routeData = new RouteData(routeTitle, dateTimeAsString, timeNeeded, distance, new ArrayList<>(geoPoints));

        routeDBHelper.save(routeData);
        geoPoints.clear();

        mapView.getOverlays().clear();
        mapView.invalidate();

    }

    private double calculateDistance() {
        double distanceInMeters = 0;
        for (int i = 0; i < geoPoints.size() - 1; i++) {
            distanceInMeters = geoPoints.get(i).distanceToAsDouble(geoPoints.get(i + 1));
        }
       double distanceInKilometers = distanceInMeters/1000;
        double roundDistance = Math.round(distanceInKilometers * 100.0)/100.0;
        return roundDistance;

    }



    private void extendRouteFromLocation(Location location) {
        geoPoints.add(new GeoPoint(location));
        Road road = roadManager.getRoad(geoPoints);
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road, Color.MAGENTA, 10);
        mapView.getOverlays().add(roadOverlay);
        mapView.invalidate();
    }



    public void addLocation(Location location) {
        if (!tracking) return;

        addMarkerOnMap(location);
        extendRouteFromLocation(location);

    }

}
