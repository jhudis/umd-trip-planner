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
import org.w3c.dom.Node;
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

    final AtomicReference<Document> docRef = new AtomicReference<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getPath();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    private void getPath() {
        Thread thread = new Thread(() -> {
            synchronized (docRef) {
                try {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    docRef.set(db.parse(new URL("http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=umd&r=122").openStream()));
                } catch (IOException | SAXException | ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        synchronized (docRef) {
            Document doc = docRef.get();

            //Add path to map
            List<LatLng> points = new ArrayList<>();
            NodeList paths = doc.getElementsByTagName("path");
            for (int i = 0; i < paths.getLength(); i++) {
                NodeList path = paths.item(i).getChildNodes();
                for (int j = 0; j < path.getLength(); j++) {
                    Node point = path.item(j);
                    if (point.getAttributes() == null) continue;
                    points.add(new LatLng(getDouble(point, "lat"), getDouble(point, "lon")));
                }
            }
            map.addPolyline(new PolylineOptions().addAll(points));

            //Zoom to fit path
            final Node bounds = doc.getElementsByTagName("route").item(0);
            map.setOnMapLoadedCallback(() -> map.moveCamera(CameraUpdateFactory.newLatLngBounds(
                    new LatLngBounds(
                            new LatLng(getDouble(bounds, "latMin"), getDouble(bounds, "lonMin")),
                            new LatLng(getDouble(bounds, "latMax"), getDouble(bounds, "lonMax"))), 100)));
        }
    }

    private static double getDouble(Node node, String name) {
        return Double.parseDouble(node.getAttributes().getNamedItem(name).getNodeValue());
    }
}