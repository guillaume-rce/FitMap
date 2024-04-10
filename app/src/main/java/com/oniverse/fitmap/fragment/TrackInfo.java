package com.oniverse.fitmap.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            track = getArguments().getString(ARG_PARAM1);
            Track t = TrackList.getInstance().getTrack(Long.parseLong(track));

            TextView trackName = view.findViewById(R.id.track_name);
            trackName.setText(t.name);

            TextView trackDesc = view.findViewById(R.id.track_description);
            trackDesc.setText(t.activity.i18n);

            TextView trackDuration = view.findViewById(R.id.track_duration);
            Duration duration = Utils.getGpxDeltaTime(t.getGpxTrack().getGpx());
            trackDuration.setText(duration.toString());

            TextView trackLength = view.findViewById(R.id.track_length);
            trackLength.setText(String.valueOf(t.length));

            List<PointValue> points = Utils.getGpxElevationPoints(t.getGpxTrack().getGpx());

            Line line = new Line(points).setColor(Color.BLUE);
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
}