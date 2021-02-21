package com.example.umdtripplanner.objects;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import static com.example.umdtripplanner.objects.Utils.*;

public class Trip {

    /** The bus ride taken on this trip. */
    private Ride ride;

    /** The walk from the origin to the bus and the walk from the bus to the destination, respectively */
    private Walk board, depart;

    /** The lat/long bounds of the trip. */
    private LatLngBounds bounds;

    /** The expected duration of the trip, in seconds. */
    private int duration;

    /**
     * Default constructor.
     * @param bus         The bus being taken.
     * @param origin      The origin of the trip.
     * @param destination The destination of the trip.
     */
    public Trip(Bus bus, LatLng origin, LatLng destination) {
        ride = bus.closestRide(origin, destination);
        board = new Walk(origin, ride.getPickup().coords);
        depart = new Walk(ride.getDropoff().coords, destination);
        bounds = mergeBounds(ride.getBounds(), board.getBounds(), depart.getBounds());
        duration = ride.getDuration() + board.getDuration() + depart.getDuration();
    }

    /**
     * Estimate the duration of the trip by not invoking the OpenRouteService API to avoid rate
     * limiting.
     * @param bus         The bus being taken.
     * @param origin      The origin of the trip.
     * @param destination The destination of the trip.
     */
    public static int estimateDuration(Bus bus, LatLng origin, LatLng destination) {
        Ride ride = bus.closestRide(origin, destination);
        int board = Walk.estimateDuration(origin, ride.getPickup().coords);
        int depart = Walk.estimateDuration(ride.getDropoff().coords, destination);
        return ride.getDuration() + board + depart;
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
