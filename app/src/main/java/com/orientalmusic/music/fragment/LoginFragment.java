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
import com.orientalmusic.music.DataStoreManager;
import com.orientalmusic.music.activity.MainActivity;
import com.orientalmusic.music.service.Service;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    MainActivity activity;
    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnLogin;
    private ProgressBar progressBar;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(MainActivity activity) {
        LoginFragment fragment = new LoginFragment();
        fragment.activity = activity;
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        attachViews();
        attachEvents();
    }

    private void attachViews() {
        edtEmail = (EditText) getView().findViewById(R.id.edtEmail);
        edtPassword = (EditText) getView().findViewById(R.id.edtPassword);
        btnLogin = (Button) getView().findViewById(R.id.btnLogin);
        progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
    }

    private void attachEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                if (email.length() == 0 || password.length() == 0) {
                    DialogManager.infoDialog(getContext(), getString(R.string.fill_fields));
                    return;
                }
                LoginTask task = new LoginTask(email, password, progressBar);
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    class LoginTask extends AsyncTask<Void, Void, Service.Response> {
        private String email;
        private String password;
        private ProgressBar progressBar;

        public LoginTask(String email, String password, ProgressBar progressBar) {
            this.email = email;
            this.password = password;
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
            progressBar.setVisibility(View.GONE);
            if (result != null) {
                if (result.isSuccess()) {
                    DataStoreManager.login(getContext());
                    activity.showMain();
                } else
                    DialogManager.infoDialog(getContext(), getString(R.string.login_error));
            } else
                DialogManager.infoDialog(getContext(), getString(R.string.login_error));
        }

        @Override
        protected Service.Response doInBackground(Void... voids) {
            Service service = new Service();
            Service.Response object = null;
            try {
                object = service.login(email, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return object;
        }
    }


}
