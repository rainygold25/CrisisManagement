package com.example.crisismanagement;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DownloadTaskAddress extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            String responseBody_string = responseBody.string();
            Log.d("response", responseBody_string);
            return responseBody_string;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
