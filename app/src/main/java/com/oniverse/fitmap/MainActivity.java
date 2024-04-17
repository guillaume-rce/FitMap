package com.oniverse.fitmap;

import androidx.appcompat.app.AlertDialog;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private int total_page;
    private int trackProgress = 0;
    private int totalProgress = 0;

    private boolean tracksLoaded = false;
    private boolean gpxDownloaded = false;
    private boolean moreInfoLoaded = false;
    private boolean switchToHomeActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.oniverse.fitmap.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ApiClient.setMetaData(() -> {
            loadTracks();
            return null;
        });
    }

    public void updateProgress() {
        ProgressBar progressBar = findViewById(R.id.progress_bar);

        int trackPerPage = TrackList.getInstance().getApiMetaData().perPage;
        /*
        Replace by:
        int perPage = TrackList.getInstance().getApiMetaData().totalTracks;
        If you want to load all tracks.
         */
        trackProgress = (TrackList.getInstance().getTotal() * 100) / (total_page * trackPerPage);

        int gpxProgress = 0;
        int moreInfoProgress = 0;

        if (trackProgress >= 100) {
            int trackNeeded = TrackList.getInstance().getTotal();
            gpxProgress = (TrackList.getInstance().getTotalWithGpx() * 100) / trackNeeded;
            moreInfoProgress = (TrackList.getInstance().getTotalWithDifficulty() * 100) / trackNeeded;
        }

        totalProgress = ((trackProgress + gpxProgress + moreInfoProgress) * 100) / 300;
        progressBar.setProgress(totalProgress);
    }

    public void loadTracks() {
        if (!tracksLoaded) {
            tracksLoaded = true;
            total_page = 2;
            /*
            If you want to load all tracks, you can use the following code:
            total_page = TrackList.getInstance().getApiMetaData().totalPage;
            But be careful, it will download 10000 tracks (1000 pages) so it will take a lot of time.
             */
            for (int i = 1; i <= total_page; i++) {
                ApiClient.findTracks(i, () -> {
                    updateProgress();
                    downloadGpx();
                    loadMoreDetails();
                    return null;
                });
            }
        }
    }

    public void downloadGpx() {
        if (trackProgress >= 100 && !gpxDownloaded) {
            gpxDownloaded = true;

            ArrayList<Track> tracks = TrackList.getInstance().getTracks();
            for (Track track : tracks) {
                if (track == null)
                    continue;
                try {
                    ApiClient.downloadGpxFile(track, getApplicationContext(), true, () -> {
                        updateProgress();
                        switchToHomeActivity();
                        return null;
                    });
                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setTitle("Error...");
                    builder.setMessage("An error occurred while loading the track details.\nPlease apologize us and try again later.");
                    builder.setCancelable(false);
                }
            }
        }

    }

    public void loadMoreDetails() {
        if (trackProgress >= 100 && !moreInfoLoaded) {
            moreInfoLoaded = true;

            ArrayList<Track> tracks = TrackList.getInstance().getTracks();
            for (Track track : tracks) {
                if (track == null)
                    continue;
                ApiClient.findMoreInfo(track, () -> {
                    updateProgress();
                    switchToHomeActivity();
                    return null;
                });
            }
        }
    }

    public void switchToHomeActivity() {
        if (totalProgress >= 100 && !switchToHomeActivity) {
            switchToHomeActivity = true;

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
            if (gpx == null)
                continue;
            List<com.oniverse.fitmap.modules.gpxparser.Track> gpxTracks = gpx.getTrack();
            TrackPoint firstPoint = gpxTracks.get(0).getFirstSegment().getFirstTrackPoint();
            TrackPoint lastPoint = gpxTracks.get(0).getLastSegment().getLastTrackPoint();
            track.start_location.point = firstPoint;
            track.end_location.point = lastPoint;
        }
    }
}
