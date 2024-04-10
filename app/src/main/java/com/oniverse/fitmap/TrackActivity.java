package com.oniverse.fitmap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.oniverse.fitmap.databinding.ActivityTrackBinding;
import com.oniverse.fitmap.modules.MapRenderer;
import com.oniverse.fitmap.modules.tracks.Track;
import com.oniverse.fitmap.modules.tracks.TrackList;

public class TrackActivity extends AppCompatActivity {
    private ActivityTrackBinding binding;
    private MapRenderer mapRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        Track track = TrackList.getInstance().getTrack(bundle.getLong("track_id"));

        mapRenderer = new MapRenderer(binding.map);
        mapRenderer.setZoom(15);
        mapRenderer.addMapScaleBarOverlay();
        mapRenderer.addRotationGestureOverlay();
        mapRenderer.addMyLocationOverlay();
        mapRenderer.addCompassOverlay();
        mapRenderer.setGpx(track.getGpxTrack().getGpx());

        LinearLayout trackInfoLayout = findViewById(R.id.track_info);
        TrackInfo trackInfo = TrackInfo.newInstance(String.valueOf(track.id));
        getSupportFragmentManager().beginTransaction().add(trackInfoLayout.getId(), trackInfo).commit();
    }
}
