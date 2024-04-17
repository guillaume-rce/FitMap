package com.oniverse.fitmap.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.oniverse.fitmap.R;
import com.oniverse.fitmap.modules.MapRenderer;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

/**
 * Service that listens for location updates and updates the map accordingly.

 */
public class Localisation extends Service {
    private final IBinder binder = new LocalBinder();
    private LocationManager locationManager;
    private LocationListener locationListener;
    private MapRenderer mapRenderer;
    private Marker currentMarker;
    private boolean liveTracking = false;
    private int updateIntervalInSeconds = 10; // Default to 10 seconds, can be changed via Intent

    /**
     * Class used for the client Binder.  Only public methods available to clients
     * are those in the Localisation class.
     */
    public class LocalBinder extends Binder {
        public Localisation getService() {
            // Return this instance of Localisation so clients can call public methods
            return Localisation.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * Called when the service is started. Reads the update interval and live tracking state from the Intent.
     *
     * @param intent The Intent supplied to startService(Intent), as given. This may be null if the service is being restarted after its process has gone away.
     * @param flags Additional data about this start request.
     * @param startId A unique integer representing this specific request to start.
     * @return The return value indicates what semantics the system should use for the service's current started state. It may be one of the constants associated with the START_CONTINUATION_MASK bits.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateIntervalInSeconds = intent.getIntExtra("updateInterval", 10);
        liveTracking = intent.getBooleanExtra("LIVE_TRACKING", liveTracking); // Read liveTracking state from Intent
        mapRenderer = MapRenderer.getInstance();

        return START_STICKY;
    }

    /**
     * Called when the service is created. Sets up the location listener and requests location updates.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // It will be called when the location is changed and will update the marker on the map.
                GeoPoint point = new GeoPoint(location.getLatitude(), location.getLongitude());

                if (currentMarker == null) {
                    Drawable icon = getResources().getDrawable(R.drawable.icon_people_marker);
                    currentMarker = mapRenderer.addMarker(point, "Votre position", icon, 0.5f, 1.0f);
                } else {
                    mapRenderer.updateMarkerPosition(currentMarker, point);
                }

                // If live tracking is enabled, add the new point to the polyline
                if (liveTracking) {
                    mapRenderer.addPolylineLive(point);
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

    /**
     * Set the live tracking state.
     *
     * @param liveTracking The new live tracking state.
     */
    public void setLiveTracking(boolean liveTracking) {
        this.liveTracking = liveTracking;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}
