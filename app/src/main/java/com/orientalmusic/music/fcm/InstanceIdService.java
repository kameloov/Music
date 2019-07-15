package com.orientalmusic.music.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.orientalmusic.music.DataStoreManager;
import com.orientalmusic.music.service.Service;

/**
 * Created by kameloov on 11/30/2017.
 */

public class InstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        new Thread(new Runnable() {
            @Override
            public void run() {
                DataStoreManager.setToken(getApplicationContext(), refreshedToken);
                Service service = new Service();
                try {
                    service.sendFcm(refreshedToken);
                } catch (Exception e) {
                    Log.e("error fcm","error submiting fcm key");
                    e.printStackTrace();
                }
            }
        }).run();


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(refreshedToken);
    }


}
