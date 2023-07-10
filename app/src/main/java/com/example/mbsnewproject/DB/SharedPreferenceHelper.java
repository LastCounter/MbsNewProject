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
    private String preference;

    public SharedPreferenceHelper(Context context) {
        this.context = context;
        this.preference = "application";
        sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);

    }
    public SharedPreferenceHelper(Context context, String preference){
        this.context = context;
        this.preference = preference;
        sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
    }

    /**
     *
     * @param routeData
     */
    @Override
    public void save(RouteData routeData) {
        Gson gson = new Gson();
        String myRouteAsJson = gson.toJson(routeData);
        Log.d("MapApp", "save: " + myRouteAsJson);

        String routeKeyGenerate = routeData.getRouteTitle() + ": " + routeData.getDateTime();
        Log.d("MapApp", "save: " + routeKeyGenerate);

        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(routeKeyGenerate, myRouteAsJson);
        editor.apply();
    }

    /**
     *
     * @param key
     * @return
     */
    @Override
    public RouteData load(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        String routeDataAsString = sharedPreferences.getString(key, null);
        Gson gson = new Gson();
        RouteData routeData = gson.fromJson(routeDataAsString, RouteData.class);

        return routeData;
    }

    /**
     *
     * @return
     */
    @Override
    public ArrayList<RouteData> loadAll() {
        ArrayList<RouteData> routeDataArrayList = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (String entry : allEntries.keySet()) {
            routeDataArrayList.add(load(entry));
        }

        return routeDataArrayList;
    }

    /**
     *
     */
    @Override
    public void removeAll() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (String entry : allEntries.keySet()) {
            remove(entry);
        }
    }

    /**
     *
     * @param key
     */
    @Override
    public void remove(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }


}
