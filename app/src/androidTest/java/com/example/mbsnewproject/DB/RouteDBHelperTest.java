package com.example.mbsnewproject.DB;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import com.example.mbsnewproject.Tracking.*;
import android.util.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class RouteDBHelperTest {

    private RouteDBHelper routeDBHelper;
    Context context;


    @Before
    public void setUp() {
        // Initialisierung der Instanzen
        context = ApplicationProvider.getApplicationContext();
        routeDBHelper = new SharedPreferenceHelper(context,"Test");
    }



    @After
    public void tearDown() {
        try {
            // Aufräumen nach jedem Test (abhängig von der konkreten Implementierung)
            routeDBHelper.removeAll();
        } catch (Exception e) {
            // Protokollieren der Ausnahme mit Logcat
            Log.e("RouteDBHelperTest", "Error during cleanup: " + e.getMessage());
        }
    }





    @Test
    public void testSaveAndLoad() {
        // Erstellen eines RouteData-Objects
        RouteData routeData1 = new RouteData("myRoute1", "2023-06-16 09:00:00", "1:30:00", 10.5, new ArrayList<>());
        RouteData routeData2 = new RouteData("myRoute2", "2022-09-11 18:00:00", "3:00:00", 8.2, new ArrayList<>());


        routeDBHelper.save(routeData1);
        routeDBHelper.save(routeData2);

        // Lade aller RouteData-Objects
        ArrayList<RouteData> loadedRouteDataList = routeDBHelper.loadAll();

        // Ueberprüfung, ob die Anzahl der eladenen RouteData-Objects korrekt ist
        assertEquals(2, loadedRouteDataList.size());
        // Ueberprüfung, ob die RouteData-Objects korrekt geladen wurden
        RouteData loadedRouteData1 = loadedRouteDataList.get(0);
        RouteData loadedRouteData2 = loadedRouteDataList.get(1);

        //Überprüfung RouteData1
        assertEquals(routeData1.getRouteTitle(), loadedRouteData1.getRouteTitle());
        assertEquals(routeData1.getDateTime(), loadedRouteData1.getDateTime());
        assertEquals(routeData1.getTimeNeeded(), loadedRouteData1.getTimeNeeded());
        assertEquals(routeData1.getDistance(), loadedRouteData1.getDistance(), 0.001);
        assertArrayEquals(routeData1.getGeoPoints().toArray(), loadedRouteData1.getGeoPoints().toArray());

        //Überprüfung RouteData2
        assertEquals(routeData2.getRouteTitle(), loadedRouteData2.getRouteTitle());
        assertEquals(routeData2.getDateTime(), loadedRouteData2.getDateTime());
        assertEquals(routeData2.getTimeNeeded(), loadedRouteData2.getTimeNeeded());
        assertEquals(routeData2.getDistance(), loadedRouteData2.getDistance(), 0.001);
        assertArrayEquals(routeData2.getGeoPoints().toArray(), loadedRouteData2.getGeoPoints().toArray());
    }



    @Test
    public void testLoadSingleRouteData() {
        // Erstellen eines RouteData-Objects
        RouteData routeData = new RouteData("myRoute1", "2023-06-16 09:00:00", "1:30:00", 10.5, new ArrayList<>());

        // Speichern des RouteData-Objekts
        routeDBHelper.save(routeData);

        // Generieren des Schlüssels
        String routeKey = "myRoute1" + ": " + "2023-06-16 09:00:00";

        // Lade das RouteData-Objekt über den generierten Schlüssel
        RouteData loadedRouteData = routeDBHelper.load(routeKey);

        // Überprüfung, ob das geladene RouteData-Objekt korrekt ist
        assertNotNull(loadedRouteData); // Überprüfen, ob es nicht null ist
        assertEquals(routeData.getRouteTitle(), loadedRouteData.getRouteTitle());
        assertEquals(routeData.getDateTime(), loadedRouteData.getDateTime());
        assertEquals(routeData.getTimeNeeded(), loadedRouteData.getTimeNeeded());
        assertEquals(routeData.getDistance(), loadedRouteData.getDistance(), 0.001);
    }



    @Test
    public void testRemoveRouteData() {
        // Erstellen von RouteData-Objects
        RouteData routeData1 = new RouteData("myRoute1", "2023-06-16 09:00:00", "1:30:00", 10.5, new ArrayList<>());
        RouteData routeData2 = new RouteData("myRoute2", "2022-09-11 18:00:00", "3:00:00", 8.2, new ArrayList<>());

        // Speichern der RouteData-Objekte
        routeDBHelper.save(routeData1);
        routeDBHelper.save(routeData2);

        // Generieren des Schlüssels für das zu entfernende RouteData-Objekt
        String routeKey = "myRoute1" + ": " + "2023-06-16 09:00:00";

        // Entfernen eines RouteData-Objekts
        routeDBHelper.remove(routeKey);

        // Lade aller RouteData-Objekte
        ArrayList<RouteData> loadedRouteDataList = routeDBHelper.loadAll();

        // Überprüfung, ob nur das verbleibende RouteData-Objekt geladen wird
        assertEquals(1, loadedRouteDataList.size());
        RouteData remainingRouteData = loadedRouteDataList.get(0);
        assertEquals("myRoute2", remainingRouteData.getRouteTitle());
    }


    @Test
    public void testRemoveAllRouteData() {
        // Erstellen von RouteData-Objects
        RouteData routeData1 = new RouteData("myRoute1", "2023-06-16 09:00:00", "1:30:00", 10.5, new ArrayList<>());
        RouteData routeData2 = new RouteData("myRoute2", "2022-09-11 18:00:00", "3:00:00", 8.2, new ArrayList<>());

        // Speichern der RouteData-Objekte
        routeDBHelper.save(routeData1);
        routeDBHelper.save(routeData2);

        // Entfernen aller RouteData-Objekte
        routeDBHelper.removeAll();

        // Lade aller RouteData-Objekte
        ArrayList<RouteData> loadedRouteDataList = routeDBHelper.loadAll();

        // Überprüfung, ob keine RouteData-Objekte geladen werden und alle RouteData-Objekte entfernt wurden
        assertTrue(loadedRouteDataList.isEmpty());
        assertEquals(0, loadedRouteDataList.size());
    }


    @Test
    public void testRemoveSingleData() {
        // Erstellen und Speichern mehrerer RouteData-Objekte
        RouteData routeData1 = new RouteData("myRoute1", "2023-06-16 09:00:00", "1:30:00", 10.5, new ArrayList<>());
        RouteData routeData2 = new RouteData("myRoute2", "2022-09-11 18:00:00", "3:00:00", 8.2, new ArrayList<>());
        routeDBHelper.save(routeData1);
        routeDBHelper.save(routeData2);

        // Entfernen von routeData1
        String keyToRemove = "myRoute1: 2023-06-16 09:00:00";
        routeDBHelper.remove(keyToRemove);

        // Überprüfen, ob nur routeData1 entfernt wurde
        ArrayList<RouteData> remainingRouteDataList = routeDBHelper.loadAll();
        assertEquals(1, remainingRouteDataList.size());

        RouteData remainingRouteData = remainingRouteDataList.get(0);
        assertEquals(routeData2.getRouteTitle(), remainingRouteData.getRouteTitle());
        assertEquals(routeData2.getDateTime(), remainingRouteData.getDateTime());
    }


    @Test
    public void testSaveAndLoadWithSpecialCharacters() {
        // Erstellen eines RouteData-Objekts mit besonderen Zeichen im Titel
        RouteData routeData = new RouteData("myRoute@#^!*(", "2023-06-16 09:00:00", "1:30:00", 10.5, new ArrayList<>());
        routeDBHelper.save(routeData);

        // Laden des RouteData-Objekts
        String key = "myRoute@#^!*(: 2023-06-16 09:00:00";
        RouteData loadedRouteData = routeDBHelper.load(key);

        // Überprüfen, ob das geladene RouteData-Objekt den Erwartungen entspricht
        assertEquals(routeData.getRouteTitle(), loadedRouteData.getRouteTitle());
        assertEquals(routeData.getDateTime(), loadedRouteData.getDateTime());
    }



    @Test
    public void testLoadWithNonExistentKey() {
        // Versuch, ein RouteData-Objekt mit einem nicht vorhandenen Schlüssel zu laden
        String nonExistentKey = "nonExistentRoute: 2023-06-16 09:00:00";
        RouteData loadedRouteData = routeDBHelper.load(nonExistentKey);

        // Überprüfen, ob das geladene RouteData-Objekt null ist oder eine Ausnahme geworfen wird
        assertNull(loadedRouteData);
    }



    @Test
    public void testOverwritingSameKey() {
        RouteData routeData1 = new RouteData("myRoute", "2023-06-16 09:00:00", "1:30:00", 10.5, new ArrayList<>());
        RouteData routeData2 = new RouteData("myRoute", "2023-06-16 09:00:00", "2:00:00", 12.0, new ArrayList<>());

        routeDBHelper.save(routeData1);
        routeDBHelper.save(routeData2); // This should overwrite the first one

        // Loading by key
        String key = "myRoute: 2023-06-16 09:00:00";
        RouteData loadedRouteData = routeDBHelper.load(key);

        // Assert that the loaded data matches the second object
        assertEquals(routeData2.getTimeNeeded(), loadedRouteData.getTimeNeeded());
        assertEquals(routeData2.getDistance(), loadedRouteData.getDistance(), 0.001);
    }



    @Test
    public void testBoundaryValues() {
        // Create a RouteData object with large distance value
        RouteData routeData = new RouteData("myLongRoute", "2023-06-16 09:00:00", "100:00:00", Double.MAX_VALUE, new ArrayList<>());

        // Save and load the RouteData
        routeDBHelper.save(routeData);
        RouteData loadedRouteData = routeDBHelper.load("myLongRoute: 2023-06-16 09:00:00");

        // Assert that the loaded data matches the saved data
        assertEquals(routeData.getDistance(), loadedRouteData.getDistance(), 0.001);
    }




    @Test
    public void testLoadAllWhenEmpty() {
        // Zunächst sollten wir sicherstellen, dass der Speicher leer ist.
        routeDBHelper.removeAll();

        // Jetzt versuchen wir, alle RouteData-Objekte zu laden.
        ArrayList<RouteData> loadedRouteDataList = routeDBHelper.loadAll();

        // Überprüfen, ob die Liste leer ist, da der Speicher leer sein sollte.
        assertTrue(loadedRouteDataList.isEmpty());
    }


}


