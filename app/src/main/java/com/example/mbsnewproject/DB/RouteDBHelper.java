package com.example.mbsnewproject.DB;

import com.example.mbsnewproject.Tracking.RouteData;

import java.util.ArrayList;

public interface RouteDBHelper {
    void save(RouteData routeData);
    RouteData load(String key);
    ArrayList<RouteData> loadAll();
    void remove(String key);
    void removeAll();
}
