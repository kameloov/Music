package com.orientalmusic.music.task;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orientalmusic.music.DataStoreManager;
import com.orientalmusic.music.entity.Order;
import com.orientalmusic.music.service.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kameloov on 12/10/2017.
 */

public class RefreshVideosTask extends AsyncTask<Void, Void, Service.Response> {
    private Context context;
    private int userId;

    public RefreshVideosTask(Context context, int userId) {
        this.context = context;
        this.userId = userId;
    }

    @Override
    protected Service.Response doInBackground(Void... voids) {
        Service service = new Service();
        Service.Response response = null;
        try {
            response = service.getPurchasedItems(userId, 0);
            if (response != null && response.isSuccess()) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<Order>>() {

                }.getType();
                ArrayList<Order> orders = gson.fromJson(response.getResponseObject(), type);
                if (orders != null && orders.size() > 0) {
                    DataStoreManager.setPurchasedVideos(context, orders);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
