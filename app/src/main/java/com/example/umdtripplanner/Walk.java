package com.example.umdtripplanner;

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

    private LatLngBounds bounds;

    public Walk(LatLng origin, LatLng destination) {
        String url = "https://api.openrouteservice.org/v2/directions/foot-walking?" +
                "api_key=" + BuildConfig.ORS_KEY +
                "&start=" + origin.longitude + "," + origin.latitude +
                "&end=" + destination.longitude + "," + destination.latitude;

        try {
            JSONObject apiOutput = new JSONObject(new BufferedReader(
                    new InputStreamReader(new URL(url).openStream())).readLine());
            JSONArray jsonCoords = apiOutput.getJSONArray("features").getJSONObject(0)
                    .getJSONObject("geometry").getJSONArray("coordinates");
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
}
