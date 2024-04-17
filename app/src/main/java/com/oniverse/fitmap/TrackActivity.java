package com.oniverse.fitmap;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.oniverse.fitmap.databinding.ActivityTrackBinding;
import com.oniverse.fitmap.fragment.TrackInfo;
import com.oniverse.fitmap.modules.MapRenderer;
import com.oniverse.fitmap.modules.tracks.Track;
import com.oniverse.fitmap.service.Localisation;

public class TrackActivity extends AppCompatActivity {
    private ActivityTrackBinding binding;
    private MapRenderer mapRenderer;
    private Localisation localisationService;
    private boolean isBound = false;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Localisation.LocalBinder binder = (Localisation.LocalBinder) service;
            localisationService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

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
        mapRenderer.addCompassOverlay();
        assert track != null;
        mapRenderer.setGpx(track.getGpxTrack().getGpx());

        Intent localisationIntent = new Intent(this, Localisation.class);
        localisationIntent.putExtra("updateInterval", 1);
        startService(localisationIntent);
        bindService(localisationIntent, connection, BIND_AUTO_CREATE);

        LinearLayout trackInfoLayout = findViewById(R.id.track_info);
        TrackInfo trackInfo = TrackInfo.newInstance(String.valueOf(track.id));
        getSupportFragmentManager().beginTransaction().add(trackInfoLayout.getId(), trackInfo).commit();
    }

    public void setLiveTracking(boolean liveTracking) {
        if (isBound && localisationService != null) {
            localisationService.setLiveTracking(liveTracking);
        }
    }

    public void stopLiveTracking() {
        setLiveTracking(false);
        mapRenderer.clearPolylineLive();
    }

    public void back() {
        stopLiveTracking();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
        stopService(new Intent(this, Localisation.class));
        Intent intent = new Intent(getApplicationContext(), ExploreActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }
}
