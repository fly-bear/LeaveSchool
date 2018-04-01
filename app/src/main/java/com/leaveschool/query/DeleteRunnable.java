package com.leaveschool.query;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.leaveschool.main.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/3/17.
 */

public class DeleteRunnable implements Runnable {

    private String cookie;
    private String index;
    private Handler handler;

    public DeleteRunnable(String cookie, String index, Handler handler) {
        this.cookie= cookie;
        this.index = index;
        this.handler = handler;
    }

    @Override
    public void run() {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();

        JSONObject json = new JSONObject();
        try {
            json.put("index", index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String temp = json.toString();
        MediaType type = MediaType.parse("application/json; charset=utf-8");;
//        RequestBody body = new RequestBody.create(mediaType, temp);
        RequestBody body = new FormBody.Builder()
                .build();

        Request request_4 = new Request.Builder()
                .url("http://bear.flybear.wang:8080/delete")
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
            String str = response.body().string();
            if(response.isSuccessful() && str.contains("ojbk")){
                message.what = Config.DeleteMessageSuccess;
                message.obj = str;
                handler.sendMessage(message);
                Log.d("ZL", "result string is"+str);
            }else{
                message.what = Config.DeleteMessageFail;
                handler.sendMessage(message);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
