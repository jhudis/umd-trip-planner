package com.example.umdtripplanner;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bus {

    List<LatLng> path;
    LatLngBounds bounds;
    List<Stop> stops;
    Map<Stop, LatLng> closestPoint;

    public Bus(Document doc) {
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

        Node route = doc.getElementsByTagName("route").item(0);
        bounds = new LatLngBounds(
                getLatLng(route, "latMin", "lonMin"),
                getLatLng(route, "latMax", "lonMax"));

        stops = new ArrayList<>();
        NodeList stopList = doc.getElementsByTagName("stop");
        for (int i = 0; i < stopList.getLength(); i++) {
            Node stop = stopList.item(i);
            if (stop.getAttributes() == null || stop.getAttributes().getLength() <= 1) continue;
            stops.add(new Stop(stop));
        }

        closestPoint = new HashMap<>();
        for (Stop stop : stops) {
            LatLng closest = Collections.min(path, (o1, o2) ->
                    Double.compare(distSquared(stop.coords, o1), distSquared(stop.coords, o2)));
            closestPoint.put(stop, closest);
        }
    }

    public List<LatLng> getPath(String fromTag, String toTag) {
        int fromIndex = 0, toIndex = 0;
        for (Stop stop : stops) {
            if (stop.tag.equals(fromTag)) fromIndex = path.indexOf(closestPoint.get(stop));
            if (stop.tag.equals(toTag)) toIndex = path.indexOf(closestPoint.get(stop));
        }
        return path.subList(fromIndex, toIndex + 1);
    }

    private static double distSquared(LatLng a, LatLng b) {
        return Math.pow(a.latitude - b.latitude, 2) + Math.pow(a.longitude - b.longitude, 2);
    }

    private static String getString(Node node, String name) {
        return node.getAttributes().getNamedItem(name).getNodeValue();
    }

    private static double getDouble(Node node, String name) {
        return Double.parseDouble(getString(node, name));
    }

    private static int getInt(Node node, String name) {
        return Integer.parseInt(getString(node, name));
    }

    private static LatLng getLatLng(Node node, String latName, String lonName) {
        return new LatLng(getDouble(node, latName), getDouble(node, lonName));
    }

    private static LatLng getLatLng(Node node) {
        return new LatLng(getDouble(node, "lat"), getDouble(node, "lon"));
    }

    private static class Stop {
        String tag, title;
        LatLng coords;
        int id;

        public Stop(Node node) {
            this.tag = getString(node, "tag");
            this.title = getString(node, "title");
            this.coords = getLatLng(node);
            this.id = getInt(node, "stopId");
        }
    }

}
