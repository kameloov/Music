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
import android.widget.TextView;

import com.orientalmusic.music.DialogManager;
import com.orientalmusic.music.R;
import com.orientalmusic.music.DataStoreManager;
import com.orientalmusic.music.activity.MainActivity;
import com.orientalmusic.music.entity.User;
import com.orientalmusic.music.service.Service;

public class RegisterFragment extends Fragment {

    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtPhone;
    private Button btnRegister;
    private TextView txtLogin;
    private ProgressBar progressBar;
    private MainActivity activity;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance(MainActivity activity) {
        RegisterFragment fragment = new RegisterFragment();
        fragment.activity = activity;
        return fragment;
    }

    private void attachViews() {
        edtName = (EditText) getActivity().findViewById(R.id.edtName);
        edtEmail = (EditText) getActivity().findViewById(R.id.edtEmail);
        edtPassword = (EditText) getActivity().findViewById(R.id.edtPassword);
        edtPhone = (EditText) getActivity().findViewById(R.id.edtPhone);
        btnRegister = (Button) getActivity().findViewById(R.id.btnRegister);
        progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
        txtLogin = (TextView) getView().findViewById(R.id.txtLogin);
        txtLogin.setTypeface(activity.getTypeface());
    }

    private void attachEvents() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String email = edtEmail.getText().toString();
                String phone = edtPhone.getText().toString();
                String password = edtPassword.getText().toString();
                if (name.length() == 0 || email.length() == 0 || password.length() == 0) {
                    DialogManager.infoDialog(getContext(), "please fill required fields");
                } else {
                    RegisterTask task = new RegisterTask(name, email, phone, password, progressBar);
                    task.execute();
                }
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.showLogin();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        attachViews();
        attachEvents();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    class RegisterTask extends AsyncTask<Void, Void, Service.Response> {
        private String name;
        private String email;
        private String phone;
        private String password;
        private ProgressBar progressBar;

        public RegisterTask(String name, String email, String phone, String password, ProgressBar progressBar) {
            this.name = name;
            this.email = email;
            this.phone = phone;
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
                    DialogManager.infoDialog(getActivity(), getString(R.string.regesterd_successfuly));
                    if (result.getResponseObject() != null) {
                        int id = result.getResponseObject().getAsInt();
                        User user = new User();
                        user.setBalance(0);
                        user.setEmail(email);
                        user.setPassword(password);
                        user.setPhone(phone);
                        user.setId(id);
                        user.setBalance(0);
                        user.setOn(true);
                        DataStoreManager.setUser(getActivity(), user);
                        activity.showMain();
                    }
                } else {
                    DialogManager.infoDialog(getActivity(), getString(R.string.registeration_failed));
                }
            }
        }

        @Override
        protected Service.Response doInBackground(Void... voids) {
            Service service = new Service();
            Service.Response object = null;
            String token = DataStoreManager.getToken(getContext());
            try {
                object = service.register(email, password, name, phone, token);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return object;
        }
    }


}
