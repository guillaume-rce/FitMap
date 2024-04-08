package com.oniverse.fitmap.modules.tracks;

import com.oniverse.fitmap.modules.MapRenderer;
import com.oniverse.fitmap.modules.gpxparser.Gpx;
import com.oniverse.fitmap.modules.gpxparser.TrackPoint;

public class Track {
    public long id;
    public String name;
    public String created_at;
    public Activity activity;
    public String label_color;
    public long length;
    public boolean is_tested_by_user;
    public Elevation elevation;
    public Location start_location;
    public Location end_location;

    private GpxTrack gpxTrack;

    public GpxTrack getGpxTrack() {
        return gpxTrack;
    }

    public void setGpxTrack(GpxTrack gpxTrack) {
        this.gpxTrack = gpxTrack;
    }

    public TrackPoint getStartPoint() {
        gpxTrack.parseGpx();
        TrackPoint point = gpxTrack.gpx.getTrack().get(0).getFirstSegment().getFirstTrackPoint();
        gpxTrack.purgeGpx();
        return point;
    }

    public TrackPoint getEndPoint() {
        if (gpxTrack.gpx == null) return null;
        return gpxTrack.gpx.getTrack().get(0).getLastSegment().getLastTrackPoint();
    }

    public static class Activity {
        public long id;
        public String name;
        public String i18n;
    }

    public static class Elevation {
        public long ascent;
        public long descent;
    }
    
    public static class Location {
        public String name;
    }

    public static class GpxTrack {
        public String path;
        public Gpx gpx;

        public GpxTrack(String path) {
            this.path = path;
        }

        public void parseGpx() {
            gpx = Gpx.readGpxFile(path);
        }

        public void purgeGpx() {
            gpx = null;
            System.gc();
        }
    }
}
