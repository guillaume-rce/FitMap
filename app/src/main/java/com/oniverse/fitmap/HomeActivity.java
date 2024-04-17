package com.oniverse.fitmap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oniverse.fitmap.databinding.ActivityHomeBinding;
import com.oniverse.fitmap.fragment.MainAlert;
import com.oniverse.fitmap.modules.MapRenderer;
import com.oniverse.fitmap.modules.gpxparser.TrackPoint;
import com.oniverse.fitmap.modules.tracks.Track;
import com.oniverse.fitmap.modules.tracks.TrackList;
import com.oniverse.fitmap.service.Localisation;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;

    private MapRenderer map;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_home);

        MapView map_v = findViewById(R.id.map);
        if (map_v == null) {
            return;
        }
        map = new MapRenderer(map_v, TileSourceFactory.MAPNIK);
        map.setZoom(9.5F, new GeoPoint(48.8583, 2.2944));
        map.addMapScaleBarOverlay();
        map.addRotationGestureOverlay();
        map.addCompassOverlay();

        for (Track track: TrackList.getInstance().getTracks()) {
            TrackPoint firstPoint = track.start_location.point;
            if (firstPoint != null)
                map.drawPoint(firstPoint, track.name);
        }

        Intent localisationIntent = new Intent(this, Localisation.class);
        startService(localisationIntent);

        // Add the alert fragment
        LinearLayout alertContainer = findViewById(R.id.main_alert);
        MainAlert alert = MainAlert.newInstance(
                R.drawable.icon_position,
                "Welcome back to FitMap!",
                "We have find a lot of trails for you to explore. Let's get started!"
        );
        getSupportFragmentManager().beginTransaction().add(alertContainer.getId(), alert).commit();
        // ---------------- Add navbar ----------------
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        navView.setSelectedItemId(R.id.navigation_home);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    return true;
                } else if (item.getItemId() == R.id.navigation_explore) {
                    // Stop the service
                    stopService(localisationIntent);
                    startActivity(new Intent(HomeActivity.this, ExploreActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.navigation_chat) {
                    // Stop the service
                    stopService(localisationIntent);
                    if (currentUser != null) {
                        // rediriger vers la page
                        startActivity(new Intent(HomeActivity.this, ChatListActivity.class));
                    } else {
                        // Rediriger vers la page de connexion
                        startActivity(new Intent(HomeActivity.this, SigninActivity.class));
                    }                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map != null)
            map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        if (map != null)
            map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }
}
