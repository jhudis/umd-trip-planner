package com.example.umdtripplanner;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Node;

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

}
