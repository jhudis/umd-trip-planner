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

    public LatLngBounds getBounds() {
        return bounds;
    }

    public int getDuration() {
        return duration;
    }
}
