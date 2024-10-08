package com.oniverse.fitmap.modules.tracks;

import android.content.Context;

import com.oniverse.fitmap.modules.MapRenderer;
import com.oniverse.fitmap.modules.gpxparser.Gpx;
import com.oniverse.fitmap.modules.gpxparser.TrackPoint;

import java.io.File;
import java.io.Serializable;

public class Track implements Serializable {
    public long id;
    public String name;
    public String difficulty;
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

    public boolean loadGpxTrack(Context context) {
        String basePath = context.getExternalFilesDir(null) + File.separator + "gpx";
        String path = basePath + File.separator + id + ".gpx";
        gpxTrack = new GpxTrack(path);
        return gpxTrack.gpxExists();
    }

    public boolean isGpxTrackValid() {
        if (gpxTrack == null || !gpxTrack.gpxExists() || gpxTrack.getGpx() == null) {
            return false;
        }
        try {
            Gpx gpx = gpxTrack.getGpx();
            if (gpx.getTrack().isEmpty()) {
                return false;
            }
            com.oniverse.fitmap.modules.gpxparser.Track start = gpx.getTrack().get(0);
            return !start.getSegments().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public static class Activity implements Serializable {
        public long id;
        public String name;
        public String i18n;
    }

    public static class Elevation implements Serializable {
        public long ascent;
        public long descent;
    }
    
    public static class Location implements Serializable {
        public String name;
        public TrackPoint point;
    }

    public static class GpxTrack implements Serializable {
        private String path;
        private Gpx gpx;

        public GpxTrack(String path) {
            this.path = path;
        }

        public boolean gpxExists() {
            return new File(path).exists();
        }

        private void parseGpx() {
            gpx = Gpx.readGpxFile(path);
        }

        private void purgeGpx() {
            gpx = null;
            System.gc();
            Runtime.getRuntime().gc();
        }

        public Gpx getGpx() {
            try {
                parseGpx();
                Gpx gpx = this.gpx;
                purgeGpx();
                return gpx;
            } catch (Exception e) {
                return null;
            }
        }
    }
}
