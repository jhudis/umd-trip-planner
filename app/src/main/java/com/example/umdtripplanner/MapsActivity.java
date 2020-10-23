package com.example.umdtripplanner;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    Trip trip;

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
                Document doc = null;
                try {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    doc = db.parse(new URL("http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=umd&r=132").openStream());
                } catch (IOException | SAXException | ParserConfigurationException e) {
                    e.printStackTrace();
                }
                assert doc != null;

                trip = new Trip(new Bus(doc), new LatLng(38.983095, -76.945778), new LatLng(38.980359, -76.939040));
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
            map.setOnMapLoadedCallback(() -> map.moveCamera(CameraUpdateFactory.newLatLngBounds(trip.getRide().getBounds(), 100)));
        }
    }
}