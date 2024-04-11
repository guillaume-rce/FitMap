package com.oniverse.fitmap.modules.tracks.api;

public class TrackResponse {
    public data data;

    public static class data {
        public long id;
        public String name;
        public String difficulty;
    }
}
