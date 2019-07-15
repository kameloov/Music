package com.orientalmusic.music.adapter;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orientalmusic.music.R;
import com.orientalmusic.music.activity.MainActivity;
import com.orientalmusic.music.entity.Video;
import com.orientalmusic.music.service.Service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kameloov on 7/23/2017.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {
    private static String url = "http://myoriental-music.com/service/video_files/";
    MainActivity contentActivity;
    List<Video> videoList;
    List<Video> temp;
    private boolean loading = false;

    public VideoAdapter(MainActivity activity, List<Video> videoList) {
        this.videoList = videoList;
        this.contentActivity = activity;
        if (videoList.size() > 0) {
            loadMore(videoList.get(0).getCategory(), null);
        }
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(contentActivity).inflate(R.layout.list_item_video, parent, false);
        return new VideoHolder(v, contentActivity);
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        Video video = videoList.get(position);
        if (video != null)
            holder.bindCategory(video);
    }

    public void loadMore(int catId, ProgressBar progressBar) {
        if (loading)
            return;
        LoadVideos loadVideos = new LoadVideos(catId, findMinId(), progressBar);
        loadVideos.execute();
    }

    private int findMinId() {
        int min = 0;
        if (videoList != null && videoList.size() > 0) {
            min = videoList.get(0).getId();
            for (Video video : videoList) {
                if (video.getId() < min)
                    min = video.getId();
            }
        }
        return min;
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgVIdeo;
        private TextView txtTitle;
        private TextView txtPrice;
        private ProgressBar progressBar;
        private MainActivity contentActivity;
        private Video video;

        public VideoHolder(View itemView, MainActivity contentActivity) {
            super(itemView);
            this.contentActivity = contentActivity;
            imgVIdeo = (ImageView) itemView.findViewById(R.id.imgVideo);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            itemView.setOnClickListener(this);
        }

        public void bindCategory(Video video) {
            this.video = video;
            txtTitle.setTypeface(contentActivity.getTypeface());
            txtPrice.setTypeface(contentActivity.getTypeface());
            txtTitle.setText(video.getTitle());
            txtPrice.setText(video.getPrice() > 0 ? String.valueOf(video.getPrice()) : "FREE");
            txtPrice.setTextColor(contentActivity.getResources().getColor(video.getPrice() > 0 ? R.color.colorAccent2 : R.color.background));
            // LoadThumbTask task = new LoadThumbTask(VideoHolder.this);
            // task.execute();
            // Glide.with(contentActivity).load(url +video.getIcon()).into(imgVIdeo);
        }

        @Override
        public void onClick(View view) {
            contentActivity.showVideo(video);
        }
    }

    class LoadVideos extends AsyncTask<Void, Void, Service.Response> {
        private ProgressBar progressBar;
        private int catId;
        private int minId;

        public LoadVideos(int catId, int minId, ProgressBar progressBar) {
            this.progressBar = progressBar;
            this.minId = minId;
            this.catId = catId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressBar != null)
                progressBar.setVisibility(View.VISIBLE);
            if (temp != null) {
                videoList.addAll(temp);
                notifyDataSetChanged();
            }
            temp = null;
            loading = true;
        }

        @Override
        protected void onPostExecute(Service.Response result) {
            super.onPostExecute(result);
            loading = false;
            if (progressBar != null)
                progressBar.setVisibility(View.GONE);
            if (result != null) {
                if (result.isSuccess()) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Video>>() {
                    }.getType();
                    Log.e("getVideos", result.getResponseObject().toString());
                    List<Video> videoList = gson.fromJson(result.getResponseObject(), type);
                    if (videoList != null) {
                        temp = videoList;
                    }
                }
            }
        }

        @Override
        protected Service.Response doInBackground(Void... voids) {
            Service service = new Service();
            Service.Response object = null;
            try {
                object = service.getVideos(minId, catId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return object;
        }
    }

    private class LoadThumbTask extends AsyncTask<Void, Void, Bitmap> {
        private VideoHolder videoHolder;

        public LoadThumbTask(VideoHolder videoHolder) {
            this.videoHolder = videoHolder;
        }

        @Override
        protected void onPreExecute() {
            videoHolder.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = null;
            MediaMetadataRetriever mediaMetadataRetriever = null;
            try {
                mediaMetadataRetriever = new MediaMetadataRetriever();
                if (Build.VERSION.SDK_INT >= 14)
                    // no headers included
                    mediaMetadataRetriever.setDataSource(url + videoHolder.video.getUrl(), new HashMap<String, String>());
                else
                    mediaMetadataRetriever.setDataSource(url + videoHolder.video.getUrl());
                //   mediaMetadataRetriever.setDataSource(videoPath);
                bitmap = mediaMetadataRetriever.getFrameAtTime();
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (mediaMetadataRetriever != null)
                    mediaMetadataRetriever.release();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            videoHolder.progressBar.setVisibility(View.INVISIBLE);
            if (bitmap != null && videoHolder != null)
                videoHolder.imgVIdeo.setImageBitmap(bitmap);
        }
    }
}
