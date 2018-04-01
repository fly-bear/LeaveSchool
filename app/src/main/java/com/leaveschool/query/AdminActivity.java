package com.leaveschool.query;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.leaveschool.R;
import com.leaveschool.main.Config;
import com.leaveschool.main.Param;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdminActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private Toolbar toolbar;
    private ListView listView;
    private SimpleAdapter adapter;
    private ArrayList<HashMap<String, Object>> arrayList;
    private String resultStr;
    private JSONArray jsonArray ;

    private String cookie;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Config.QueryMessageSuccess:
                    resultStr = (String) msg.obj;
                    Toast.makeText(AdminActivity.this, "获取信息成功", Toast.LENGTH_SHORT).show();
                    initListView();
                    break;
                case Config.QueryMessageFail:
                    Toast.makeText(AdminActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        findAllViews();
        init();
    }

    private void findAllViews() {
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        listView = (ListView) findViewById(R.id.cjlv);
    }

    private void init() {
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        resultStr = intent.getStringExtra("resultStr");
        String[] temp = resultStr.split("cookie:");
        resultStr = temp[0];
        cookie = temp[1];

        SharedPreferences pref = getSharedPreferences("AdminInfos", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("AdminCookie", cookie);
        editor.commit();

        arrayList = new ArrayList<>();
        String[] paramFrom = {"week", "name", "leavetime", "returntime", "status"};
        int[] paramTo = new int[]{
                R.id.weekNum,
                R.id.name,
                R.id.leaveTime,
                R.id.returnTime,
                R.id.status};
        adapter = new SimpleAdapter(this, arrayList, R.layout.list_item,
                paramFrom,
                paramTo);
        listView.setAdapter(adapter);

        initListView();
        listView.setOnItemClickListener(this);
    }
    private void initListView() {
        try {
            if(resultStr != null && !resultStr.equals("")){
                jsonArray = new JSONArray(resultStr);
                if(jsonArray.length() == 0){
                    Toast.makeText(this, "获取到0条信息，可能没有提交信息，也有可能输入的姓名学号不正确。", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        refreshData();
        adapter.notifyDataSetChanged();
    }
    private void refreshData() {
        arrayList.clear();
        try {
            for(int i=0; i<jsonArray.length(); i++){

                JSONObject temp = (JSONObject) jsonArray.get(i);

                HashMap<String, Object> map = new HashMap<>();
                String rescore = "";
                map.put("week",         temp.get( Param.CN_week)    );
                map.put("name",         temp.get( Param.CN_name)    );
                map.put("leavetime",    temp.get( Param.CN_leavetime   )    );
                map.put("returntime",   temp.get( Param.CN_returntime  )    );
                map.put("status",       temp.get( Param.CN_status  )    );
                arrayList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ShowDetailActivity.class);
        JSONObject temp = null;
        try {
             temp = (JSONObject) jsonArray.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonStr = temp.toString();
        jsonStr = jsonStr + "cookie:"+ cookie;
        intent.putExtra("jsonStr", jsonStr);
        startActivity(intent);
    }

    private void fetchDataOnInternet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();
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
//                RequestBody body = new RequestBody.create(mediaType, temp);
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
//                        Log.d("ZL", "result string is"+str);
                    }else{
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_query, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                fetchDataOnInternet();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ZL", "-----Activity onStart() 方法执行！");
    }
    @Override
    protected void onResume() {
        super.onResume();
        fetchDataOnInternet();
        Log.d("ZL", "-----Activity onResume() 方法执行！");

    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("ZL", "-----Activity onPause() 方法执行！");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("ZL", "-----Activity onStop() 方法执行！");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("ZL", "-----Activity onRestart()方法执行");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ZL", "-----Activity onDestroy() 方法执行！");
    }

}
