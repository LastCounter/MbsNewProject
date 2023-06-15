package com.example.mbsnewproject.DB;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.mbsnewproject.Tracking.RouteData;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class SharedPreferenceHelper implements RouteDBHelper {
    private Context context;
    private SharedPreferences sharedPreferences;

    public SharedPreferenceHelper(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("application", Context.MODE_PRIVATE);
    }

    @Override
    public void save(RouteData routeData) {
        Gson gson = new Gson();
        String myRouteAsJson = gson.toJson(routeData);
        Log.d("MapApp", "save: "+ myRouteAsJson);

        String routeKeyGenerate = routeData.getRouteTitle() + ": " + routeData.getDateTime();
        Log.d("MapApp", "save: "+ routeKeyGenerate);

        SharedPreferences sharedPreferences = context.getSharedPreferences("application", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(routeKeyGenerate, myRouteAsJson);
        editor.apply();
}


    @Override
    public RouteData load(String key) {
        //TODO lädt eine einzige RoutData
        SharedPreferences sharedPreferences = context.getSharedPreferences("application", Context.MODE_PRIVATE);
        String routeDataAsString = sharedPreferences.getString(key, null);
        Gson gson = new Gson();
        RouteData routeData = gson.fromJson(routeDataAsString, RouteData.class);

        return routeData;
    }

    @Override
    public ArrayList<RouteData> loadAll() {
        //TODO ruft meine load methode auf für jeden key
        ArrayList<RouteData> routeDataArrayList = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences("application", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (String entry  : allEntries.keySet()) {
            routeDataArrayList.add(load(entry));
        }

        return routeDataArrayList;
    }

    @Override
    public void removeAll(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("application", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (String entry  : allEntries.keySet()) {
           remove(entry);
        }
    }
    @Override
    public void remove(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("application", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }



}
