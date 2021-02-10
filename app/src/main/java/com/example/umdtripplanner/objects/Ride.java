package com.example.umdtripplanner.objects;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Ride extends ArrayList<LatLng> {

    private Stop pickup, dropoff;
    private LatLngBounds bounds;
    private int duration;

    public Ride(Stop pickup, Stop dropoff) {
        this.pickup = pickup;
        this.dropoff = dropoff;
        this.duration = calculateDuration();
    }

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

    private int calculateDuration() {
        //TODO: Implement
        return 0;
    }

    public LatLngBounds getBounds() {
        return bounds;
    }

    public Stop getPickup() {
        return pickup;
    }

    public Stop getDropoff() {
        return dropoff;
    }

    public int getDuration() {
        return duration;
    }
}
