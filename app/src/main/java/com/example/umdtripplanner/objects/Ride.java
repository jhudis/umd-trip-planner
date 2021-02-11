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

    /** The stops at which the rider is picked up and dropped off, respectively. */
    private Stop pickup, dropoff;

    /** The lat/long bounds of the ride. */
    private LatLngBounds bounds;

    /** The expected duration of the ride, in seconds. */
    private int duration;

    /**
     * Default constructor.
     * @param pickup  The stop at which the rider is picked up.
     * @param dropoff The stop at which the rider is dropped off.
     * @param stops   The full list of stops this ride's bus goes to, in order.
     * @param route   The route number of this ride's bus.
     */
    public Ride(Stop pickup, Stop dropoff, List<Stop> stops, int route) {
        this.pickup = pickup;
        this.dropoff = dropoff;
        this.duration = calculateDuration(stops, route);
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

    private int calculateDuration(List<Stop> stops, int route) {
        //Add the durations from stop to stop, starting at pickup and ending at dropoff, wrapping
        //around if necessary
        int curr = stops.indexOf(pickup), end = stops.indexOf(dropoff), next, duration = 0;
        do {
            next = curr + 1 == stops.size() ? 0 : curr + 1;
            duration += findGapDuration(stops.get(curr), stops.get(next), route);
            curr = next;
        } while (curr != end);
        return duration;
    }

    private int findGapDuration(Stop curr, Stop next, int route) {
        return MapsActivity.getDao().getDuration(route, curr.tag, next.tag);
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
