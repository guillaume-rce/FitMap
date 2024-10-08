package com.oniverse.fitmap.modules.gpxparser;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;
/**
 * Model class for GPX tracks
 * Represents a track element in a GPX file
 * Contains a name and a list of track segments
 * @see TrackSegment
 */
@Root(name="trk", strict=false)
public class Track {
    @Element(name="name")
    private String name;

    @ElementList(inline=true, required=false)
    private List<TrackSegment> segments;

    // Getters
    public String getName() {
        return name;
    }

    public List<TrackSegment> getSegments() {
        return segments;
    }

    public TrackSegment getFirstSegment() {
        return segments.get(0);
    }

    public TrackSegment getLastSegment() {
        return segments.get(segments.size() - 1);
    }
}
