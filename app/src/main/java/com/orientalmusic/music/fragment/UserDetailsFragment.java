package com.orientalmusic.music.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.orientalmusic.music.R;
import com.orientalmusic.music.DataStoreManager;
import com.orientalmusic.music.activity.MainActivity;
import com.orientalmusic.music.entity.User;

public class UserDetailsFragment extends Fragment {
    private MainActivity activity;
    private TextView txtName;
    private TextView txtPhone;
    private TextView txtEmail;
    private TextView txtBalance;
    private Button btnLogout;

    public UserDetailsFragment() {
        // Required empty public constructor
    }

    public static UserDetailsFragment newInstance(MainActivity activity) {
        UserDetailsFragment fragment = new UserDetailsFragment();
        fragment.activity = activity;
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        attachViews();
        attachEvents();
        viewUser();
    }

    private void viewUser() {
        User user = DataStoreManager.getUser(getContext());
        if (user != null) {
            txtBalance.setText(String.valueOf(user.getBalance()));
            txtName.setText(user.getName());
            txtPhone.setText(user.getPhone());
            txtEmail.setText(user.getEmail());
        }
    }

    private void attachViews() {
        txtBalance = (TextView) getView().findViewById(R.id.txtBalance);
        txtName = (TextView) getView().findViewById(R.id.txtName);
        txtEmail = (TextView) getView().findViewById(R.id.txtEmail);
        txtPhone = (TextView) getView().findViewById(R.id.txtPhone);
        btnLogout = (Button) getView().findViewById(R.id.btnLogout);
        txtBalance.setTypeface(activity.getTypeface());
        txtName.setTypeface(activity.getTypeface());
        txtEmail.setTypeface(activity.getTypeface());
        txtPhone.setTypeface(activity.getTypeface());
    }

    private void attachEvents() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataStoreManager.logout(getContext());
                activity.showMain();
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
        return inflater.inflate(R.layout.fragment_user_details, container, false);
    }

}
