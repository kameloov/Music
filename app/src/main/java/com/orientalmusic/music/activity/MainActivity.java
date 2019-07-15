package com.orientalmusic.music.activity;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.orientalmusic.music.DialogManager;
import com.orientalmusic.music.R;
import com.orientalmusic.music.DataStoreManager;
import com.orientalmusic.music.entity.Book;
import com.orientalmusic.music.entity.Category;
import com.orientalmusic.music.entity.More;
import com.orientalmusic.music.entity.SubCategory;
import com.orientalmusic.music.entity.User;
import com.orientalmusic.music.entity.Video;
import com.orientalmusic.music.fragment.BdfViewerFragment;
import com.orientalmusic.music.fragment.BookCategoryFragment;
import com.orientalmusic.music.fragment.BookDetailsFragment;
import com.orientalmusic.music.fragment.BooksFragment;
import com.orientalmusic.music.fragment.LoginFragment;
import com.orientalmusic.music.fragment.MainFragment;
import com.orientalmusic.music.fragment.MoreDetailsFragment;
import com.orientalmusic.music.fragment.MoreFragment;
import com.orientalmusic.music.fragment.ProductFragment;
import com.orientalmusic.music.fragment.RegisterFragment;
import com.orientalmusic.music.fragment.RequestSongFragment;
import com.orientalmusic.music.fragment.SubCategoryFragment;
import com.orientalmusic.music.fragment.UserDetailsFragment;
import com.orientalmusic.music.fragment.VideoCategoryFragment;
import com.orientalmusic.music.fragment.VideoDetailsFragment;
import com.orientalmusic.music.fragment.VideosFragment;
import com.orientalmusic.music.interfaces.IAdCaller;
import com.orientalmusic.music.service.Service;

import java.io.File;

public class MainActivity extends AppCompatActivity implements RewardedVideoAdListener {
    public RewardedVideoAd rewardedVideoAd;
    private FrameLayout container;
    private Typeface typeface;
    private IAdCaller caller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = (FrameLayout) findViewById(R.id.container);
        showMain();
        typeface = Typeface.createFromAsset(getAssets(), "fonts/capture.ttf");
        MobileAds.initialize(this, "ca-app-pub-9710938493931950~2724719623");
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void loadRewardedVideoAd() {
        rewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }

    public void showMain() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, 0);
        transaction.replace(R.id.container, MainFragment.newInstance(this));
       // transaction.addToBackStack("main");
        transaction.commit();
    }

    public void showVideos(SubCategory subCategory) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, 0);
        transaction.replace(R.id.container, VideosFragment.newInstance(this, subCategory));
        transaction.addToBackStack("videos");
        transaction.commit();
    }

    public void showVideo(Video video) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, 0);
        transaction.replace(R.id.container, VideoDetailsFragment.newInstance(this, video));
        transaction.addToBackStack("video_details");
        transaction.commit();
    }

    public void showRegister() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, 0);
        transaction.replace(R.id.container, RegisterFragment.newInstance(this));
        transaction.addToBackStack("register");
        transaction.commit();
    }

    public void showLogin() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, 0);
        transaction.replace(R.id.container, LoginFragment.newInstance(this));
        transaction.addToBackStack("login");
        transaction.commit();
    }

    public void showVideoCategories() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, 0);
        transaction.replace(R.id.container, VideoCategoryFragment.newInstance(this));
        transaction.addToBackStack("video_category");
        transaction.commit();
    }

    public void showBookCategories() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, 0);
        transaction.replace(R.id.container, BookCategoryFragment.newInstance(this));
        transaction.addToBackStack("book_category");
        transaction.commit();
    }
    public void showSubCategories(Category category, boolean isVideo) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, 0);
        transaction.replace(R.id.container, SubCategoryFragment.newInstance(this,category,isVideo));
        transaction.addToBackStack("book_category");
        transaction.commit();
    }
    public void showbook(Book book) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, 0);
        transaction.replace(R.id.container, BookDetailsFragment.newInstance(this, book));
        transaction.addToBackStack("book_details");
        transaction.commit();
    }

    public void showAd(IAdCaller caller){
        this.caller = caller;
        rewardedVideoAd.show();
    }

    @Override
    public void onBackPressed() {
        int count =MainActivity.this.getSupportFragmentManager().getBackStackEntryCount();
        Log.e("stack","count is = "+count);
        super.onBackPressed();
    }

    public void showUserDetails() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, 0);
        transaction.replace(R.id.container, UserDetailsFragment.newInstance(this));
        transaction.addToBackStack("user_details");
        transaction.commit();
    }

    public void showBooks(SubCategory subCategory) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, 0);
        transaction.replace(R.id.container, BooksFragment.newInstance(this, subCategory));
        transaction.addToBackStack("books");
        transaction.commit();
    }

    public void showPdf(File file) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, 0);
        transaction.replace(R.id.container, BdfViewerFragment.newInstance(file));
        transaction.addToBackStack("pdf");
        transaction.commit();
    }

    public void showMore() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, 0);
        transaction.replace(R.id.container, MoreFragment.newInstance(this));
        transaction.addToBackStack("more");
        transaction.commit();
    }


    public void showProducts() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, 0);
        transaction.replace(R.id.container, ProductFragment.newInstance(this));
        transaction.addToBackStack("more");
        transaction.commit();
    }

    public void showMoreDetails(More more) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, 0);
        transaction.replace(R.id.container, MoreDetailsFragment.newInstance(more));
        transaction.addToBackStack("more_details");
        transaction.commit();
    }

    public void showRequest() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, 0);
        transaction.replace(R.id.container, RequestSongFragment.newInstance());
        transaction.addToBackStack("request_song");
        transaction.commit();
    }

    @Override
    public void onResume() {
        rewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        rewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        rewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        AddBalanceTask task = new AddBalanceTask(DataStoreManager.getUser(this).getId(), rewardItem.getAmount(),caller);
        task.execute();
        DialogManager.infoDialog(this, getString(R.string.rewarded_by) + rewardItem.getAmount());
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
       // DialogManager.infoDialog(this, getString(R.string.load_ad_error));
    }

    class AddBalanceTask extends AsyncTask<Void, Void, Service.Response> {
        private int id;
        private int balance;
        private IAdCaller caller;

        public AddBalanceTask(int id, int balance,IAdCaller caller) {
            this.id = id;
            this.balance = balance;
            this.caller = caller;
        }

        @Override
        protected void onPostExecute(Service.Response response) {
            super.onPostExecute(response);
            if (response.isSuccess()) {
                User user = DataStoreManager.getUser(MainActivity.this);
                user.setBalance(user.getBalance() + balance);
                DataStoreManager.setUser(MainActivity.this, user);
                if (caller!=null)
                    caller.onRewarded();
            }
        }

        @Override
        protected Service.Response doInBackground(Void... voids) {
            Service service = new Service();
            Service.Response response = null;
            try {
                response = service.addBalance(id, balance);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

}
