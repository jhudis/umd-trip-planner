package com.example.umdtripplanner;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Ride extends ArrayList<LatLng> {

    private LatLngBounds bounds;

    @Override
    public boolean addAll(@NonNull Collection<? extends LatLng> c) {
        boolean ret = super.addAll(c);

        List<LatLng> potentials = new ArrayList<>(c);
        if (bounds != null) {
            potentials.add(bounds.northeast);
            potentials.add(bounds.southwest);
        }

        bounds = new LatLngBounds(
                new LatLng(
                        Collections.min(potentials, (o1, o2) -> Double.compare(o1.latitude, o2.latitude)).latitude,
                        Collections.min(potentials, (o1, o2) -> Double.compare(o1.longitude, o2.longitude)).longitude),
                new LatLng(
                        Collections.max(potentials, (o1, o2) -> Double.compare(o1.latitude, o2.latitude)).latitude,
                        Collections.max(potentials, (o1, o2) -> Double.compare(o1.longitude, o2.longitude)).longitude));

        return ret;
    }

    public LatLngBounds getBounds() {
        return bounds;
    }
}
