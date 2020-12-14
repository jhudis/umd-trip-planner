package com.example.umdtripplanner.objects;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Ride extends ArrayList<LatLng> {

    Stop pickup, dropoff;
    private LatLngBounds bounds;

    @Override
    public boolean addAll(@NonNull Collection<? extends LatLng> c) {
        boolean ret = super.addAll(c);

        List<LatLng> potentials = new ArrayList<>(c);
        if (bounds != null) {
            potentials.add(bounds.northeast);
            potentials.add(bounds.southwest);
        }

        bounds = Utils.getBounds(potentials);

        return ret;
    }

    public LatLngBounds getBounds() {
        return bounds;
    }
}
