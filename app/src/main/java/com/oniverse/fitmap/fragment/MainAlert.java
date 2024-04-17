package com.oniverse.fitmap.fragment;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oniverse.fitmap.R;
/**
 * MainAlert.java
 * Purpose: Fragment for alert dialog
 * Description: This class is used to create the fragment for the alert dialog.
 * It is used to display the alert dialog with the icon, title, and message.
 */
public class MainAlert extends Fragment {

    private static final String ARG_PARAM1 = "icon";
    private static final String ARG_PARAM2 = "title";
    private static final String ARG_PARAM3 = "message";
/*
        * Required empty public constructor
        * This method is used to create the fragment for the alert dialog.
        * @return void
 */
    public MainAlert() {
        // Required empty public constructor
    }
/*
        * newInstance
        * This method is used to create a new instance of the fragment for the alert dialog.
        * @param iconResId The icon resource ID
        * @param title The title of the alert dialog
        * @param message The message of the alert dialog
        * @return MainAlert
 */
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_alert, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            int iconResId = getArguments().getInt(ARG_PARAM1);
            ImageView icon = view.findViewById(R.id.icon);
            icon.setImageDrawable(ContextCompat.getDrawable(requireContext(), iconResId));

            String mTitle = getArguments().getString(ARG_PARAM2);
            TextView title = view.findViewById(R.id.title);
            title.setText(mTitle);

            String mMessage = getArguments().getString(ARG_PARAM3);
            TextView message = view.findViewById(R.id.message);
            message.setText(mMessage);
        }
    }
}
