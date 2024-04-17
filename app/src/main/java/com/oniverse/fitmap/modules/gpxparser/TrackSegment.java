package com.oniverse.fitmap.modules.gpxparser;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import java.util.List;
/**
 * Model class for GPX track segments
 * Represents a track segment element in a GPX file
 * Contains a list of track points
 * @see TrackPoint
 * @see Track
 * @see Gpx
 * @see TrackPoint
 */
@Root(name="trkseg", strict=false)
public class TrackSegment {
    @ElementList(inline=true, required=false)
    private List<TrackPoint> trackPoints;

    // Getters
    public List<TrackPoint> getTrackPoints() {
        return trackPoints;
    }

    public TrackPoint getFirstTrackPoint() {
        return trackPoints.get(0);
    }

    public TrackPoint getLastTrackPoint() {
        return trackPoints.get(trackPoints.size() - 1);
    }
}
