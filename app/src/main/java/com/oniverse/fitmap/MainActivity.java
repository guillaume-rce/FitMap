package com.oniverse.fitmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.oniverse.fitmap.databinding.ActivityMainBinding;
import com.oniverse.fitmap.modules.gpxparser.Gpx;
import com.oniverse.fitmap.modules.gpxparser.TrackPoint;
import com.oniverse.fitmap.modules.tracks.Track;
import com.oniverse.fitmap.modules.tracks.TrackList;
import com.oniverse.fitmap.modules.tracks.api.ApiClient;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int total_page;
    private int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ApiClient.setMetaData(() -> {
            loadTracks();
            return null;
        });
    }

    public void loadTracks() {
        total_page = 10;
        /*
        If you want to load all tracks, you can use the following code:
        total_page = TrackList.getInstance().getApiMetaData().totalPage;
        But be careful, it will download 10000 tracks (1000 pages) so it will take a lot of time.
         */
        for (int i = 1; i <= total_page; i++) {
            ApiClient.findTracks(i, true, this, () -> {
                setProgressBarValue();
                switchToHomeActivity();
                return null;
            });
        }
    }

    public void setProgressBarValue() {
        ProgressBar progressBar = findViewById(R.id.download_bar);
        int total = TrackList.getInstance().getTotalWithGpx();
        int perPage = TrackList.getInstance().getApiMetaData().perPage;
        progress = (total * 100) / (total_page * perPage);
        progressBar.setProgress(progress);
    }

    public void switchToHomeActivity() {
        if (progress >= 100) {
            loadPoints();

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);

            System.gc();
            Runtime.getRuntime().gc();

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadPoints() {
        ArrayList<Track> tracks = TrackList.getInstance().getTracks();
        for (int i = 0; i < tracks.size(); i++) {
            Track track = tracks.get(i);
            Gpx gpx = track.getGpxTrack().getGpx();
            TrackPoint firstPoint = gpx.getTrack().get(0).getFirstSegment().getFirstTrackPoint();
            TrackPoint lastPoint = gpx.getTrack().get(0).getLastSegment().getLastTrackPoint();
            track.start_location.point = firstPoint;
            track.end_location.point = lastPoint;

            int percent = (i * 100) / tracks.size();
            ProgressBar progressBar = findViewById(R.id.load_bar);
            progressBar.setProgress(percent);
        }
    }
}
