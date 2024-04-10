package com.oniverse.fitmap.modules;

import android.os.Build;

import com.oniverse.fitmap.modules.gpxparser.Gpx;
import com.oniverse.fitmap.modules.gpxparser.Track;
import com.oniverse.fitmap.modules.gpxparser.TrackPoint;
import com.oniverse.fitmap.modules.gpxparser.TrackSegment;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PointValue;

public class Utils {
    public static Duration getGpxDeltaTime(Gpx gpx) {
        Duration duration = null;
        if (gpx != null) {
            // Time will be like 2024-04-03T15:59:28Z
            String fTime = gpx.getTrack().get(0).getFirstSegment().getFirstTrackPoint().getTime();
            String lTime = gpx.getTrack().get(0).getLastSegment().getLastTrackPoint().getTime();
            ZonedDateTime fZonedDateTime = getZonedDateTime(fTime);
            ZonedDateTime lZonedDateTime = getZonedDateTime(lTime);
            if (fZonedDateTime != null && lZonedDateTime != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                duration = Duration.between(fZonedDateTime, lZonedDateTime);
            }
        }
        return duration;
    }

    public static ZonedDateTime getZonedDateTime(String time) {
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        }
        Instant instant = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            instant = Instant.from(formatter.parse(time));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && instant != null) {
            return ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
        }
        return null;
    }

    public static List<PointValue> getGpxElevationPoints(Gpx gpx) {
        List<PointValue> points = null;
        if (gpx != null) {
            List<Track> tracks = gpx.getTrack();
            for (Track track : tracks) {
                List<TrackSegment> segments = track.getSegments();
                for (TrackSegment segment : segments) {
                    List<TrackPoint> segmentPoints = segment.getTrackPoints();
                    for (TrackPoint point : segmentPoints) {
                        PointValue p = new PointValue((float) point.getElevation(),
                                (float) getDistance(segment.getFirstTrackPoint(), point));

                        if (points == null) {
                            points = new ArrayList<>();
                        }
                        points.add(p);
                    }
                }
            }

        }
        return points;
    }

    public static double getDistance(TrackPoint p1, TrackPoint p2) {
        double lat1 = p1.getLatitude();
        double lon1 = p1.getLongitude();
        double lat2 = p2.getLatitude();
        double lon2 = p2.getLongitude();
        double R = 6371e3; // metres
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (R * c);
    }
}
