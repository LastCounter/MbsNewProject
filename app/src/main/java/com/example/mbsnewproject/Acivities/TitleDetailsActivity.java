package com.example.mbsnewproject.Acivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

import com.example.mbsnewproject.R;
import com.example.mbsnewproject.Tracking.RouteData;
import com.google.gson.Gson;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;


public class TitleDetailsActivity extends AppCompatActivity {
    private RouteData routeData;
    private MapView mapView;
    private RoadManager roadManager;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_details);

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);


        this.roadManager = new OSRMRoadManager(this, "TEST_AGENT");
        ((OSRMRoadManager) roadManager).setMean(OSRMRoadManager.MEAN_BY_FOOT);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Gson gson = new Gson();
        Intent intent = getIntent();
        String myRouteAsString = intent.getStringExtra("selectedRoute");
        routeData = gson.fromJson(myRouteAsString, RouteData.class);


        mapView = findViewById(R.id.mapDetails);
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        mapView.setMultiTouchControls(true);

        GeoPoint centerPoint = routeData.getGeoPoints().get(routeData.getGeoPoints().size() / 2);

        IMapController mapController = mapView.getController();
        mapController.setCenter(centerPoint);
        mapController.setZoom(12.5);

        drawRoute(routeData.getGeoPoints());

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(routeData.getRouteTitle());
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);

        textView1.setText(routeData.getTimeNeeded());
        textView2.setText(routeData.getDateTime());
        String distance = String.valueOf(routeData.getDistance());
        textView3.setText(distance + " km");


    }

    private void drawRoute(ArrayList<GeoPoint> geoPoints) {
        Road road = roadManager.getRoad(geoPoints);
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road, Color.MAGENTA, 10);
        mapView.getOverlays().add(roadOverlay);

        // Setzen Sie den Marker am Startpunkt
        if (!geoPoints.isEmpty()) {
            GeoPoint startPoint = geoPoints.get(0);
            Marker marker = new Marker(mapView);
            marker.setPosition(startPoint);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setTitle("Startpunkt");
            marker.setIcon(ActivityCompat.getDrawable(this, R.drawable.red_marker));

            mapView.getOverlays().add(marker);
        }
        if (!geoPoints.isEmpty()) {
            GeoPoint endPoint = geoPoints.get(geoPoints.size() - 1);
            Marker marker = new Marker(mapView);
            marker.setPosition(endPoint);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setTitle("Endpunkt");
            marker.setIcon(ActivityCompat.getDrawable(this, R.drawable.red_marker));

            mapView.getOverlays().add(marker);
        }

        mapView.invalidate();
    }


}