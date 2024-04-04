package com.oniverse.fitmap.modules.gpxparser;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import java.util.List;

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
