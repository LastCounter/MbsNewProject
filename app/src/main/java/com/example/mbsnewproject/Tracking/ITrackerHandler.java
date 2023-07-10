package com.example.mbsnewproject.Tracking;

import android.location.Location;

public interface ITrackerHandler {
    void addLocation(Location location);
    void stop(String title);
    void start();
}
