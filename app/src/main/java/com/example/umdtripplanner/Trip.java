package com.example.umdtripplanner;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import static com.example.umdtripplanner.Utils.*;

public class Trip {

    private Ride ride;
    private Walk board, depart;
    private LatLngBounds bounds;

    public Trip(Bus bus, LatLng origin, LatLng destination) {
        ride = bus.closestRide(origin, destination);
        board = new Walk(origin, ride.pickup.coords);
        depart = new Walk(ride.dropoff.coords, destination);
        bounds = mergeBounds(ride.getBounds(), board.getBounds(), depart.getBounds());
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
}
