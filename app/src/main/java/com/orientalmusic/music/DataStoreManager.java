package com.orientalmusic.music;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orientalmusic.music.entity.Contact;
import com.orientalmusic.music.entity.Order;
import com.orientalmusic.music.entity.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataStoreManager {
    static Gson gson = new Gson();

    public static User getUser(Context context) {
        User user = null;
        SharedPreferences preferences = context.getSharedPreferences("user", context.MODE_PRIVATE);
        String userData = preferences.getString("userData", "");
        Log.e("userDate",userData);
        user = gson.fromJson(userData, User.class);
        return user;
    }

    public static void setUser(Context context, User user) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String s = gson.toJson(user, User.class);
        editor.putString("userData", s);
        editor.apply();

    }

    public static void setToken(Context context, String token) {
        SharedPreferences preferences = context.getSharedPreferences("fcm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public static ArrayList<Order> getPurchasedVideos(Context context) {
        ArrayList<Order> videos = null;
        SharedPreferences preferences = context.getSharedPreferences("user", context.MODE_PRIVATE);
        String array = preferences.getString("videos", "");
        Log.e("purchased videos", " " + array);
        Type type = new TypeToken<List<Order>>() {
        }.getType();
        videos = gson.fromJson(array, type);
        return videos;
    }

    public static void setPurchasedVideos(Context context, ArrayList<Order> orders) {
        if (orders != null) {
            SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("videos", gson.toJson(orders));
            editor.apply();
        }
    }


    public static Contact getContactData(Context context) {
        Contact c = null;
        SharedPreferences preferences = context.getSharedPreferences("user", context.MODE_PRIVATE);
        String array = preferences.getString("contact", "");
        c = gson.fromJson(array, Contact.class);
        return c;
    }

    public static void setContactData(Context context, Contact contact) {
        if (contact != null) {
            SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("contact", gson.toJson(contact));
            editor.apply();
        }
    }
    public static String getToken(Context context) {
        String token = "";
        SharedPreferences preferences = context.getSharedPreferences("fcm", context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        return token;
    }

    public static void login(Context context) {
        User user = getUser(context);
        if (user != null) {
            user.setOn(true);
            setUser(context, user);
        }
    }

    public static void logout(Context context) {
        User user = getUser(context);
        if (user != null) {
            user.setOn(false);
            setUser(context, user);
        }
    }
}
