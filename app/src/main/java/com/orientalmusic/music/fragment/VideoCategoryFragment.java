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
import com.orientalmusic.music.adapter.VideoCategoryAdapter;
import com.orientalmusic.music.entity.Category;
import com.orientalmusic.music.service.Service;

import java.lang.reflect.Type;
import java.util.List;


public class VideoCategoryFragment extends Fragment {
    private RecyclerView lstCategory;
    private LinearLayoutManager layoutManager;
    private MainActivity activity;
    private ProgressBar progressBar;
    private TextView txtEmpty;

    public VideoCategoryFragment() {
        // Required empty public constructor
    }

    public static VideoCategoryFragment newInstance(MainActivity activity) {
        VideoCategoryFragment fragment = new VideoCategoryFragment();
        fragment.activity = activity;
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        attachViews();
        layoutManager = new LinearLayoutManager(getActivity());
        lstCategory.setLayoutManager(layoutManager);
        LoadCategoriesTask task = new LoadCategoriesTask(progressBar, txtEmpty);
        task.execute();
    }

    private void attachViews() {
        lstCategory = (RecyclerView) getView().findViewById(R.id.lstCategory);
        txtEmpty = (TextView) getView().findViewById(R.id.txtEmpty);
        progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_category, container, false);
    }

    class LoadCategoriesTask extends AsyncTask<Void, Void, Service.Response> {
        private ProgressBar progressBar;
        private View txtEmpty;

        public LoadCategoriesTask(ProgressBar progressBar, View txtEmpty) {
            this.txtEmpty = txtEmpty;
            this.progressBar = progressBar;
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
                    Type type = new TypeToken<List<Category>>() {
                    }.getType();
                    Log.e("getCategory", result.getResponseObject().toString());
                    List<Category> categoryList = gson.fromJson(result.getResponseObject(), type);
                    if (categoryList != null) {
                        VideoCategoryAdapter adapter = new VideoCategoryAdapter(activity, categoryList);
                        lstCategory.setAdapter(adapter);
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
                object = service.getCategories();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return object;
        }
    }
}
