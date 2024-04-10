package com.oniverse.fitmap.modules.gpxparser;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Root(name="gpx", strict=false)
public class Gpx {
    @ElementList(inline=true)
    private List<Track> track;

    // Getters
    public List<Track> getTrack() {
        return track;
    }

    public List<TrackPoint> getTrackPointsBetween(TrackPoint start, TrackPoint end) {
        List<TrackPoint> trackPoints = null;
        boolean startFound = false;
        if (track != null) {
            for (Track t : track) {
                for (TrackSegment s : t.getSegments()) {
                    for (TrackPoint p : s.getTrackPoints()) {
                        if (p.equals(start)) {
                            trackPoints = new CopyOnWriteArrayList<>();
                            startFound = true;
                        }
                        if (startFound) {
                            trackPoints.add(p);
                        }
                        if (p.equals(end)) {
                            return trackPoints;
                        }
                    }
                }
            }
        }
        return trackPoints;
    }

    public static Gpx readGpxFile(String filePath) {
        Serializer serializer = new Persister();
        File source = new File(filePath);
        Gpx gpx = null;

        try {
            gpx = serializer.read(Gpx.class, source);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return gpx;
    }
}
