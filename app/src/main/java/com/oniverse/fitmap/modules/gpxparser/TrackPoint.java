package com.oniverse.fitmap.modules.gpxparser;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="trkpt", strict=false)
public class TrackPoint {
    @Attribute(name="lat")
    private double latitude;

    @Attribute(name="lon")
    private double longitude;

    @Element(name="ele")
    private double elevation;

    @Element(name="time")
    private String time;

    // Getters
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getElevation() {
        return elevation;
    }

    public String getTime() {
        return time;
    }
}