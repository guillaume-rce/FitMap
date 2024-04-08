package com.oniverse.fitmap.modules.tracks.api;

import com.oniverse.fitmap.modules.tracks.Track;

import java.util.List;

public class TracksResponse {
    public List<Track> data;
    public Links links;
    public Meta meta;

    public class Links {
        public String first;
        public String last;
        public String prev;
        public String next;
    }

    public class Meta {
        public int current_page;
        public int from;
        public int to;
        public int last_page;
        public int per_page;
        public int total;
    }
}
