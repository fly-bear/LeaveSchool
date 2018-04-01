package com.leaveschool.query;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.leaveschool.R;
import com.leaveschool.main.Config;
import com.leaveschool.main.Param;
import com.leaveschool.main.ProfileEdit;

import org.json.JSONException;
import org.json.JSONObject;

public class ShowDetailActivity extends AppCompatActivity {

//    private DetailItem grade;
//    private DetailItem major;
    private DetailItem classes;
    private DetailItem id;
    private DetailItem name;
    private DetailItem area;
    private DetailItem dormitory;
    private DetailItem isleft;
    private DetailItem aim;
    private DetailItem reason;
//    private DetailItem phonenumber;
//    private DetailItem emergencycontact;
//    private DetailItem emergencynumber;
//    private DetailItem homecontact;
//    private DetailItem homenumber;
//    private DetailItem isinlib;

    private DetailItem leavetime    ;
    private DetailItem returntime   ;
    private DetailItem status       ;

    private Button cancel;
    private Button delete;

    JSONObject jsonObject;

    private String cookie;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Config.DeleteMessageSuccess:
                    Toast.makeText(ShowDetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case Config.DeleteMessageFail:
                    Toast.makeText(ShowDetailActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

        getWindow().setLayout(dip2px(300), dip2px(480));

        findAllViews();
        setListeners();
        initValues();
        getDataFromPre();
        updateValues();

    }

    private void findAllViews() {
//        grade               = (DetailItem) findViewById(R.id.grade);
//        major               = (DetailItem) findViewById(R.id.major);
        classes             = (DetailItem) findViewById(R.id.classes);
        id                  = (DetailItem) findViewById(R.id.id);
        name                = (DetailItem) findViewById(R.id.name);
        area                = (DetailItem) findViewById(R.id.area);
        dormitory           = (DetailItem) findViewById(R.id.dormitory);
        isleft              = (DetailItem) findViewById(R.id.isleft);
        aim                 = (DetailItem) findViewById(R.id.aim);
        reason              = (DetailItem) findViewById(R.id.reason);
//        phonenumber         = (DetailItem) findViewById(R.id.phonenumber);
//        emergencycontact    = (DetailItem) findViewById(R.id.emergencycontact);
//        emergencynumber     = (DetailItem) findViewById(R.id.emergencynumber);
//        homecontact         = (DetailItem) findViewById(R.id.homecontact);
//        homenumber          = (DetailItem) findViewById(R.id.homenumber);
//        isinlib             = (DetailItem) findViewById(R.id.isinlib);

        leavetime           = (DetailItem) findViewById(R.id.leavetime );
        returntime          = (DetailItem) findViewById(R.id.returntime);
        status              = (DetailItem) findViewById(R.id.status);

        cancel = (Button) findViewById(R.id.cancel);
        delete = (Button) findViewById(R.id.delete);
    }

    private void setListeners() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == cancel.getId()){
                    finish();
                }else if(v.getId() == delete.getId()){
                    SharedPreferences pref = getSharedPreferences("AdminInfos", MODE_PRIVATE);
                    String cookie = pref.getString("AdminCookie","");
                    String index = jsonObject.optString("序列号");
                    DeleteRunnable runnable = new DeleteRunnable(cookie, index, handler);
                    new Thread(runnable).start();
                }

            }
        };
        cancel.setOnClickListener(listener);
        delete.setOnClickListener(listener);
    }

    private void initValues() {

//        grade               .set(View.GONE,           0, "年级",   "2015级");
//        major               .set(View.GONE,             0, "专业",   "自动化");
        classes             .set(View.VISIBLE,             0, "班级",   "自动化1502");
        id                  .set(View.VISIBLE,           0, "学号",   "");
        name                .set(View.VISIBLE,           0, "姓名",   "");
        area                .set(View.GONE,             0, "住宿园区","西院");
        dormitory           .set(View.GONE,           0, "宿舍", "例:狮城413");
        isleft              .set(View.GONE,             0, "是否离汉", "");
        aim                 .set(View.VISIBLE,           0, "目的地址", "");
        reason              .set(View.VISIBLE,           0, "请假理由", "");
//        phonenumber         .set(View.GONE,                0, "本人号码", "");
//        emergencycontact    .set(View.GONE,                0, "紧急联系人", "");
//        emergencynumber     .set(View.GONE,                0, "联系方式", "");
//        homecontact         .set(View.GONE,                0, "家庭联系人", "");
//        homenumber          .set(View.GONE,                0, "联系方式", "");
//        isinlib             .set(View.GONE,                0, "是否武汉库", "");

        leavetime           .set(View.VISIBLE,           0, "离校时间", "点击此处选择时间");
        returntime          .set(View.VISIBLE,           0, "返校时间", "点击此处选择时间");
        status              .set(View.VISIBLE,           0, "状态", "");
    }

    private void getDataFromPre() {
        Intent intent = getIntent();
        String jsonStr = "[]";
        jsonStr = intent.getStringExtra("jsonStr");
        String[] temp = jsonStr.split("cookie:");
        jsonStr = temp[0];
        cookie = temp[1];
        try {
            if(jsonStr != null){
                jsonObject = new JSONObject(jsonStr);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateValues() {
        try {
            if(jsonObject != null){
//                grade             .updateValue( jsonObject.getString(Param. grade       ) );
//                major             .updateValue( jsonObject.getString(Param. major       ) );
                classes           .updateValue( jsonObject.getString(Param.CN_classes     ) );
                id                .updateValue( jsonObject.getString(Param.CN_id          ) );
                name              .updateValue( jsonObject.getString(Param.CN_name        ) );
//                area              .updateValue( jsonObject.getString(Param. area        ) );
//                dormitory         .updateValue( jsonObject.getString(Param. dormitory   ) );
//                isleft            .updateValue( jsonObject.getString(Param. isleft      ) );
                aim               .updateValue( jsonObject.getString(Param.CN_aim         ) );
                reason            .updateValue( jsonObject.getString(Param.CN_reason      ) );
                //
                leavetime         .updateValue( jsonObject.getString(Param.CN_leavetime   ) );
                returntime        .updateValue( jsonObject.getString(Param.CN_returntime  ) );
                status            .updateValue( jsonObject.getString(Param.CN_status      ) );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public int dip2px(float dpValue) {
        float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
