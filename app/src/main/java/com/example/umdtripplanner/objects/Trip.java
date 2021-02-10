package com.example.umdtripplanner.objects;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import static com.example.umdtripplanner.objects.Utils.*;

public class Trip {

    private Ride ride;
    private Walk board, depart;
    private LatLngBounds bounds;
    private int duration;

    public Trip(Bus bus, LatLng origin, LatLng destination) {
        ride = bus.closestRide(origin, destination);
        board = new Walk(origin, ride.getPickup().coords);
        depart = new Walk(ride.getDropoff().coords, destination);
        bounds = mergeBounds(ride.getBounds(), board.getBounds(), depart.getBounds());
        duration = ride.getDuration() + board.getDuration() + depart.getDuration();
    }

    public Ride getRide() {
        return ride;
    }

    public Walk getBoard() {
        return board;
    }

    public Walk getDepart() {
        return depart;
    }

    public LatLngBounds getBounds() {
        return bounds;
    }

    public int getDuration() {
        return duration;
    }
}
