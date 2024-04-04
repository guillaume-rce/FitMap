package com.oniverse.fitmap.modules.tracks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TrackList {
    private static TrackList instance;

    private final ArrayList<Track> tracks;
    private int total;

    public TrackList() {
        tracks = new ArrayList<>();
        total = 0;
    }

    public int getTotal() {
        return total;
    }

    public Track getTrack(long id) {
        Stream<Track> trackStream = tracks.stream().filter(track -> track.id == id);
        return trackStream.findFirst().orElse(null);
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public void addTrack(Track track) {
        tracks.add(track);
        total++;
    }

    public void addTracks(List<Track> tracks) {
        this.tracks.addAll(tracks);
        total += tracks.size();
    }

    public static TrackList getInstance() {
        if (instance == null) {
            instance = new TrackList();
        }
        return instance;
    }
}
