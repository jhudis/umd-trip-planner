package com.example.umdtripplanner;

import com.google.android.gms.maps.model.LatLng;

public class Trip {

    private Ride ride;
    private Walk board, depart;

    public Trip(Bus bus, LatLng origin, LatLng destination) {
        ride = bus.closestRide(origin, destination);
        board = new Walk(origin, ride.pickup.coords);
        depart = new Walk(ride.dropoff.coords, destination);
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
}
