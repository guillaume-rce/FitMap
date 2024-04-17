package com.oniverse.fitmap.modules.gpxparser;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
/**
 * Model class for GPX track points
 * Represents a track point element in a GPX file
 * Contains latitude, longitude, elevation, and time
 * @see Track
 */
@Root(name="trkpt", strict=false)
public class TrackPoint implements Serializable {
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