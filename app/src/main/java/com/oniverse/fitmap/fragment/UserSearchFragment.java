package com.oniverse.fitmap.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.oniverse.fitmap.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class UserSearchFragment extends Fragment {

    private String mParam1;
    private String mParam2;

    public static UserSearchFragment newInstance() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_search, container, false);
    }

}