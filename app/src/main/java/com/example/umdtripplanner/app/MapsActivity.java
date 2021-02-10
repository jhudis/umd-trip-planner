package com.example.umdtripplanner.app;

import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;

import android.graphics.Color;
import android.os.Bundle;

import com.example.umdtripplanner.objects.Bus;
import com.example.umdtripplanner.R;
import com.example.umdtripplanner.objects.Trip;
import com.example.umdtripplanner.room.AppDatabase;
import com.example.umdtripplanner.room.GapsDao;
import com.example.umdtripplanner.room.Gap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    Trip trip;
    static GapsDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getBus();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    private void getBus() {
        Thread thread = new Thread(() -> {
            synchronized (this) {
                AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, "gaps.db").createFromAsset("database/gaps.db").build();
                dao = db.gapsDao();

                trip = new Trip(new Bus(132), new LatLng(38.983095, -76.945778), new LatLng(38.980359, -76.939040));
                int u=0;
            }
        });
        thread.start();
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        synchronized (this) {
            map.addPolyline(new PolylineOptions().addAll(trip.getBoard()).color(Color.CYAN));
            map.addPolyline(new PolylineOptions().addAll(trip.getDepart()).color(Color.CYAN));
            map.addPolyline(new PolylineOptions().addAll(trip.getRide()));
            map.setOnMapLoadedCallback(() -> map.moveCamera(CameraUpdateFactory.newLatLngBounds(trip.getBounds(), 100)));
        }
    }

    public static GapsDao getDao() {
        return dao;
    }
}