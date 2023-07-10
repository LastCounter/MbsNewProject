package com.example.mbsnewproject.Tracking;

import android.location.LocationListener;

public interface ITrackerService  {

    void locate();
    void startHandler();
    void stopHandler(String title);

}
