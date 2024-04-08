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

    private Gpx gpx;

    public Gpx getGpx() {
        return gpx;
    }

    public void setGpx(Gpx gpx) {
        this.gpx = gpx;
    }

    public void setGpx(Gpx gpx, MapRenderer mapRenderer) {
        this.gpx = gpx;
        mapRenderer.drawPoint(gpx.getTrack().get(0).getFirstSegment().getFirstTrackPoint(),
                this.name);
    }

    public TrackPoint getStartPoint() {
        if (gpx == null) return null;
        return gpx.getTrack().get(0).getFirstSegment().getFirstTrackPoint();
    }

    public TrackPoint getEndPoint() {
        if (gpx == null) return null;
        return gpx.getTrack().get(0).getLastSegment().getLastTrackPoint();
    }

    public class Activity {
        public long id;
        public String name;
        public String i18n;
    }

    public class Elevation {
        public long ascent;
        public long descent;
    }
    
    public class Location {
        public String name;
    }
}
