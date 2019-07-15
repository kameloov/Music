package com.orientalmusic.music.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orientalmusic.music.DataStoreManager;
import com.orientalmusic.music.R;
import com.orientalmusic.music.Social;
import com.orientalmusic.music.activity.MainActivity;
import com.orientalmusic.music.adapter.ImagesAdapter;
import com.orientalmusic.music.adapter.MoreAdapter;
import com.orientalmusic.music.entity.Contact;
import com.orientalmusic.music.entity.More;
import com.orientalmusic.music.service.Service;
import com.orientalmusic.music.task.DownloadBookTask;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kameloov on 12/14/2017.
 */

public class DownloadDialog extends Dialog implements View.OnClickListener {
    private ViewPager pager;
    private ProgressBar progressBar;
    private ProgressBar progressBar2;
    private Button btnCancel;
    private DownloadBookTask task;
    private Context context;
    private Timer timer;
    private Handler handler;

    public DownloadDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().set
        setContentView(R.layout.dialog_download);
        this.context = context;
        attachViews();
        attachEvents();
        progressBar.setMax(100);
        LoadMoreTask task =  new LoadMoreTask();
        task.execute();
    }

    private void attachViews() {
        pager = findViewById(R.id.pager);
        progressBar = findViewById(R.id.progressBar);
        progressBar2 = findViewById(R.id.progressBar2);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void attachEvents() {
        btnCancel.setOnClickListener(this);
    }


    public void setProgress(int progress){
        progressBar.setProgress(progress);
    }



    public void setTask(DownloadBookTask task){
        this.task = task;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCancel:
                if (task!=null);
                task.cancel(false);
                dismiss();
                break;

        }
    }

    class LoadMoreTask extends AsyncTask<Void, Void, Service.Response> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar2.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Service.Response result) {
            super.onPostExecute(result);
            progressBar2.setVisibility(View.GONE);
            if (result != null) {
                if (result.isSuccess()) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<More>>() {
                    }.getType();
                    Log.e("getMore", result.getResponseObject().toString());
                    List<More> moreList = gson.fromJson(result.getResponseObject(), type);
                    if (moreList != null) {
                        ImagesAdapter adapter = new ImagesAdapter(context, moreList);
                        pager.setAdapter(adapter);
                        timer = new Timer();
                        timer.scheduleAtFixedRate(new SwitchTask(),0,4000);
                    }
                }

            }
        }

        @Override
        protected Service.Response doInBackground(Void... voids) {
            Service service = new Service();
            Service.Response object = null;
            try {
                object = service.getProducts();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return object;
        }
    }

    @Override
    public void dismiss() {
        timer.cancel();
        super.dismiss();

    }

    class SwitchTask extends TimerTask{

        @Override
        public void run() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    nextProduct();
                }
            });

        }

        private void nextProduct(){
            int count = pager.getAdapter().getCount();
            int current = pager.getCurrentItem();
            if (current<count-1)
                pager.setCurrentItem(current+1);
            else
                pager.setCurrentItem(0);
        }
    }
}
