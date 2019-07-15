package com.orientalmusic.music.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.orientalmusic.music.R;
import com.orientalmusic.music.activity.MainActivity;
import com.orientalmusic.music.interfaces.IAdCaller;

/**
 * Created by kameloov on 12/14/2017.
 */

public class ToneDialog extends Dialog implements View.OnClickListener, IAdCaller {
    private MainActivity mainActivity;
    private Button btnPlay;
    private Button btnClose;

    public ToneDialog(@NonNull MainActivity mainActivity) {
        super(mainActivity);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.tone_dialog);
        this.mainActivity = mainActivity;
        attachViews();
        attachEvents();
    }

    private void attachViews() {
        btnClose = findViewById(R.id.btnClose);
        btnPlay = findViewById(R.id.btnPlay);
    }

    private void attachEvents() {
        btnPlay.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnClose:
                dismiss();
                break;
            case R.id.btnPlay:
                if (mainActivity.rewardedVideoAd.isLoaded()) {
                    mainActivity.showAd(this);
                } else
                    Log.e("playVideo","video not loaded yet");
                break;
        }
    }

    @Override
    public void onRewarded() {

    }
}
