package com.orientalmusic.music.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;
import com.orientalmusic.music.R;

import java.io.File;


public class BdfViewerFragment extends Fragment {

    private File file;
    private PDFView pdfView;

    public BdfViewerFragment() {
        // Required empty public constructor
    }

    public static BdfViewerFragment newInstance(File file) {
        BdfViewerFragment fragment = new BdfViewerFragment();
        fragment.file = file;
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pdfView = (PDFView) getView().findViewById(R.id.pdfView);
        if (file != null) {
            pdfView.fromFile(file)
                    .defaultPage(1)
                    .enableSwipe(true)
                    .load();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bdf_viewer, container, false);
    }

}
