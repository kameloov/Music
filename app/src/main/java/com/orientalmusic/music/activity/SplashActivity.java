package com.orientalmusic.music.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.orientalmusic.music.R;
import com.orientalmusic.music.DataStoreManager;
import com.orientalmusic.music.entity.User;
import com.orientalmusic.music.task.RefreshContactTask;
import com.orientalmusic.music.task.RefreshUserTask;
import com.orientalmusic.music.task.RefreshVideosTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        User user = DataStoreManager.getUser(this);
        RefreshContactTask contactTask = new RefreshContactTask(this);
        contactTask.execute();
        if (user != null) {
            RefreshUserTask task = new RefreshUserTask(this, user.getId());
            task.execute();
            RefreshVideosTask videosTask = new RefreshVideosTask(this, user.getId());
            videosTask.execute();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(), android.R.anim.fade_in, android.R.anim.fade_out);
                    startActivity(intent, options.toBundle());
                } else
                    startActivity(intent);
                finish();
            }
        }, 2000);
    }

}
