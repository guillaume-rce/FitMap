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

/**
 * TrackActivity class
 * It will display the track and track information.
 */
public class TrackActivity extends AppCompatActivity {
    private ActivityTrackBinding binding;
    private MapRenderer mapRenderer;
    private Localisation localisationService;
    private Track track;
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

    /**
     * onCreate method
     * It will display the track and track information.
     * @param savedInstanceState Bundle The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        track = (Track) bundle.getSerializable("track");

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

    /**
     * This method will toggle the live tracking in the service.
     * @param liveTracking boolean The live tracking state
     */
    public void setLiveTracking(boolean liveTracking) {
        if (isBound && localisationService != null) {
            localisationService.setLiveTracking(liveTracking);
        }
    }

    /**
     * This method will stop the live tracking in the service.
     * And clear the polyline on the map.
     */
    public void stopLiveTracking() {
        setLiveTracking(false);
        mapRenderer.clearPolylineLive(); // Clear the polyline on the map
    }

    /**
     * This method will share the track to the conversation activity.
     */
    public void shareTrack() {
        Intent intent = new Intent(getApplicationContext(), ChatListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("track", track);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * It will go back to the ExploreActivity.
     */
    public void back() {
        stopLiveTracking();
        if (isBound) {
            unbindService(connection); // Unbind the service to avoid memory leaks
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
