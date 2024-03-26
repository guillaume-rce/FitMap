package com.oniverse.fitmap;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;


public class MainAlert extends Fragment {

    private static final String ARG_PARAM1 = "icon";
    private static final String ARG_PARAM2 = "title";
    private static final String ARG_PARAM3 = "message";

    private Drawable mIcon;
    private String mTitle;
    private String mMessage;

    public MainAlert() {
        // Required empty public constructor
    }

    public static MainAlert newInstance(int iconResId, String title, String message) {
        MainAlert fragment = new MainAlert();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, iconResId);
        args.putString(ARG_PARAM2, title);
        args.putString(ARG_PARAM3, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int iconResId = getArguments().getInt(ARG_PARAM1);
            mIcon = ContextCompat.getDrawable(requireContext(), iconResId);
            mTitle = getArguments().getString(ARG_PARAM2);
            mMessage = getArguments().getString(ARG_PARAM3);

            ImageView icon = requireView().findViewById(R.id.icon);
            icon.setImageDrawable(mIcon);

            TextView title = requireView().findViewById(R.id.title);
            title.setText(mTitle);

            TextView message = requireView().findViewById(R.id.message);
            message.setText(mMessage);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_alert, container, false);
    }
}