package com.orientalmusic.music.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orientalmusic.music.R;
import com.orientalmusic.music.DataStoreManager;
import com.orientalmusic.music.activity.MainActivity;
import com.orientalmusic.music.dialog.ContactDialog;
import com.orientalmusic.music.entity.User;
import com.orientalmusic.music.interfaces.IAdCaller;


public class MainFragment extends Fragment implements View.OnClickListener,IAdCaller{
    private CardView crdVideo;
    private CardView crdBooks;
    private CardView crdUser;
    private CardView crdTones;
    private CardView crdEctra;
    private CardView crdContact;
    private CardView crdRequest;
    private CardView crdProduct;
    private TextView txtBooks;
    private TextView txtVideos;
    private TextView txtMore;
    private TextView txtContact;
    private TextView txtUser;
    private TextView txtRequest;
    private TextView txtProduct;
    private TextView txtBalance;
    private MainActivity activity;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(MainActivity activity) {
        MainFragment fragment = new MainFragment();
        fragment.activity = activity;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        attachViews();
        attachEvents();
        User user = DataStoreManager.getUser(activity);
        txtBalance.setText(user == null ? "0" : String.valueOf(user.getBalance()));
    }

    private void attachViews() {
        crdVideo = (CardView) getView().findViewById(R.id.crdVideo);
        crdBooks = (CardView) getView().findViewById(R.id.crdBooks);
        crdUser = (CardView) getView().findViewById(R.id.crdUser);
        crdTones = (CardView) getView().findViewById(R.id.crdTones);
        crdEctra = (CardView) getView().findViewById(R.id.crdExtra);
        crdContact = (CardView) getView().findViewById(R.id.crdContact);
        crdRequest = (CardView) getView().findViewById(R.id.crdRequest);
        crdProduct = (CardView) getView().findViewById(R.id.crdProducts);

        txtBooks = (TextView) getView().findViewById(R.id.txtBooks);
        txtVideos = (TextView) getView().findViewById(R.id.txtVideos);
        txtMore = (TextView) getView().findViewById(R.id.txtMore);
        txtContact = (TextView) getView().findViewById(R.id.txtContact);
        txtUser = (TextView) getView().findViewById(R.id.txtUser);
        txtBalance = (TextView) getView().findViewById(R.id.txtBalance);
        txtProduct = (TextView) getView().findViewById(R.id.txtProducts);

        txtRequest = getView().findViewById(R.id.txtRequest);
        txtBooks.setTypeface(activity.getTypeface());
        txtVideos.setTypeface(activity.getTypeface());
        txtMore.setTypeface(activity.getTypeface());
        txtContact.setTypeface(activity.getTypeface());
        txtUser.setTypeface(activity.getTypeface());
        txtRequest.setTypeface(activity.getTypeface());
        txtBalance.setTypeface(activity.getTypeface());
        txtProduct.setTypeface(activity.getTypeface());
    }

    private void attachEvents() {
        crdBooks.setOnClickListener(this);
        crdVideo.setOnClickListener(this);
        crdUser.setOnClickListener(this);
        crdTones.setOnClickListener(this);
        crdEctra.setOnClickListener(this);
        crdContact.setOnClickListener(this);
        crdRequest.setOnClickListener(this);
        crdProduct.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.crdVideo:
                activity.showVideoCategories();
                break;
            case R.id.crdBooks:
                activity.showBookCategories();
                break;
            case R.id.crdUser:
                User user = DataStoreManager.getUser(getContext());
                Log.e("token", "token is : " + DataStoreManager.getToken(getContext()));
                if (user != null)
                    if (user.isOn())
                        activity.showUserDetails();
                    else
                        activity.showLogin();
                else
                    activity.showRegister();
                break;
            case R.id.crdTones:
                if (activity.rewardedVideoAd.isLoaded()) {
                    activity.showAd(this);
                }

                break;

            case R.id.crdExtra:
                activity.showMore();
                break;

            case R.id.crdProducts:
                activity.showProducts();
                break;
            case R.id.crdRequest:
                activity.showRequest();
                break;
            case R.id.crdContact:
                ContactDialog dialog = new ContactDialog(activity);
                dialog.show();
                break;
        }
    }

    @Override
    public void onRewarded() {
        User user = DataStoreManager.getUser(activity);
        txtBalance.setText(user == null ? "0" : String.valueOf(user.getBalance()));
    }
}
