package com.orientalmusic.music.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.orientalmusic.music.DialogManager;
import com.orientalmusic.music.R;
import com.orientalmusic.music.service.Service;


public class RequestSongFragment extends Fragment {
    private EditText edtTitle;
    private EditText edtText;
    private Button btnRequest;
    private ProgressBar progressBar;

    public RequestSongFragment() {
        // Required empty public constructor
    }

    public static RequestSongFragment newInstance() {
        RequestSongFragment fragment = new RequestSongFragment();
        return fragment;
    }

    private void attachViews() {
        edtText = getView().findViewById(R.id.edtText);
        edtTitle = getView().findViewById(R.id.edtTitle);
        btnRequest = getView().findViewById(R.id.btnRequest);
        progressBar = getView().findViewById(R.id.progressBar);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        attachViews();
        attachEvents();
    }

    private void attachEvents() {
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edtTitle.getText().toString();
                String text = edtText.getText().toString();
                if (title.length() == 0) {
                    DialogManager.infoDialog(getContext(), getString(R.string.empty_title));
                    return;
                }
                RequestSongTask task = new RequestSongTask(title, text, progressBar);
                task.execute();
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
        return inflater.inflate(R.layout.fragment_request_song, container, false);
    }

    class RequestSongTask extends AsyncTask<Void, Void, Service.Response> {
        private String title;
        private String text;
        private ProgressBar progressBar;

        public RequestSongTask(String title, String text, ProgressBar progressBar) {
            this.title = title;
            this.text = text;
            this.progressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Service.Response result) {
            super.onPostExecute(result);
            String message = getString(R.string.request_error);
            progressBar.setVisibility(View.GONE);
            if (result != null) {
                if (result.isSuccess()) {
                    message = getString(R.string.request_done);
                    edtText.setText("");
                    edtTitle.setText("");
                }
            }
            DialogManager.infoDialog(getContext(), message);
        }

        @Override
        protected Service.Response doInBackground(Void... voids) {
            Service service = new Service();
            Service.Response object = null;
            try {
                object = service.addRequest(title, text);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return object;
        }
    }
}
