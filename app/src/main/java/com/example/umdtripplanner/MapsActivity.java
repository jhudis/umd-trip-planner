package com.example.umdtripplanner;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        AtomicReference<Document> docRef = new AtomicReference<>();
        Thread thread = new Thread(() -> {
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                docRef.set(db.parse(new URL("http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=umd&r=104").openStream()));
            } catch (IOException | SAXException | ParserConfigurationException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try { thread.join(); } catch (InterruptedException e) { e.printStackTrace(); }
        Document doc = docRef.get();

        NodeList paths = doc.getElementsByTagName("path");
        for (int i = 0; i < paths.getLength(); i++) {
            List<LatLng> points = new ArrayList<>();
            NodeList path = paths.item(i).getChildNodes();
            for (int j = 0; j < path.getLength(); j++) {
                NamedNodeMap point = path.item(j).getAttributes();
                if (point == null) continue;
                points.add(new LatLng(
                        Double.parseDouble(point.getNamedItem("lat").getNodeValue()),
                        Double.parseDouble(point.getNamedItem("lon").getNodeValue())));
            }
            map.addPolyline(new PolylineOptions().addAll(points));
        }

        final NamedNodeMap bounds = doc.getElementsByTagName("route").item(0).getAttributes();
        map.setOnMapLoadedCallback(() -> map.moveCamera(CameraUpdateFactory.newLatLngBounds(
                new LatLngBounds(
                        new LatLng(Double.parseDouble(bounds.getNamedItem("latMin").getNodeValue()), Double.parseDouble(bounds.getNamedItem("lonMin").getNodeValue())),
                        new LatLng(Double.parseDouble(bounds.getNamedItem("latMax").getNodeValue()), Double.parseDouble(bounds.getNamedItem("lonMax").getNodeValue()))), 100)));
    }
}