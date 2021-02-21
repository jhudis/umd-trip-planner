package com.example.umdtripplanner.objects;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Node;

import static com.example.umdtripplanner.objects.Utils.*;

public class Stop {

    /** The short tag and full title of this stop. */
    String tag, title;

    /** The lat/long coordinates of this stop. */
    LatLng coords;

    /**
     * Default constructor.
     * @param node A {@code <stop>} child of a {@code <route>}.
     */
    public Stop(Node node) {
        this.tag = getString(node, "tag");
        this.title = getString(node, "title");
        this.coords = getLatLng(node);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stop stop = (Stop) o;

        return tag.equals(stop.tag);
    }

    @Override
    public int hashCode() {
        return tag.hashCode();
    }
}
