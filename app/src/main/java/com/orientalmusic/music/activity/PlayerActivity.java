package com.orientalmusic.music.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import com.orientalmusic.music.R;

public class PlayerActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        videoView = (VideoView) findViewById(R.id.videoView);
        String url = getIntent().getStringExtra("url");
        mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        if (url.length() > 0) {
            videoView.setVideoPath(url);
            videoView.start();
        }
    }
}
