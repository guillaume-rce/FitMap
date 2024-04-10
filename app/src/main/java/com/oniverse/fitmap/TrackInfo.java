package com.oniverse.fitmap;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oniverse.fitmap.modules.Utils;
import com.oniverse.fitmap.modules.tracks.Track;
import com.oniverse.fitmap.modules.tracks.TrackList;

import org.w3c.dom.Text;

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

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "track";

    // TODO: Rename and change types of parameters
    private String track;

    public TrackInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param track Parameter 1.
     * @return A new instance of fragment TrackInfo.
     */
    public static TrackInfo newInstance(String track) {
        TrackInfo fragment = new TrackInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, track);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            track = getArguments().getString(ARG_PARAM1);
            Track t = TrackList.getInstance().getTrack(Long.parseLong(track));

            TextView trackName = getView().findViewById(R.id.track_name);
            trackName.setText(t.name);

            TextView trackDesc = getView().findViewById(R.id.track_description);
            trackDesc.setText(t.activity.i18n);

            TextView trackDuration = getView().findViewById(R.id.track_duration);
            Duration duration = Utils.getGpxDeltaTime(t.getGpxTrack().getGpx());
            trackDuration.setText(duration.toString());

            TextView trackLength = getView().findViewById(R.id.track_length);
            trackLength.setText(String.valueOf(t.length));

            List<PointValue> points = Utils.getGpxElevationPoints(t.getGpxTrack().getGpx());

            Line line = new Line(points).setColor(Color.BLUE).setCubic(true);
            List<Line> lines = new ArrayList<Line>();
            lines.add(line);

            LineChartView chart = getView().findViewById(R.id.chart);

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