package com.leaveschool.query;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.leaveschool.R;
import com.leaveschool.main.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class QueryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private SimpleAdapter adapter;
    private ArrayList<HashMap<String, Object>> arrayList;
    private String resultStr;
    private JSONArray jsonArray ;

    private String username;
    private String password;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Config.QueryMessageSuccess:
                    resultStr = (String) msg.obj;
                    Toast.makeText(QueryActivity.this, "获取信息成功", Toast.LENGTH_SHORT).show();
                    initListView();
                    break;
                case Config.QueryMessageFail:
                    Toast.makeText(QueryActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        findAllViews();
        init();

        fetchDataOnInternet();



    }

    private void findAllViews() {
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        listView = (ListView) findViewById(R.id.cjlv);
    }

    private void init() {
        setSupportActionBar(toolbar);

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

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
    }

    private void fetchDataOnInternet() {
        QueryRunnable runnable = new QueryRunnable(this, handler, username,password);
        new Thread(runnable).start();
    }

    private void initListView() {
//        listView.removeAllViews();
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
                map.put("week",         temp.get( "week"      )   );
                map.put("name",         temp.get( "name"      )   );
                map.put("leavetime",    temp.get( "leavetime" )   );
                map.put("returntime",   temp.get( "returntime")   );
                map.put("status",       temp.get( "status"    )   );
                arrayList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
}
