package com.oniverse.fitmap.modules;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.oniverse.fitmap.R;
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
import java.time.format.DateTimeParseException;

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
            // Updated pattern with 'X' to correctly interpret 'Z' as the timezone designator
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
        }
        Instant instant = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                instant = Instant.from(formatter.parse(time));
            } catch (DateTimeParseException e) {
                // Handle the exception if the time string is not properly formatted
                e.printStackTrace();
                return null;
            }
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
                        PointValue p = new PointValue(
                                (float) getDistanceWithGpx(segmentPoints.get(0), point, gpx),
                                (float) point.getElevation());

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

    public static double getDistanceWithGpx(TrackPoint p1, TrackPoint p2, Gpx gpx) {
        double distance = 0;
        if (gpx != null) {
            List<TrackPoint> trackPoints = gpx.getTrackPointsBetween(p1, p2);
            if (trackPoints != null) {
                for (int i = 0; i < trackPoints.size() - 1; i++) {
                    distance += getDistance(trackPoints.get(i), trackPoints.get(i + 1));
                }
            }
        }
        return distance;
    }

    public static double getDistance(TrackPoint p1, TrackPoint p2) {
        double distance = 0;
        if (p1 != null && p2 != null) {
            double lat1 = p1.getLatitude();
            double lon1 = p1.getLongitude();
            double lat2 = p2.getLatitude();
            double lon2 = p2.getLongitude();
            double theta = lon1 - lon2;
            distance = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            distance = Math.acos(distance);
            distance = Math.toDegrees(distance);
            distance = distance * 60 * 1.1515;
            distance = distance * 1.609344;
        }
        return distance;
    }

    public static String getFormattedLength(double length) {
        length = length / 1000;
        return String.format("%.0fkm %.0fm", length, length % 1 * 1000);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static Drawable getDifficultyIcon(String difficulty, Context context) {
        if (difficulty == null)
            return context.getDrawable(R.drawable.ic_difficulty_unknow);

        Drawable icon = null;
        switch (difficulty) {
            case "low":
                icon = context.getDrawable(R.drawable.ic_difficulty_easy);
                break;
            case "medium":
                icon = context.getDrawable(R.drawable.ic_difficulty_medium);
                break;
            case "high":
                icon = context.getDrawable(R.drawable.ic_difficulty_high);
                break;

        }
        return icon;
    }
}
