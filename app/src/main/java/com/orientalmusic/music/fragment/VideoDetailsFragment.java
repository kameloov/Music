package com.orientalmusic.music.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orientalmusic.music.C;
import com.orientalmusic.music.DialogManager;
import com.orientalmusic.music.R;
import com.orientalmusic.music.DataStoreManager;
import com.orientalmusic.music.activity.MainActivity;
import com.orientalmusic.music.activity.PlayerActivity;
import com.orientalmusic.music.dialog.ToneDialog;
import com.orientalmusic.music.entity.Order;
import com.orientalmusic.music.entity.User;
import com.orientalmusic.music.entity.Video;
import com.orientalmusic.music.service.Service;
import com.orientalmusic.music.task.RefreshUserTask;
import com.orientalmusic.music.task.RefreshVideosTask;

import java.util.ArrayList;


public class VideoDetailsFragment extends Fragment implements View.OnClickListener {

    private Video video;
    private TextView txtTitle;
    private TextView txtDetails;
    private TextView txtPrice;
    private Button btnView;
    private Button btnBuy;
    private ProgressBar progressBar;
    private ArrayList<Order> orders;
    private MainActivity activity;

    public VideoDetailsFragment() {
        // Required empty public constructor
    }


    public static VideoDetailsFragment newInstance(MainActivity activity, Video video) {
        VideoDetailsFragment fragment = new VideoDetailsFragment();
        fragment.video = video;
        fragment.activity = activity;
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        attachViews();
        attachEvents();
        viewData();
    }

    private void attachViews() {
        txtTitle = getView().findViewById(R.id.txtTitle);
        txtDetails = getView().findViewById(R.id.txtDetails);
        txtPrice = getView().findViewById(R.id.txtPrice);
        btnView = getView().findViewById(R.id.btnView);
        btnBuy = getView().findViewById(R.id.btnBuy);
        progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
    }

    private void attachEvents() {
        btnView.setOnClickListener(this);
        btnBuy.setOnClickListener(this);
    }

    private void viewData() {
        orders = DataStoreManager.getPurchasedVideos(getContext());

        boolean purchased = isPurchased(video.getId());
        btnBuy.setVisibility(video.getPrice() > 0 && !purchased ? View.VISIBLE : View.GONE);
        if (video.getPrice() > 0) {
            btnView.setVisibility(purchased ? View.VISIBLE : View.GONE);
        } else
            btnView.setVisibility(View.VISIBLE);

        txtTitle.setText(video.getTitle());
        txtDetails.setText(video.getDetails());
        txtPrice.setText(String.valueOf(video.getPrice()));
    }

    private boolean isPurchased(int videoId) {
        boolean purchased = false;
        if (orders != null) {
            for (Order order : orders) {
                if (order.getItemId() == videoId)
                    return true;
            }
        }
        return purchased;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_details, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnView:
                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                intent.putExtra("url", C.VIDEO_URL + video.getUrl());
                startActivity(intent);
                break;
            case R.id.btnBuy:
                User user = DataStoreManager.getUser(getContext());
                if (user != null) {
                    if (user.isOn()) {
                        if (user.getBalance() > video.getPrice()) {
                            BuyVideoTask task = new BuyVideoTask(user.getId(), video.getId(), progressBar);
                            task.execute();
                        } else
                            new ToneDialog(activity).show();
                    } else
                        DialogManager.infoDialog(getContext(), getString(R.string.please_login));
                } else
                    DialogManager.infoDialog(getContext(), getString(R.string.please_register));
                break;
        }
    }

    class BuyVideoTask extends AsyncTask<Void, Void, Service.Response> {
        private int userId;
        private int videoId;
        private ProgressBar progressBar;

        public BuyVideoTask(int userId, int videoId, ProgressBar progressBar) {
            this.userId = userId;
            this.videoId = videoId;
            this.progressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Service.Response response) {
            super.onPostExecute(response);
            progressBar.setVisibility(View.GONE);
            if (response != null && response.isSuccess()) {
                DialogManager.infoDialog(getContext(), getString(R.string.purchased));
                btnBuy.setVisibility(View.GONE);
                btnView.setVisibility(View.VISIBLE);
                RefreshUserTask task1 = new RefreshUserTask(getContext(), userId);
                task1.execute();
                RefreshVideosTask task = new RefreshVideosTask(getContext(), userId);
                task.execute();

            } else
                DialogManager.infoDialog(getContext(), getString(R.string.purchase_err));
        }

        @Override
        protected Service.Response doInBackground(Void... voids) {
            Service service = new Service();
            Service.Response response = null;
            try {
                response = service.buyItem(videoId, userId, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }
}
