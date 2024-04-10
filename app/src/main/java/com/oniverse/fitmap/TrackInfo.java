package com.oniverse.fitmap;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oniverse.fitmap.modules.tracks.Track;
import com.oniverse.fitmap.modules.tracks.TrackList;

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
            Track t = TrackList.getInstance().getTrack(track);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_track_info, container, false);
    }
}