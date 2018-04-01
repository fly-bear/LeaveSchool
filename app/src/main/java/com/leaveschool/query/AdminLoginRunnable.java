package com.leaveschool.query;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.leaveschool.main.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/3/17.
 */

public class AdminLoginRunnable implements Runnable{
    private Handler handler;

    private String username;
    private String password;

    private String cookie;

    public AdminLoginRunnable(Handler handler, String username, String password) {
        this.handler = handler;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();

        Request request_1 = new Request.Builder()
                .url("http://bear.flybear.wang:8080/tologin")
                .build();


        try {
            Response response = client.newCall(request_1).execute();
            Log.d("ZL", "first response is:"+response.toString());
            if(response.isSuccessful()){
                cookie = response.header("Set-Cookie").split(";")[0];
            }else{
                return;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody form = new FormBody.Builder()
                .add("logname", username)
                .add("logpass", password)
                .build();
        Request request_2 = new Request.Builder()
                .url("http://bear.flybear.wang:8080/login")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "zh-CN,zh;q=0.8")
                .header("Connection", "keep-alive")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Cookie", cookie)
                .header("Host", "bear.flybear.wang:8080")      //Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.108 Safari/537.36 2345Explorer/8.2.2.14352")
                .header("Referer", "http://bear.flybear.wang:8080/tologin")
                .post(form)
                .build();
        String firstRedirectUrl = "";
        try {
            Response response = null;
            response = client.newCall(request_2).execute();
            Log.d("ZL", "second response is:"+response.toString());
//            Log.d("ZL", "second response body is:"+response.body().string());
            if(response.isSuccessful()){
                firstRedirectUrl = response.header("Location");
                Log.d("ZL", "firstRedirectUrl is:"+firstRedirectUrl);
            }else{
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Request request_3 = new Request.Builder()
//                .url(firstRedirectUrl)
//                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
//                .header("Accept-Encoding", "gzip, deflate")
//                .header("Accept-Language", "zh-CN,zh;q=0.8")
//                .header("Connection", "keep-alive")
//                .header("Content-Type", "application/x-www-form-urlencoded")
//                .header("Cookie", cookie)
//                .header("Host", "bear.flybear.wang:8080")      //Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0
//                .header("Origin", "http://bear.flybear.wang:8080")
//                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.108 Safari/537.36 2345Explorer/8.2.2.14352")
////                .header("Referer", "http://bear.flybear.wang:8080/form")
//                .build();
//        try {
//            Response response = client.newCall(request_3).execute();
//            Log.d("ZL", "first response is:"+response.toString());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        JSONObject json = new JSONObject();
        try {
            json.put("week",    "全部");
            json.put("major",   "全部");
            json.put("classes", "全部");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String temp = json.toString();
        MediaType type = MediaType.parse("application/json; charset=utf-8");;
//        RequestBody body = new RequestBody.create(mediaType, temp);
        RequestBody body = new FormBody.Builder()
                .build();

        Request request_4 = new Request.Builder()
                .url("http://bear.flybear.wang:8080/search")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "zh-CN,zh;q=0.8")
                .header("Connection", "keep-alive")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Cookie", cookie)
                .header("Host", "bear.flybear.wang:8080")      //Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0
                .header("Origin", "http://bear.flybear.wang:8080")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.108 Safari/537.36 2345Explorer/8.2.2.14352")
                .post(body)
                .put(RequestBody.create(type, temp))
                .build();
        try {
            Response response = client.newCall(request_4).execute();
            Log.d("ZL", "third response is:"+response.toString());
            Message message = new Message();
            if(response.isSuccessful()){
                String str = response.body().string();
                str = str + "cookie:"+cookie;
                message.what = Config.QueryMessageSuccess;
                message.obj = str;
                handler.sendMessage(message);
                Log.d("ZL", "result string is"+str);
//                Log.d("ZL", "result string is"+str);
            }else{
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
