package com.example.umdtripplanner.objects;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.w3c.dom.Node;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Utils {

    /** Returns the distance from lat/lon pair a to pair b, squared. */
    static double distSquared(LatLng a, LatLng b) {
        return Math.pow(a.latitude - b.latitude, 2) + Math.pow(a.longitude - b.longitude, 2);
    }

    public static String getString(Node node, String name) {
        return node.getAttributes().getNamedItem(name).getNodeValue();
    }

    public static double getDouble(Node node, String name) {
        return Double.parseDouble(getString(node, name));
    }

    public static int getInt(Node node, String name) {
        return Integer.parseInt(getString(node, name));
    }

    public static LatLng getLatLng(Node node, String latName, String lonName) {
        return new LatLng(getDouble(node, latName), getDouble(node, lonName));
    }

    public static LatLng getLatLng(Node node) {
        return new LatLng(getDouble(node, "lat"), getDouble(node, "lon"));
    }

    public static LatLngBounds getBounds(List<LatLng> coords) {
        return new LatLngBounds(
                new LatLng(
                        Collections.min(coords, (o1, o2) -> Double.compare(o1.latitude, o2.latitude)).latitude,
                        Collections.min(coords, (o1, o2) -> Double.compare(o1.longitude, o2.longitude)).longitude),
                new LatLng(
                        Collections.max(coords, (o1, o2) -> Double.compare(o1.latitude, o2.latitude)).latitude,
                        Collections.max(coords, (o1, o2) -> Double.compare(o1.longitude, o2.longitude)).longitude));
    }

    public static LatLngBounds mergeBounds(LatLngBounds... boundsArray) {
        List<LatLngBounds> bounds = Arrays.asList(boundsArray);
        return new LatLngBounds(
                new LatLng(
                        Collections.min(bounds, (o1, o2) -> Double.compare(o1.southwest.latitude, o2.southwest.latitude)).southwest.latitude,
                        Collections.min(bounds, (o1, o2) -> Double.compare(o1.southwest.longitude, o2.southwest.longitude)).southwest.longitude),
                new LatLng(
                        Collections.max(bounds, (o1, o2) -> Double.compare(o1.northeast.latitude, o2.northeast.latitude)).northeast.latitude,
                        Collections.max(bounds, (o1, o2) -> Double.compare(o1.northeast.longitude, o2.northeast.longitude)).northeast.longitude));
    }

}
