package com.leaveschool.query;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.leaveschool.R;
import com.leaveschool.main.Config;
import com.leaveschool.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText mAccountEdt;
    private EditText mPasswordEdt;
    private Button mLoginBtn;

    private CheckBox saveAdminInfo;
    private CheckBox autoLogin;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Config.QueryMessageSuccess:

                    String resultStr = (String) msg.obj;
                    Intent intent = new Intent(AdminLoginActivity.this, AdminActivity.class);
                    intent.putExtra("resultStr", resultStr);
                    startActivity(intent);
                    finish();
//                    Toast.makeText(QueryActivity.this, "获取信息成功", Toast.LENGTH_SHORT).show();
//                    initListView();
                    break;
                case Config.QueryMessageFail:
//                    Toast.makeText(QueryActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        findAllViews();
        init();
        readInfos();
    }

    private void findAllViews(){
        mAccountEdt = (EditText) findViewById(R.id.account);
        mPasswordEdt = (EditText) findViewById(R.id.password);
        mLoginBtn = (Button) findViewById(R.id.login);

        saveAdminInfo = (CheckBox) findViewById(R.id.saveAdminInfo);
        autoLogin = (CheckBox) findViewById(R.id.autoLogin);
    }

    private void init() {
        mLoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //
                login();
            }
        });
    }
    private void readInfos() {
        SharedPreferences pref = getSharedPreferences("AdminInfos", MODE_PRIVATE);
        boolean isSaveAdminInfos = pref.getBoolean("isSaveAdminInfos",false);
        //上次是否选了记住信息，没有选则返回
        if(!isSaveAdminInfos){
            return;
        }else {
            //选了记住信息
            saveAdminInfo.setChecked(true);
            String AdminAccount = pref.getString("AdminAccount","");
            String AdminPassword = pref.getString("AdminPassword","");
            mAccountEdt.setText(AdminAccount);
            mPasswordEdt.setText(AdminPassword);
        }


    }

    private void login() {
        final String accountStr = mAccountEdt.getText().toString();
        String passwordStr = mPasswordEdt.getText().toString();
        if(TextUtils.isEmpty(accountStr) || TextUtils.isEmpty(passwordStr)){
            Toast.makeText(this, "用户名或密码为空!", Toast.LENGTH_SHORT).show();
            return;
        }

        saveInfo();

        AdminLoginRunnable runnable = new AdminLoginRunnable(handler, accountStr,passwordStr);
        new Thread(runnable).start();

    }

    private void saveInfo() {
        SharedPreferences pref = getSharedPreferences("AdminInfos", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if(saveAdminInfo.isChecked()){
            editor.putBoolean("isSaveAdminInfos", true);
            editor.putString("AdminAccount", mAccountEdt.getText().toString());
            editor.putString("AdminPassword", mPasswordEdt.getText().toString());
        }else{
            editor.putBoolean("isSaveAdminInfos", false);
        }
        if(autoLogin.isChecked()){
            editor.putBoolean("isautoLogin", true);
        }else {
            editor.putBoolean("isautoLogin", false);
        }
        editor.commit();
        Log.d("ZL", "管理员登录信息存储成功");
    }

}
