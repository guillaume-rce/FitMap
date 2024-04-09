package com.oniverse.fitmap;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.oniverse.fitmap.databinding.ActivityTrackBinding;
import com.oniverse.fitmap.modules.MapRenderer;

public class TrackActivity extends AppCompatActivity {
    private ActivityTrackBinding binding;
    private MapRenderer mapRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mapRenderer = new MapRenderer(binding.map);
        mapRenderer.setZoom(15);
        mapRenderer.addMapScaleBarOverlay();
        mapRenderer.addRotationGestureOverlay();
        mapRenderer.addMyLocationOverlay();
        mapRenderer.addCompassOverlay();
    }
}
