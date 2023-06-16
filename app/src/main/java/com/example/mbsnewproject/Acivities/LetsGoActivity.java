package com.example.mbsnewproject.Acivities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.mbsnewproject.R;
import com.example.mbsnewproject.Tracking.TrackerService;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class LetsGoActivity extends AppCompatActivity {

    private static final String[] PERMS = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private MapView mapView;
    private TrackerService trackerService;
    private Button startButton;
    private boolean started;
    private IMapController mapController;
    private Button centerButton;
    private Toolbar toolbar;
    private MyLocationNewOverlay locationOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lets_go);

        toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("Lets Go!");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        mapView = (MapView) findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);


        mapView.setMultiTouchControls(true);

        mapController = mapView.getController();
        this.trackerService = new TrackerService(this, mapView);

        requestPermissions(PERMS, 1);

        startButton = (Button) findViewById(R.id.startStopButton);
        startButton.setOnClickListener(v -> onButtonClick());
        centerButton = (Button) findViewById(R.id.centerButton);
        centerButton.setOnClickListener(v -> reCenterClick());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //TODO rework


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean permissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;


        if (permissionGranted) {
            this.locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
            this.locationOverlay.enableFollowLocation();
            mapView.getOverlays().add(this.locationOverlay);
            trackerService.locate();
        } else {
            GeoPoint startPoint = new GeoPoint(52.531677, 13.381777);
            mapController.setCenter(startPoint);
        }
        mapController.setZoom(15.0);

    }

    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        mapView.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        mapView.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onClickStart() {

        startButton.setText("STOP");
        trackerService.startHandler();
        toolbar.setTitle("GO GO GO!");

    }


    public void onClickStop() {
        toolbar.setTitle("Lets Go!");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("save")
                .setView(R.layout.save_route_popup)
                .setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        Button saveButton = (Button) dialog.findViewById(R.id.saveButton);
        EditText editText = (EditText) dialog.findViewById(R.id.inputTitle);
        saveButton.setOnClickListener(v -> {

            String title = editText.getText().toString();
            dialog.cancel();
            trackerService.stopHandler(title);
            mapView.getOverlays().add(locationOverlay);
        });
        Button cancelButton = (Button) dialog.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(v -> {
            dialog.cancel();
            trackerService.stopHandler(null);
        });


        startButton.setText("START");

    }

    public void onButtonClick() {
        if (!started) {
            onClickStart();
        } else onClickStop();

        started = !started;
    }

    private void reCenterClick() {
        if (locationOverlay.getMyLocation() != null) {


            mapView.getController().setCenter(locationOverlay.getMyLocation());
        } else {
            mapView.getController().setZoom(15.0);
        }
    }

    public MapView getMapView() {
        return mapView;
    }
}
