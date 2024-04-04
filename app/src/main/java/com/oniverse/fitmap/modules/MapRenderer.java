package com.oniverse.fitmap.modules;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import com.oniverse.fitmap.R;
import com.oniverse.fitmap.modules.gpxparser.TrackPoint;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay2;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class MapRenderer {
    private final MapView map;
    private final Context context;

    private MyLocationNewOverlay mLocationOverlay;
    private CompassOverlay mCompassOverlay;
    private RotationGestureOverlay mRotationGestureOverlay;
    private ScaleBarOverlay mScaleBarOverlay;

    private ArrayList<OverlayItem> items = new ArrayList<>();
    private ArrayList<Marker> markers = new ArrayList<>();

    public MapRenderer(MapView map) {
        this(map, TileSourceFactory.MAPNIK);
    }

    public MapRenderer(MapView map, ITileSource tileSource) {
        this.map = map;
        this.map.setTileSource(tileSource);

        this.map.setBuiltInZoomControls(true);
        this.map.setMultiTouchControls(true);

        this.context = map.getContext();
    }

    public void setZoom(float zoom) {
        IMapController mapController = map.getController();
        mapController.setZoom(zoom);
    }

    public void setCenter(GeoPoint center) {
        IMapController mapController = map.getController();
        mapController.setCenter(center);
    }

    public void setZoom(float zoom, GeoPoint center) {
        IMapController mapController = map.getController();
        mapController.setZoom(zoom);
        mapController.setCenter(center);
    }

    public void addMyLocationOverlay() {
        // TODO: Use another method because this one seems to don't work
        if (this.mLocationOverlay == null) {
            mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context), map);
            mLocationOverlay.enableMyLocation();
        }
        if (!map.getOverlays().contains(mLocationOverlay)) {
            map.getOverlays().add(mLocationOverlay);
            map.invalidate();
        }
    }

    public void removeMyLocationOverlay() {
        map.getOverlays().remove(mLocationOverlay);
    }

    public void addCompassOverlay() {
        mCompassOverlay = new CompassOverlay(context, new InternalCompassOrientationProvider(context), map);
        mCompassOverlay.enableCompass();
        map.getOverlays().add(mCompassOverlay);
    }

    public void removeCompassOverlay() {
        map.getOverlays().remove(mCompassOverlay);
    }

    public void addGridlinesOverlay() {
        LatLonGridlineOverlay2 overlay = new LatLonGridlineOverlay2();
        map.getOverlays().add(overlay);
    }

    public void addRotationGestureOverlay() {
        mRotationGestureOverlay = new RotationGestureOverlay(context, map);
        mRotationGestureOverlay.setEnabled(true);
        map.setMultiTouchControls(true);
        map.getOverlays().add(mRotationGestureOverlay);
    }

    public void removeRotationGestureOverlay() {
        map.getOverlays().remove(mRotationGestureOverlay);
    }

    public void addMapScaleBarOverlay() {
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mScaleBarOverlay = new ScaleBarOverlay(map);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        map.getOverlays().add(mScaleBarOverlay);
    }

    public void removeMapScaleBarOverlay() {
        map.getOverlays().remove(mScaleBarOverlay);
    }

    public void addAllOverlays() {
        addMyLocationOverlay();
        addCompassOverlay();
        addGridlinesOverlay();
        addRotationGestureOverlay();
        addMapScaleBarOverlay();
    }

    public void removeAllOverlays() {
        removeMyLocationOverlay();
        removeCompassOverlay();
        removeRotationGestureOverlay();
        removeMapScaleBarOverlay();
    }

    public ItemizedOverlayWithFocus<OverlayItem> addIconWithCallback(GeoPoint point, String title, String snippet,
                                    Callable<Void> singleTapCallback, Callable<Void> longPressCallback) {
        items.add(new OverlayItem(title, snippet, point));

        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        // Here we can perform some action on single tap
                        try {
                            singleTapCallback.call();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        // Here we can perform some action on long press
                        try {
                            longPressCallback.call();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                }, context);
        mOverlay.setFocusItemsOnTap(true);

        map.getOverlays().add(mOverlay);
        return mOverlay;
    }

    public void removeIcon(ItemizedOverlayWithFocus<OverlayItem> item) {
        map.getOverlays().remove(item);
        OverlayItem overlayItem = item.getItem(0);
        items.remove(overlayItem);
    }

    public Marker addMarker(GeoPoint point, String title, Drawable icon) {
        return addMarker(point, title, icon, Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
    }

    public Marker addMarker(GeoPoint point, String title, Drawable icon,
            float anchorU, float anchorV) {
        Marker marker = new Marker(map);
        markers.add(marker);

        marker.setPosition(point);
        marker.setAnchor(anchorU, anchorV);
        map.getOverlays().add(marker);

        marker.setIcon(icon);
        marker.setTitle(title);
        return marker;
    }

    public void removeMarker(Marker marker) {
        map.getOverlays().remove(marker);
        markers.remove(marker);
    }

    // TODO: Add other makers and lines ( https://github.com/osmdroid/osmdroid/wiki/Markers,-Lines-and-Polygons-(Java)#marker-using-a-text-label-instead-of-an-icon )

    public void onResume() {
        map.onResume();
    }

    public void onPause() {
        map.onPause();
    }

    public void setTileSource(ITileSource tileSource) {
        map.setTileSource(tileSource);
    }

    public void drawPoint(TrackPoint trackPoint, String title) {
        GeoPoint startPoint = new GeoPoint(trackPoint.getLatitude(), trackPoint.getLongitude());
        this.addMarker(startPoint,  title, context.getDrawable(R.drawable.icon_location),
                Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
    }


}
