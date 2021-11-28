package com.example.puyee.utils;

import com.example.puyee.bean.token.TokenReq;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkUtils {
    public static String getHuaweiToken(String url, TokenReq req) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, new Gson().toJson(req));
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String header = response.header("X-Subject-Token");
            return header;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
