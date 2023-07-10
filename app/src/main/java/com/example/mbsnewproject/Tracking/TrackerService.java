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

public class TrackerService extends Service implements LocationListener, ITrackerService {
    Context context;
    double latitude;
    double longitude;
    LocationManager locationManager;
    Location location = null;
    private ITrackerHandler trackerHandler;

    public TrackerService() {
    }

    //erklären
    public TrackerService(Context context, ITrackerHandler trackerHandler) {
        this.context = context;
        this.trackerHandler = trackerHandler;

    }


    /**
     * Diese Methode verwendet den Location Manager, um den aktuellen Standort des Benutzers abzurufen.
     * @throws SecurityException Wenn die erforderlichen Berechtigungen für den Zugriff auf den Standort nicht vorhanden sind.
     *
     */
    public void locate() {
        try {
            locationManager = (LocationManager) this.context.getSystemService(LOCATION_SERVICE);
            boolean istGPSActive = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetActive = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (istGPSActive) {
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 10, this);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } else if (isNetActive) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 6000, 10, this);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
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

    /**
     * startet den Tracker-Handler.
     * @see TrackerHandler
     */
    public void startHandler() {
        trackerHandler.start();
    }
    /**
     * Stoppt den Tracker-Handler.
     * @param title Der Titel der aufgezeichneten Laufstrecke.
     * @see TrackerHandler
     */
    public void stopHandler(String title) {
        trackerHandler.stop(title);
    }
}