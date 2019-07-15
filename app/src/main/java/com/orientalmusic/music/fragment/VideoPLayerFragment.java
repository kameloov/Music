package com.orientalmusic.music.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orientalmusic.music.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoPLayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoPLayerFragment extends Fragment {

    public VideoPLayerFragment() {
        // Required empty public constructor
    }

    public static VideoPLayerFragment newInstance(String param1, String param2) {
        VideoPLayerFragment fragment = new VideoPLayerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_player, container, false);
    }

}
