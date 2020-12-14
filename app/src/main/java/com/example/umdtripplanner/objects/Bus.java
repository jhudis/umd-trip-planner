package com.example.umdtripplanner.objects;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static com.example.umdtripplanner.objects.Utils.*;

public class Bus {

    /** The full, detailed path this bus travels. */
    List<LatLng> path;

    /** The max/min lat/lon of all the points on the path. */
    LatLngBounds bounds;

    /** The stops this bus stops at. */
    List<Stop> stops;

    /** A map of each stop to the closest point to it on the path. */
    Map<Stop, LatLng> closestPointToStops;

    /**
     * Default constructor.
     * @param number The bus number.
     */
    public Bus(int number) {
        //Poll NextBus API
        Document doc = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new URL("http://webservices.nextbus.com/service/publicXMLFeed?command=routeConfig&a=umd&r=" + number).openStream());
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        assert doc != null;

        //Load the path
        path = new ArrayList<>();
        NodeList segments = doc.getElementsByTagName("path");
        for (int i = 0; i < segments.getLength(); i++) {
            NodeList segment = segments.item(i).getChildNodes();
            for (int j = 0; j < segment.getLength(); j++) {
                Node point = segment.item(j);
                if (point.getAttributes() == null) continue;
                path.add(getLatLng(point));
            }
        }

        //Load the bounds
        Node route = doc.getElementsByTagName("route").item(0);
        bounds = new LatLngBounds(
                getLatLng(route, "latMin", "lonMin"),
                getLatLng(route, "latMax", "lonMax"));

        //Load the stops
        stops = new ArrayList<>();
        NodeList stopList = doc.getElementsByTagName("stop");
        for (int i = 0; i < stopList.getLength(); i++) {
            Node stop = stopList.item(i);
            if (stop.getAttributes() == null || stop.getAttributes().getLength() <= 1) continue;
            stops.add(new Stop(stop));
        }

        //Build stop-to-point list
        closestPointToStops = new HashMap<>();
        for (Stop stop : stops) {
            LatLng closest = Collections.min(path, (o1, o2) ->
                    Double.compare(distSquared(stop.coords, o1), distSquared(stop.coords, o2)));
            closestPointToStops.put(stop, closest);
        }
    }

    /**
     * Find the stops closest to the start location and end destination, respectively, and return
     * the path between those stops.
     * @param origin      Start location of the trip.
     * @param destination End location of the trip.
     * @return The ride with the shortest walking distance to/from the start/end stops, respectively.
     */
    public Ride closestRide(LatLng origin, LatLng destination) {
        return getRide(closestStop(origin), closestStop(destination));
    }

    /** Returns the closest stop to the given coords. */
    private Stop closestStop(LatLng coords) {
        return Collections.min(stops, (o1, o2) ->
                Double.compare(distSquared(coords, o1.coords), distSquared(coords, o2.coords)));
    }

    /**
     * Get the part of the path that runs between the given stops.
     * @param pickup  The stop at the start of the ride.
     * @param dropoff The stop at the end of the ride.
     * @return A list of points between the two given stops.
     */
    private Ride getRide(Stop pickup, Stop dropoff) {
        int fromIndex = path.indexOf(closestPointToStops.get(pickup));
        int toIndex = path.indexOf(closestPointToStops.get(dropoff)) + 1;

        Ride ride = new Ride();
        if (fromIndex <= toIndex) {
            ride.addAll(path.subList(fromIndex, toIndex));
        } else {
            ride.addAll(path.subList(fromIndex, path.size() - 1));
            ride.addAll(path.subList(0, toIndex));
        }
        ride.pickup = pickup;
        ride.dropoff = dropoff;

        return ride;
    }

}
