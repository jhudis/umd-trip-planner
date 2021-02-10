package com.example.umdtripplanner.objects;

import androidx.annotation.NonNull;

import com.example.umdtripplanner.app.MapsActivity;
import com.example.umdtripplanner.room.GapsDao;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Ride extends ArrayList<LatLng> {

    private Stop pickup, dropoff;
    private LatLngBounds bounds;
    private int duration;

    public Ride(Stop pickup, Stop dropoff, List<Stop> stops) {
        this.pickup = pickup;
        this.dropoff = dropoff;
        this.duration = calculateDuration(stops);
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

    private int calculateDuration(List<Stop> stops) {
        int curr = stops.indexOf(pickup), end = stops.indexOf(dropoff), next, duration = 0;
        do {
            next = curr + 1 == stops.size() ? 0 : curr + 1;
            duration += findGapDuration(stops.get(curr), stops.get(next));
            curr = next;
        } while (curr != end);
        return duration;
    }

    private int findGapDuration(Stop curr, Stop next) {
        return MapsActivity.getDao().getDuration(132, "loop", curr.tag, "loop", next.tag);
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
