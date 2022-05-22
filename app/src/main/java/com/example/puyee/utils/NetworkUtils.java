package com.example.puyee.utils;

import android.graphics.Bitmap;

import com.example.puyee.bean.recognize.RecognizeRsp;
import com.example.puyee.bean.token.Auth;
import com.example.puyee.bean.token.Domain;
import com.example.puyee.bean.token.Identity;
import com.example.puyee.bean.token.Password;
import com.example.puyee.bean.token.Project;
import com.example.puyee.bean.token.Scope;
import com.example.puyee.bean.token.TokenReq;
import com.example.puyee.bean.token.User;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkUtils {
    public static String getQiaofeiToken() {
        TokenReq req = new TokenReq();
        Auth auth = new Auth();
        Identity identity = new Identity();
        Password password = new Password();
        User user = new User();
        Domain domain = new Domain();
        domain.setName("sophie_0825");
        user.setName("sophie_0825");
        user.setDomain(domain);
        user.setPassword("hw012Shishier");
        password.setUser(user);
        List<String> methods = new ArrayList<>();
        methods.add("password");
        identity.setMethods(methods);
        identity.setPassword(password);
        auth.setIdentity(identity);
        Scope scope = new Scope();
        Project project = new Project();
        project.setName("ap-southeast-1");
        scope.setProject(project);
        auth.setScope(scope);
        req.setAuth(auth);
        return NetworkUtils.getHuaweiToken("https://iam.ap-southeast-1.myhwclouds.com/v3/auth/tokens", req);
    }



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

    public static byte [] bitmapToByte(Bitmap bitmap, Bitmap.CompressFormat format) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, 100, baos);
        return baos.toByteArray();
    }

    public static RecognizeRsp getPuyeeRecognize(byte[] bytes) {
        String url = "https://d206d0062c244291b76468b9a883f36a.apigw.ap-southeast-1.huaweicloud.com/v1/infers/eb056f64-4af4-445c-b519-58400c9e3bb0";
        String token = getQiaofeiToken();
        RecognizeRsp rsp = null;
        try {
//            rsp = new Gson().fromJson(Constants.recognize_result, RecognizeRsp.class);
            rsp = getRecognizeResult(url, token, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rsp;
    }
    public static RecognizeRsp getRecognizeResult(String url, String token, byte[] bytes) throws Exception {
        OkHttpClient client = new OkHttpClient().newBuilder().callTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("images","test.png", RequestBody.create(MediaType.parse("application/octet-stream"), bytes)).build();
        Request request = new Request.Builder().url(url).method("POST", body).addHeader("X-Auth-Token", token).build();
        Response response = null;
        response = client.newCall(request).execute();
        RecognizeRsp rsp = new Gson().fromJson(response.body().string(), RecognizeRsp.class);
        return rsp;
    }
}
