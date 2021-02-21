package com.example.umdtripplanner.objects;

import com.example.umdtripplanner.BuildConfig;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Walk extends ArrayList<LatLng> {

    /** The lat/long bounds of the walk. */
    private LatLngBounds bounds;

    /** The expected duration of the walk, in seconds. */
    private int duration;

    /** Mean radius of Earth, in meters. */
    private static final int EARTH_RADIUS_METERS = 6371000;

    /** Average walking speed, in meters per second. */
    private static final double WALKING_SPEED_MPS = 1.4;

    public Walk(LatLng origin, LatLng destination) {
        //Prepare URL for OpenRouteService request
        String url = "https://api.openrouteservice.org/v2/directions/foot-walking?" +
                "api_key=" + BuildConfig.ORS_KEY +
                "&start=" + origin.longitude + "," + origin.latitude +
                "&end=" + destination.longitude + "," + destination.latitude;

        try {
            //Make request
            JSONObject apiOutput = new JSONObject(new BufferedReader(new InputStreamReader(new URL(url).openStream())).readLine());
            JSONObject features = apiOutput.getJSONArray("features").getJSONObject(0);

            //Get duration
            duration = (int) features.getJSONObject("properties").getJSONObject("summary").getDouble("duration");

            //Get path
            JSONArray jsonCoords = features.getJSONObject("geometry").getJSONArray("coordinates");
            for (int i = 0; i < jsonCoords.length(); i++) {
                JSONArray jsonCoord = jsonCoords.getJSONArray(i);
                add(new LatLng(jsonCoord.getDouble(1), jsonCoord.getDouble(0)));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        bounds = Utils.getBounds(this);
    }

    public static int estimateDuration(LatLng origin, LatLng destination) {
        double lat1 = Math.toRadians(origin.latitude), lon1 = Math.toRadians(origin.longitude),
               lat2 = Math.toRadians(destination.latitude), lon2 = Math.toRadians(destination.longitude);
        //From https://en.wikipedia.org/wiki/Great-circle_distance#Formulae
        double radians = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1));
        double meters = radians * EARTH_RADIUS_METERS;
        double seconds = meters / WALKING_SPEED_MPS;
        return (int) seconds;
    }

    public LatLngBounds getBounds() {
        return bounds;
    }

    public int getDuration() {
        return duration;
    }
}
