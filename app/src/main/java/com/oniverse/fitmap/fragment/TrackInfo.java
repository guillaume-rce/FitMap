package com.oniverse.fitmap.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.oniverse.fitmap.ExploreActivity;
import com.oniverse.fitmap.R;
import com.oniverse.fitmap.TrackActivity;
import com.oniverse.fitmap.modules.Utils;
import com.oniverse.fitmap.modules.tracks.Track;
import com.oniverse.fitmap.modules.tracks.TrackList;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.model.LineChartData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrackInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackInfo extends Fragment {

    private static final String ARG_PARAM1 = "track";
    private boolean liveTracking = false;
    private ImageButton liveTrackingButton;
    private ImageButton stopLiveTrackingButton;

    public TrackInfo() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param track The track to display.
     * @return A new instance of fragment TrackInfo.
     */
    public static TrackInfo newInstance(String track) {
        TrackInfo fragment = new TrackInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, track);
        fragment.setArguments(args);
        return fragment;
    }
    /**
     * Called when the fragment is created.
     *
     * @param inflater The layout inflater.
     * @param container The container.
     * @param savedInstanceState The saved instance state.
     * @return The view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_track_info, container, false);
    }
    /**
     * Called when the view is created.
     *
     * @param view The view.
     * @param savedInstanceState The saved instance state.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initializing buttons
        liveTrackingButton = view.findViewById(R.id.livetrack_button);
        stopLiveTrackingButton = view.findViewById(R.id.stop_livetrack_button);

        ImageButton back = view.findViewById(R.id.back_button);
        back.setOnClickListener(this::back);

        liveTrackingButton.setOnClickListener(this::toggleLiveTracking);
        stopLiveTrackingButton.setOnClickListener(this::stopLiveTracking);

        if (liveTrackingButton == null || stopLiveTrackingButton == null) {
            throw new IllegalStateException("One or more buttons are not initialized!");
        }

        if (getArguments() != null) {
            String track = getArguments().getString(ARG_PARAM1);
            assert track != null;
            Track t = TrackList.getInstance().getTrack(Long.parseLong(track));

            // ---- Add the track name ----
            TextView trackName = view.findViewById(R.id.track_name);
            trackName.setText(t.name);

            // ---- Add the track difficulty ----
            TextView trackDifficulty = view.findViewById(R.id.track_difficulty);
            if (t.difficulty == null || t.difficulty.equals("null") || t.difficulty.isEmpty()) {
                System.out.println("disable");
                trackDifficulty.setEnabled(false);
                trackDifficulty.setVisibility(View.INVISIBLE);
            }
            trackDifficulty.setText(t.difficulty);
            Drawable icon = Utils.getDifficultyIcon(t.difficulty, getContext());
            trackDifficulty.setBackground(icon);

            // ---- Add the track description ----
            TextView trackDesc = view.findViewById(R.id.track_description);
            trackDesc.setText(t.activity.i18n);

            // ---- Add the track duration ----
            TextView trackDuration = view.findViewById(R.id.track_duration);
            Duration duration = Utils.getGpxDeltaTime(t.getGpxTrack().getGpx());
            String formattedDuration = "";
            if (duration != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                long hours = duration.toHours();
                long minutes = duration.toMinutes() % 60;
                formattedDuration = String.format("%02dH %02dMin", hours, minutes);
            }
            trackDuration.setText(formattedDuration);

            // ---- Add the track length ----
            TextView trackLength = view.findViewById(R.id.track_length);
            trackLength.setText(Utils.getFormattedLength(t.length));

            // ---- Add the track elevation ----
            List<PointValue> points = Utils.getGpxElevationPoints(t.getGpxTrack().getGpx());

            Line line = new Line(points).setColor(Color.BLUE);
            line.setFilled(true);
            line.setHasPoints(false);
            List<Line> lines = new ArrayList<>();
            lines.add(line);

            LineChartView chart = view.findViewById(R.id.chart);
            chart.setInteractive(false);

            LineChartData data = new LineChartData();
            data.setLines(lines);

            Axis axisX = new Axis();
            axisX.setHasLines(true);
            axisX.setHasSeparationLine(true);
            axisX.setInside(false);

            Axis axisY = new Axis();
            axisY.setHasLines(true);
            axisY.setHasSeparationLine(true);
            axisY.setInside(false);

            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);

            chart.setLineChartData(data);

            /*
            To modify the viewport of the chart, you can use the following code:
            Viewport viewport = new Viewport(chart.getMaximumViewport());
            viewport.top = 10; // exemple de valeur maximale sur l'axe Y
            chart.setMaximumViewport(viewport);
            chart.setCurrentViewport(viewport);
             */
        }
    }
    /**
     * Toggle the live tracking.
     *
     * @param view The view.
     */
    public void toggleLiveTracking(View view) {
        // Start or stop the live tracking
        liveTracking = !liveTracking;

        if (liveTracking) {
            liveTrackingButton.setImageDrawable(requireContext().getDrawable(R.drawable.ic_pause));
            stopLiveTrackingButton.setVisibility(View.VISIBLE);
        } else {
            liveTrackingButton.setImageDrawable(requireContext().getDrawable(R.drawable.ic_play));
            stopLiveTrackingButton.setVisibility(View.INVISIBLE);
        }

        TrackActivity activity = (TrackActivity) getActivity();
        if (activity != null) {
            activity.setLiveTracking(liveTracking);
        }
    }
    /**
     * Stop the live tracking.
     *
     * @param view The view.
     */
    public void stopLiveTracking(View view) {
        // Stop the live tracking
        liveTracking = false;

        liveTrackingButton.setImageDrawable(requireContext().getDrawable(R.drawable.ic_play));
        stopLiveTrackingButton.setVisibility(View.INVISIBLE);

        TrackActivity activity = (TrackActivity) getActivity();
        if (activity != null) {
            activity.stopLiveTracking();
        }
    }
    /**
     * Go back to the explore activity.
     *
     * @param view The view.
     */
    public void back(View view) {
        // Go back to the explore activity
        TrackActivity activity = (TrackActivity) getActivity();
        if (activity != null) {
            activity.back();
        }
    }
}