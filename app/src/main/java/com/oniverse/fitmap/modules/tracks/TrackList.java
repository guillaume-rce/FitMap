package com.oniverse.fitmap.modules.tracks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TrackList {
    private static TrackList instance;

    private final ArrayList<Track> tracks;
    private int total;

    private ApiMetaData apiMetaData;

    public TrackList() {
        tracks = new ArrayList<>();
        total = 0;
    }

    public int getTotal() {
        return total;
    }

    public int getTotalWithGpx() {
        return (int) tracks.stream().filter(track -> track.getGpxTrack() != null).count();
    }

    public Track getTrack(long id) {
        Stream<Track> trackStream = tracks.stream().filter(track -> track.id == id);
        return trackStream.findFirst().orElse(null);
    }

    public Track getTrack(String name) {
        Stream<Track> trackStream = tracks.stream().filter(track -> track.name.equals(name));
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

    public void setApiMetaData(ApiMetaData apiMetaData) {
        this.apiMetaData = apiMetaData;
    }

    public ApiMetaData getApiMetaData() {
        return apiMetaData;
    }

    public static class ApiMetaData {
        public int totalPage;
        public int totalTracks;
        public int perPage;

        public ApiMetaData(int totalPage, int totalTracks, int perPage) {
            this.totalPage = totalPage;
            this.totalTracks = totalTracks;
            this.perPage = perPage;
        }
    }
}
