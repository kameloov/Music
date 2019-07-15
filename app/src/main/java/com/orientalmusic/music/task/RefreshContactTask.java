package com.orientalmusic.music.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orientalmusic.music.DataStoreManager;
import com.orientalmusic.music.entity.Contact;
import com.orientalmusic.music.entity.User;
import com.orientalmusic.music.service.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by kameloov on 12/10/2017.
 */

public class RefreshContactTask extends AsyncTask<Void, Void, Service.Response> {
    private Context context;

    public RefreshContactTask(Context context) {
        this.context = context;
    }

    @Override
    protected Service.Response doInBackground(Void... voids) {
        Service.Response response = null;
        Service service = new Service();
        try {
            response = service.getContactUsINfo();
            if (response != null && response.isSuccess()) {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Contact>>() {

                }.getType();
                ArrayList<Contact> contacts = gson.fromJson(response.getResponseObject(), type);
                if (contacts.size() > 0) {
                    Contact contact = contacts.get(0);
                    DataStoreManager.setContactData(context,contact);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
