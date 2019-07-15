package com.orientalmusic.music.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.orientalmusic.music.R;
import com.orientalmusic.music.entity.More;

public class MoreDetailsFragment extends Fragment {

    private More more;
    private WebView webView;

    public MoreDetailsFragment() {
        // Required empty public constructor
    }

    public static MoreDetailsFragment newInstance(More more) {
        MoreDetailsFragment fragment = new MoreDetailsFragment();
        fragment.more = more;
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        attachViews();
        viewData();
    }

    private void attachViews() {
        webView = getView().findViewById(R.id.webView);
    }

    private void viewData() {
        if (more != null) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDefaultTextEncodingName("UTF_8");
            webView.setWebViewClient(new Callback());
            webView.setWebChromeClient(new WebChromeClient() {

            });
            webView.loadUrl(more.getUrl());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_details, container, false);
    }

    class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }

}
