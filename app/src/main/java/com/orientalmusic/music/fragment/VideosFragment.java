package com.orientalmusic.music.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orientalmusic.music.R;
import com.orientalmusic.music.activity.MainActivity;
import com.orientalmusic.music.adapter.VideoAdapter;
import com.orientalmusic.music.entity.SubCategory;
import com.orientalmusic.music.entity.Video;
import com.orientalmusic.music.service.Service;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideosFragment extends Fragment {
    private MainActivity activity;
    private SubCategory subCategory;
    private RecyclerView lstVideos;
    private LinearLayoutManager layoutManager;
    private ProgressBar progressBar;
    private TextView txtEmpty;

    public VideosFragment() {
        // Required empty public constructor
    }


    public static VideosFragment newInstance(MainActivity activity, SubCategory subCategory) {
        VideosFragment fragment = new VideosFragment();
        fragment.activity = activity;
        fragment.subCategory = subCategory;
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        attachViews();
        layoutManager = new LinearLayoutManager(getActivity());
        lstVideos.setLayoutManager(layoutManager);
        LoadVideos task = new LoadVideos(subCategory.getId(), 0, progressBar, txtEmpty);
        task.execute();
    }

    private void attachViews() {
        lstVideos = (RecyclerView) getView().findViewById(R.id.lstVideos);
        txtEmpty = (TextView) getView().findViewById(R.id.txtEmpty);
        progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
        lstVideos.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (layoutManager != null && lstVideos.getAdapter() != null) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (layoutManager.findLastVisibleItemPosition() > lstVideos.getAdapter().getItemCount() - 4) {
                            VideoAdapter adapter = ((VideoAdapter) lstVideos.getAdapter());
                            if (adapter != null)
                                adapter.loadMore(subCategory.getId(), progressBar);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_videos, container, false);
    }


    class LoadVideos extends AsyncTask<Void, Void, Service.Response> {
        private ProgressBar progressBar;
        private View txtEmpty;
        private int catId;
        private int minId;

        public LoadVideos(int catId, int minId, ProgressBar progressBar, View txtEmpty) {
            this.txtEmpty = txtEmpty;
            this.progressBar = progressBar;
            this.minId = minId;
            this.catId = catId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txtEmpty.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Service.Response result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            if (result != null) {
                if (result.isSuccess()) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Video>>() {
                    }.getType();
                    Log.e("getVideos", result.getResponseObject().toString());
                    List<Video> videoList = gson.fromJson(result.getResponseObject(), type);
                    if (videoList != null) {
                        VideoAdapter adapter = new VideoAdapter(activity, videoList);
                        lstVideos.setAdapter(adapter);
                    } else
                        txtEmpty.setVisibility(View.VISIBLE);
                } else
                    txtEmpty.setVisibility(View.VISIBLE);

            } else
                txtEmpty.setVisibility(View.VISIBLE);
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

}
