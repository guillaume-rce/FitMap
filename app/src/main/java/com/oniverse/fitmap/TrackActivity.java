package com.oniverse.fitmap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.oniverse.fitmap.databinding.ActivityTrackBinding;
import com.oniverse.fitmap.modules.MapRenderer;
import com.oniverse.fitmap.modules.tracks.Track;

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
        Track track = (Track) bundle.getSerializable("track");

        mapRenderer = new MapRenderer(binding.map);
        mapRenderer.setZoom(15);
        mapRenderer.addMapScaleBarOverlay();
        mapRenderer.addRotationGestureOverlay();
        mapRenderer.addMyLocationOverlay();
        mapRenderer.addCompassOverlay();

        LinearLayout trackInfoLayout = findViewById(R.id.track_info);
        TrackInfo trackInfo = TrackInfo.newInstance(String.valueOf(track.id));
    }
}
