package com.orientalmusic.music.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orientalmusic.music.DataStoreManager;
import com.orientalmusic.music.entity.User;
import com.orientalmusic.music.service.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by kameloov on 12/10/2017.
 */

public class RefreshUserTask extends AsyncTask<Void, Void, Service.Response> {
    private int id;
    private Context context;

    public RefreshUserTask(Context context, int id) {
        this.context = context;
        this.id = id;
    }

    @Override
    protected Service.Response doInBackground(Void... voids) {
        Service.Response response = null;
        Service service = new Service();
        try {
            response = service.getUser(id);
            if (response != null && response.isSuccess()) {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<User>>() {

                }.getType();
                ArrayList<User> users = gson.fromJson(response.getResponseObject(), type);
                if (users.size() > 0) {
                    User old = DataStoreManager.getUser(context);
                    User current = users.get(0);
                    current.setOn(old.isOn());
                    DataStoreManager.setUser(context, current);
                } else
                    Log.e("refreshUser", "list is empty");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
