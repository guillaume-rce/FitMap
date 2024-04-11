package com.oniverse.fitmap.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.oniverse.fitmap.ExploreActivity;
import com.oniverse.fitmap.R;
import com.oniverse.fitmap.modules.Utils;
import com.oniverse.fitmap.modules.tracks.Track;
import com.oniverse.fitmap.modules.tracks.TrackList;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

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
    private String track;

    public TrackInfo() {
        // Required empty public constructor
    }

    public static TrackInfo newInstance(String track) {
        TrackInfo fragment = new TrackInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, track);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton back = view.findViewById(R.id.back_button);
        back.setOnClickListener(this::back);

        if (getArguments() != null) {
            track = getArguments().getString(ARG_PARAM1);
            Track t = TrackList.getInstance().getTrack(Long.parseLong(track));

            // ---- Add the track name ----
            TextView trackName = view.findViewById(R.id.track_name);
            trackName.setText(t.name);

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
                long seconds = duration.getSeconds() % 60;
                formattedDuration = String.format("%02dH %02dMin %02dSec", hours, minutes, seconds);
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
            List<Line> lines = new ArrayList<Line>();
            lines.add(line);

            LineChartView chart = view.findViewById(R.id.chart);
            chart.setInteractive(false);

            LineChartData data = new LineChartData();
            data.setLines(lines);

            chart.setLineChartData(data);

            /*
            Viewport viewport = new Viewport(chart.getMaximumViewport());
            viewport.top = 10; // exemple de valeur maximale sur l'axe Y
            chart.setMaximumViewport(viewport);
            chart.setCurrentViewport(viewport);
             */
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_track_info, container, false);
    }

    public void back(View view) {
        // Go back to the explore activity
        Intent intent = new Intent(getContext(), ExploreActivity.class);
        startActivity(intent);
    }
}