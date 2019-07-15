package com.orientalmusic.music.service;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.orientalmusic.music.klib.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Service {
    public static final String url = "http://myoriental-music.com/service";
    private static final int TIME_OUT = 10000;
    private JsonParser parser;

    public Service() {
        parser = new JsonParser();
    }


    public Response get(String urlString, int timeout) throws Exception {
        Response result = null;
        InputStream connectionInputStream = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(timeout);
            connectionInputStream = conn.getResponseCode() == 200 ? conn.getInputStream() : conn.getErrorStream();
            result = new Response();
            String res = new String(StreamUtils.load(connectionInputStream), "utf-8");
            JsonElement responseObject = parser.parse(res);
            boolean success = responseObject.getAsJsonObject().get("success").getAsInt() > 0;
            result.setSuccess(success);
            if (success)
                result.setResponseObject(responseObject.getAsJsonObject().get("data"));
            else
                result.setMessage(responseObject.getAsJsonObject().get("message").getAsString());
        } finally {
            StreamUtils.close(connectionInputStream);
        }
        return result;
    }


    public Response post(String urlString, byte[] data, int timeout) throws Exception {
        Response response = null;
        byte[] result = null;
        HttpURLConnection connection = null;
        ByteArrayInputStream dataInputStream;
        OutputStream connectionOutputStream = null;
        InputStream connectionInputStream = null;
        ByteArrayOutputStream dataOutputStream = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(timeout);
            dataInputStream = new ByteArrayInputStream(data);
            connectionOutputStream = connection.getOutputStream();
            StreamUtils.copyStream(dataInputStream, connectionOutputStream);
            connectionInputStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream();
            dataOutputStream = new ByteArrayOutputStream();
            StreamUtils.copyStream(connectionInputStream, dataOutputStream);
            result = dataOutputStream.toByteArray();
            response = new Response();
            Log.e("response", new String(result));
            JsonElement element = parser.parse(new String(result, "utf-8"));
            boolean success = element.getAsJsonObject().get("success").getAsInt() > 0;
            response.setSuccess(success);
            if (success)
                response.setResponseObject(element.getAsJsonObject().get("data"));
            else
                response.setMessage(element.getAsJsonObject().get("message").getAsString());
        } finally {
            if (connectionOutputStream != null) {
                connectionOutputStream.close();
            }

            if (connectionInputStream != null) {
                connectionInputStream.close();
            }
        }
        return response;
    }

    public Response register(String email, String password, String name, String phone, String token) throws Exception {
        Response response = null;
        JsonObject params = new JsonObject();
        params.addProperty("email", email);
        params.addProperty("password", password);
        params.addProperty("name", name);
        params.addProperty("phone", phone);
        params.addProperty("fcm_key", token);
        Log.e("params", params.toString());
        response = post(url + "users/register", params.toString().getBytes(), TIME_OUT);
        Log.e("register", response.isSuccess() ? response.getResponseObject().toString() : response.getMessage());
        return response;
    }

    public Response sendFcm(String fcm) throws Exception {
        Response response = null;
        JsonObject params = new JsonObject();
        params.addProperty("fcm_key", fcm);
        response = post(url + "users/fcm", params.toString().getBytes(), TIME_OUT);
        Log.e("send fcm", response.getResponseObject().toString());
        return response;
    }

    public Response login(String email, String password) throws Exception {
        Response response = null;
        JsonObject params = new JsonObject();
        params.addProperty("email", email);
        params.addProperty("password", password);
        Log.e("params", params.toString());
        response = post(url + "users/login", params.toString().getBytes(), TIME_OUT);
        Log.e("login", response.isSuccess() ? response.getResponseObject().toString() : response.getMessage());
        return response;
    }

    public Response addBalance(int id, int balance) throws Exception {
        Response response = null;
        JsonObject params = new JsonObject();
        params.addProperty("id", id);
        params.addProperty("balance", balance);
        Log.e("params", params.toString());
        response = post(url + "users/balance", params.toString().getBytes(), TIME_OUT);
        Log.e("addBalance", response.getResponseObject().toString());
        return response;
    }

    public Response addRequest(String title, String text) throws Exception {
        Response response = null;
        JsonObject params = new JsonObject();
        params.addProperty("title", title);
        params.addProperty("text", text);
        Log.e("params", params.toString());
        response = post(url + "request", params.toString().getBytes(), TIME_OUT);
        Log.e("add request", response.getResponseObject().toString());
        return response;
    }

    public Response buyItem(int itemID, int userId, int type) throws Exception {
        Response response = null;
        JsonObject params = new JsonObject();
        params.addProperty("userId", userId);
        params.addProperty("itemId", itemID);
        params.addProperty("type", type);
        Log.e("params", params.toString());
        response = post(url + "orders", params.toString().getBytes(), TIME_OUT);
        Log.e("buy item", response.getResponseObject().toString());
        return response;
    }

    public Response getUser(int id) throws Exception {
        Response response = null;
        response = get(url + "/users/" + id, TIME_OUT);
        return response;
    }

    public Response getPurchasedItems(int userId, int type) throws Exception {
        Response response = null;
        response = get(url + "/orders/" + userId + "/" + type, TIME_OUT);
        if (response.isSuccess())
            Log.e("getPurchased", response.getResponseObject().toString());
        return response;
    }

    public Response getCategories() throws Exception {
        Response response = null;
        response = get(url + "/category", TIME_OUT);
        return response;
    }

    public Response getSubCategories(int catId) throws Exception {
        Response response = null;
        response = get(url + "/subcategory/"+catId, TIME_OUT);
        return response;
    }

    public Response getContactUsINfo() throws Exception{
        Response response = null;
        response = get(url + "/contact", TIME_OUT);
        return response;
    }

    public Response getVideos(int minId, int categoryId) throws Exception {
        Response response = null;
        response = get(url + "/videos/" + categoryId + "/" + minId, TIME_OUT);
        return response;
    }

    public Response getBooks(int minId, int categoryId) throws Exception {
        Response response = null;
        response = get(url + "/books/" + categoryId + "/" + minId, TIME_OUT);
        return response;
    }

    public Response getMore() throws Exception {
        Response response = null;
        response = get(url + "/more", TIME_OUT);
        return response;
    }

    public Response getProducts() throws Exception {
        Response response = null;
        response = get(url + "/product", TIME_OUT);
        return response;
    }

    public static class Response {
        private boolean success;
        private JsonElement responseObject;
        private String message;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public JsonElement getResponseObject() {
            return responseObject;
        }

        public void setResponseObject(JsonElement responseObject) {
            this.responseObject = responseObject;
        }
    }
}
