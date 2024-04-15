package com.oniverse.fitmap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oniverse.fitmap.databinding.ActivityExploreBinding;
import com.oniverse.fitmap.databinding.ActivityMainBinding;
import com.oniverse.fitmap.modules.MapRenderer;
import com.oniverse.fitmap.modules.gpxparser.TrackPoint;
import com.oniverse.fitmap.modules.tracks.TrackList;
import com.oniverse.fitmap.modules.tracks.Track;

import java.util.ArrayList;
import java.util.Arrays;

public class ExploreActivity extends AppCompatActivity {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private ActivityExploreBinding binding_explore;
    private DatabaseReference mDatabase;
    private MapRenderer map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_explore = ActivityExploreBinding.inflate(getLayoutInflater());
        setContentView(binding_explore.getRoot());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_explore);

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });

        MapView map_v = findViewById(R.id.map);
        map = new MapRenderer(map_v, TileSourceFactory.MAPNIK);
        map.setZoom(9.5F, new GeoPoint(48.8583, 2.2944));
        map.addMapScaleBarOverlay();
        map.addRotationGestureOverlay();
        map.addMyLocationOverlay();
        map.addCompassOverlay();

        for (Track track: TrackList.getInstance().getTracks()) {
            TrackPoint start = track.start_location.point;
            if (start != null) {
                map.drawPointWithGpxLoader(start, track.name, this);
            }
        }

        // ---------------- Add navbar ----------------
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        navView.setSelectedItemId(R.id.navigation_explore);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    startActivity(new Intent(ExploreActivity.this, HomeActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.navigation_explore) {
                    return true;
                } else if (item.getItemId() == R.id.navigation_chat) {
                    if (currentUser != null) {
                        // rediriger vers la page
                        startActivity(new Intent(ExploreActivity.this, ChatActivity.class));
                    } else {
                        // Rediriger vers la page de connexion
                        startActivity(new Intent(ExploreActivity.this, SigninActivity.class));
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        ArrayList<String> permissionsToRequest = new ArrayList<>(
                Arrays.asList(permissions).subList(0, grantResults.length));

        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }

        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}
