package com.oniverse.fitmap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.oniverse.fitmap.databinding.ActivityHomeBinding;
import com.oniverse.fitmap.fragment.MainAlert;
import com.oniverse.fitmap.modules.MapRenderer;
import com.oniverse.fitmap.modules.gpxparser.TrackPoint;
import com.oniverse.fitmap.modules.tracks.Track;
import com.oniverse.fitmap.modules.tracks.TrackList;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeActivity extends AppCompatActivity {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    private ActivityHomeBinding binding;

    private MapRenderer map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_home);

        requestPermissionsIfNecessary(new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });

        MapView map_v = findViewById(R.id.map);
        if (map_v == null) {
            return;
        }
        map = new MapRenderer(map_v, TileSourceFactory.MAPNIK);
        map.setZoom(9.5F, new GeoPoint(48.8583, 2.2944));
        map.addMapScaleBarOverlay();
        map.addRotationGestureOverlay();
        map.addMyLocationOverlay();
        map.addCompassOverlay();

        for (Track track: TrackList.getInstance().getTracks()) {
            TrackPoint firstPoint = track.start_location.point;
            if (firstPoint != null)
                map.drawPoint(firstPoint, track.name);
        }

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
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    return true;
                } else if (item.getItemId() == R.id.navigation_explore) {
                    startActivity(new Intent(HomeActivity.this, ExploreActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.navigation_chat) {
                    startActivity(new Intent(HomeActivity.this, SigninActivity.class));
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
        if (map != null)
            map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        if (map != null)
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
