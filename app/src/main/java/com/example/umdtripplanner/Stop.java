package com.example.umdtripplanner;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Node;

import static com.example.umdtripplanner.Utils.*;

public class Stop {
    String tag, title;
    LatLng coords;
    int id;

    public Stop(Node node) {
        this.tag = getString(node, "tag");
        this.title = getString(node, "title");
        this.coords = getLatLng(node);
        this.id = getInt(node, "stopId");
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
