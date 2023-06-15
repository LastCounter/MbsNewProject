package com.example.mbsnewproject.Tracking;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import org.osmdroid.views.MapView;

import java.util.List;

public class TrackerService extends Service implements LocationListener {
    Context context;
    double latitude;
    double longitude;
    LocationManager locationManager;
    Location location = null;
    private TrackerHandler trackerHandler;

    public TrackerService() {
    }

    //erklären
    public TrackerService(Context context, MapView mapView) {
        this.context = context;
        this.trackerHandler = new TrackerHandler(context, mapView);
    }


    //erklären
    public void locate() {

        try {
            //kiregen ein Location manager über den context
            locationManager = (LocationManager) this.context.getSystemService(LOCATION_SERVICE);
            //hatt der Benutzer ein aktives GPS oder ist es aus
            boolean istGPSActive = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetActive = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            //wir wollen das über GPS auslesen GPSPROVIDER, warten eine Minute(Millisekunden), Genauigkeit (10m) und ein Location Listener
            if (istGPSActive) {
                //updated
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 10, this);
                // kriegen unsere Location zurück
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } else if (isNetActive) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 6000, 10, this);
                // kriegen unsere Location zurück
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        //falls beide nicht an sind
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

        }

    }

    public void startTrackerHandler() {
        trackerHandler.start();
    }

    public void stopUpdates() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        trackerHandler.addLocation(location);
        Log.d("MyLocationTest", "onLocationChanged: " + location.getLongitude());
        Log.d("MyLocationTest", "onLocationChanged: " + location.getLatitude());
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    public double getLongitude() {
        if (location != null) {
            return location.getLongitude();
        }
        return 0;
    }

    public double getLatitude() {
        if (location != null) {
            return location.getLatitude();
        }
        return 0;
    }

    public void startHandler() {
        trackerHandler.start();
    }
    public void stopHandler(String title) {
        trackerHandler.stop(title);
    }
}