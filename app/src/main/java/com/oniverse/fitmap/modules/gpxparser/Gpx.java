package com.oniverse.fitmap.modules.gpxparser;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import java.io.File;
import java.util.List;

@Root(name="gpx", strict=false)
public class Gpx {
    @ElementList(inline=true)
    private List<Track> track;

    // Getters
    public List<Track> getTrack() {
        return track;
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
