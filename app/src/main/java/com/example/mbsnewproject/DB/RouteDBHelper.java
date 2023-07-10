package com.example.mbsnewproject.DB;

import com.example.mbsnewproject.Tracking.RouteData;

import java.util.ArrayList;

/**
 * Eine Schnittstelle zur Speicherung und zum Laden von Routendaten.
 */
public interface RouteDBHelper {
    /**
     * Speichert eine RouteData.
     * @param routeData Die zu speichernde RouteData.
     */
    void save(RouteData routeData);

    /**
     * Lädt eine RouteData anhand des Schlüssels.
     * @param key Der Schlüssel zum Laden der RouteData.
     * @return Die geladene RouteData oder null, falls keine RouteData mit dem
     * Schlüssel vorhanden ist.
     */
    RouteData load(String key);

    /**
     * Lädt alle gespeicherten RouteData-Objekte.
     * @return Eine ArrayList mit allen geladenen RouteData-Objekten.
     */
    ArrayList<RouteData> loadAll();

    /**
     * Entfernt eine RouteData anhand des Schlüssels.
     * @param key  Der Schlüssel der zu entfernenden RouteData.
     */
    void remove(String key);

    /**
     * Entfernt alle gespeicherten RouteData-Objekte.
     */
    void removeAll();

}
