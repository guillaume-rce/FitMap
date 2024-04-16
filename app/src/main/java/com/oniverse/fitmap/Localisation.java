package com.oniverse.fitmap;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.oniverse.fitmap.modules.MapRenderer;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

public class Localisation extends Service {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private MapRenderer mapRenderer;
    private Marker currentMarker;
    private int updateIntervalInSeconds = 10; // Default to 10 seconds, can be passed via Intent

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateIntervalInSeconds = intent.getIntExtra("updateInterval", 10);
        mapRenderer = MapRenderer.getInstance();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                GeoPoint point = new GeoPoint(location.getLatitude(), location.getLongitude());
                if (currentMarker == null) {
                    Drawable icon = getResources().getDrawable(R.drawable.icon_people_marker);
                    currentMarker = mapRenderer.addMarker(point, "Votre position", icon, 0.5f, 1.0f);
                } else {
                    mapRenderer.updateMarkerPosition(currentMarker, point);
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };

        try {
            // Request updates at the specified interval
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * updateIntervalInSeconds, 0, locationListener, Looper.getMainLooper());
        } catch (SecurityException e) {
            Log.e("LocalisationService", "Failed to request location updates", e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}
