package com.leaveschool.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.leaveschool.query.AdminLoginActivity;
import com.leaveschool.query.QueryActivity;
import com.leaveschool.R;
import com.leaveschool.widget.BToast;
import com.leaveschool.widget.CustomDatePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ProfileEdit grade;
    private ProfileEdit major;
    private ProfileEdit classes;
    private ProfileEdit id;
    private ProfileEdit name;
    private ProfileEdit area;
    private ProfileEdit dormitory;
    private ProfileEdit isleft;
    private ProfileEdit aim;
    private ProfileEdit reason;
    private ProfileEdit phonenumber;
    private ProfileEdit emergencycontact;
    private ProfileEdit emergencynumber;
    private ProfileEdit homecontact;
    private ProfileEdit homenumber;
    private ProfileEdit isinlib;


    private ProfileEdit leaveTime;
    private ProfileEdit returnTime;

    private CheckBox isSaveInfoCheBox;
    private Button submit;
//    private Spinner weekNum;
    private TextView weekTitle;
    private int weekNum = 2;

    CustomDatePicker customDatePicker;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    BToast.showText(MainActivity.this,"提交成功\n请点击查询核对信息",Toast.LENGTH_LONG, true);
                    submit.setEnabled(true);
                    submit.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    break;
                case 0:
                    BToast.showText(MainActivity.this,"提交失败",Toast.LENGTH_SHORT, false);
                    submit.setEnabled(true);
                    submit.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findAllViews();
        getWeekNum();
        initValues();
        readInfos();
        initDropEditTexts();
        initDatePicker();
    }

    private void findAllViews() {
        weekTitle = (TextView) findViewById(R.id.weekTitle);

        grade               = (ProfileEdit) findViewById(R.id.grade              );
        major               = (ProfileEdit) findViewById(R.id.major              );
        classes             = (ProfileEdit) findViewById(R.id.classes            );
        id                  = (ProfileEdit) findViewById(R.id.id                 );
        name                = (ProfileEdit) findViewById(R.id.name               );
        area                = (ProfileEdit) findViewById(R.id.area               );
        dormitory           = (ProfileEdit) findViewById(R.id.dormitory          );
        isleft              = (ProfileEdit) findViewById(R.id.isleft             );
        aim                 = (ProfileEdit) findViewById(R.id.aim                );
        reason              = (ProfileEdit) findViewById(R.id.reason             );
        phonenumber         = (ProfileEdit) findViewById(R.id.phonenumber        );
        emergencycontact    = (ProfileEdit) findViewById(R.id.emergencycontact   );
        emergencynumber     = (ProfileEdit) findViewById(R.id.emergencynumber    );
        homecontact         = (ProfileEdit) findViewById(R.id.homecontact        );
        homenumber          = (ProfileEdit) findViewById(R.id.homenumber         );
        isinlib             = (ProfileEdit) findViewById(R.id.isinlib            );

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        leaveTime             = (ProfileEdit) findViewById(R.id.leaveTime);
        leaveTime.getmValueView().setmEditTextUnfouse();
        leaveTime.getmValueView().getmEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                String now = sdf.format(new Date());
                // 日期格式为yyyy-MM-dd HH:mm
                customDatePicker.show(now, leaveTime.getmKeyView().getText().toString());
            }
        });
//        leaveTime.getmValueView().setListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
//                String now = sdf.format(new Date());
//                // 日期格式为yyyy-MM-dd HH:mm
//                customDatePicker.show(now, leaveTime.getmKeyView().getText().toString());
//            }
//        });
        returnTime             = (ProfileEdit) findViewById(R.id.returnTime);
        returnTime.getmValueView().setmEditTextUnfouse();
        returnTime.getmValueView().getmEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                String now = sdf.format(new Date());
                // 日期格式为yyyy-MM-dd HH:mm
                customDatePicker.show(now, returnTime.getmKeyView().getText().toString());
            }
        });

        isSaveInfoCheBox = (CheckBox) findViewById(R.id.isSaveMyInfo);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
//        weekNum = (Spinner) findViewById(R.id.weekNum);
//        weekNum.setOnItemSelectedListener(this);
    }

    private void getWeekNum() {
        //代码编写时间：2015年11月17日14:40:12
        Calendar cal = Calendar.getInstance();//这一句必须要设置，否则美国认为第一天是周日，而我国认为是周一，对计算当期日期是第几周会有错误
        cal.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);// 每周从周一开始
        cal.setMinimalDaysInFirstWeek(7); // 设置每周最少为7天
        cal.setTime(new Date());
        int weeks=cal.get(Calendar.WEEK_OF_YEAR);
        weekNum = weeks - 8;
        Log.d("ZL", "周数："+weeks);
        Log.d("ZL", "学校周数："+weekNum);
    }

    private void initValues() {
        weekTitle.setText("本周是第"+weekNum+"周");

        grade               .set(View.INVISIBLE,           0, "年级",   "2015级");
        major               .set(View.VISIBLE,             0, "专业",   "自动化");
        classes             .set(View.VISIBLE,             0, "班级",   "自动化1502");
        id                  .set(View.INVISIBLE,           0, "学号",   "");
        name                .set(View.INVISIBLE,           0, "姓名",   "");
        area                .set(View.VISIBLE,             0, "住宿园区","西院");
        dormitory           .set(View.INVISIBLE,           0, "宿舍", "例:狮城413");
        isleft              .set(View.VISIBLE,             0, "是否离汉", "");
        aim                 .set(View.INVISIBLE,           0, "目的地址", "");
        reason              .set(View.INVISIBLE,           0, "请假理由", "");
        phonenumber         .set(View.INVISIBLE,           0, "本人号码", "");
        emergencycontact    .set(View.INVISIBLE,           0, "紧急联系人", "");
        emergencynumber     .set(View.INVISIBLE,           0, "联系方式", "");
        homecontact         .set(View.INVISIBLE,           0, "家庭联系人", "");
        homenumber          .set(View.INVISIBLE,           0, "联系方式", "");
        isinlib             .set(View.VISIBLE,             0, "是否武汉库", "");
        leaveTime           .set(View.GONE,           0, "离校时间", "点击此处选择时间");
        returnTime          .set(View.GONE,           0, "返校时间", "点击此处选择时间");
    }

    private void readInfos() {
        SharedPreferences pref = getSharedPreferences("infos", MODE_PRIVATE);
        boolean isSaveInfos = pref.getBoolean("isSaveInfoCheBox",false);
        //上次是否选了记住信息，没有选则返回
        if(!isSaveInfos){
            return;
        }else {
            //选了记住信息
            isSaveInfoCheBox.setChecked(true);
        }
        String infoStrs = pref.getString("infos","");
        if(infoStrs == null || infoStrs.equals("")){
            return;
        }
        try {
            JSONArray jsonArray = new JSONArray(infoStrs);
            JSONObject infos = (JSONObject) jsonArray.get(0);

            grade             .updateValue( infos.getString( grade           .getmKeyView() .getText() .toString() ) );
            major             .updateValue( infos.getString( major           .getmKeyView() .getText() .toString() ) );
            classes           .updateValue( infos.getString( classes         .getmKeyView() .getText() .toString() ) );
            id                .updateValue( infos.getString( id              .getmKeyView() .getText() .toString() ) );
            name              .updateValue( infos.getString( name            .getmKeyView() .getText() .toString() ) );
            area              .updateValue( infos.getString( area            .getmKeyView() .getText() .toString() ) );
            dormitory         .updateValue( infos.getString( dormitory       .getmKeyView() .getText() .toString() ) );
            isleft            .updateValue( infos.getString( isleft          .getmKeyView() .getText() .toString() ) );
            aim               .updateValue( infos.getString( aim             .getmKeyView() .getText() .toString() ) );
            reason            .updateValue( infos.getString( reason          .getmKeyView() .getText() .toString() ) );
            phonenumber       .updateValue( infos.getString( phonenumber     .getmKeyView() .getText() .toString() ) );
            emergencycontact  .updateValue( infos.getString( emergencycontact.getmKeyView() .getText() .toString() ) );
            emergencynumber   .updateValue( infos.getString( emergencynumber .getmKeyView() .getText() .toString() ) );
            homecontact       .updateValue( infos.getString( homecontact     .getmKeyView() .getText() .toString() ) );
            homenumber        .updateValue( infos.getString( homenumber      .getmKeyView() .getText() .toString() ) );
            isinlib           .updateValue( infos.getString( isinlib         .getmKeyView() .getText() .toString() ) );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initDropEditTexts() {
        String[] strings = new String[]{
            "自动化",
            "自动化zy",
            "电气",
            "电气zy"
        };
        setDropAdapter( major.getmValueView(), strings);

        String[] strings_2 = new String[]{
                "自动化1501",
                "自动化1502",
                "自动化1503",
                "自动化1504",
                "自动化1505",
                "自动化zy1501",
                "电气1501",
                "电气1502",
                "电气1503",
                "电气1504",
                "电气1505",
                "电气1506",
                "电气zy1501"
        };
        setDropAdapter(classes.getmValueView(), strings_2);

        String[] areaStrings = new String[]{
                "东院",
                "西院"
        };
        setDropAdapter(area.getmValueView(), areaStrings);

        String[] isLeftStrings = new String[]{
                "是",
                "否"
        };
        setDropAdapter(isleft.getmValueView(), isLeftStrings);

        String[] isinlibStrings = new String[]{
                "是",
                "否"
        };
        setDropAdapter(isinlib.getmValueView(), isinlibStrings);
    }
    private void setDropAdapter(DropEditText editText, String[] strings) {

        MyBaseAdapter adapter = new MyBaseAdapter(strings);
        editText.setAdapter(adapter);

    }

    class MyBaseAdapter extends BaseAdapter{
//        private String[] strings;
        private List<String> mList = new ArrayList<String>();

        public MyBaseAdapter(String[] strings) {
//            this.strings = strings;
            for(int i=0; i<strings.length; i++){
                mList.add(strings[i]);
            }
        }
        @Override
        public int getCount() {
            return mList.size();
        }
        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            LinearLayout layout = new LinearLayout(getBaseContext());
//            layout.setLayoutParams();

            TextView tv = new TextView(MainActivity.this);
            tv.setText(mList.get(position));
            tv.setTextSize(18);
//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
//            );
//            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
//            );
//            tv.setLayoutParams(params);
//            layout.addView(tv, params);
            return tv;
        }
    }

    private void initDatePicker() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
//        String now = sdf.format(new Date());
//        currentDate.setText(now.split(" ")[0]);
//        currentTime.setText(now);

//        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
//            @Override
//            public void handle(String time) { // 回调接口，获得选中的时间
//                currentDate.setText(time.split(" ")[0]);
//            }
//        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
//        customDatePicker1.showSpecificTime(false); // 不显示时和分
//        customDatePicker1.setIsLoop(false); // 不允许循环滚动

        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time, String requester) { // 回调接口，获得选中的时间
//                currentTime.setText(time);
                if(requester.equals(leaveTime.getmKeyView().getText().toString())){
                    leaveTime.updateValue(time);
                }else if(requester.equals(returnTime.getmKeyView().getText().toString())){
                    returnTime.updateValue(time);
                }
            }
        }, "2010-01-01 00:00", "2020-12-31 23:59"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(true); // 显示时和分
        customDatePicker.setIsLoop(true); // 允许循环滚动
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.submit){
            if(isInfoFull()){
                if(isSaveInfoCheBox.isChecked()){
                    saveMyInfo();
                }
                submit.setEnabled(false);
                submit.setTextColor(Color.GRAY);
                submit();
//                BToast.showText(MainActivity.this, "提交成功\n请点击查询核对信息", Toast.LENGTH_LONG, true);
            }else {
//                Toast.makeText(this, "信息未填完整！", Toast.LENGTH_SHORT).show();
            }
        }
//        // 两个View的ID一样，那个先判断则先执行那个，原因暂时未知
//        else if(v.getId() == returnTime.getmValueView().getId()){
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
//            String now = sdf.format(new Date());
//            // 日期格式为yyyy-MM-dd HH:mm
//            customDatePicker.show(now, returnTime.getmKeyView().getText().toString());
//        }else if(v.getId() == leaveTime.getmValueView().getId()){
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
//            String now = sdf.format(new Date());
//            // 日期格式为yyyy-MM-dd HH:mm
//            customDatePicker.show(now, leaveTime.getmKeyView().getText().toString());
//        }
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }

    private boolean isInfoFull() {
        if(
                grade            .getValue().equals("") ||
                major            .getValue().equals("") ||
                classes          .getValue().equals("") ||
                id               .getValue().equals("") ||
                name             .getValue().equals("") ||
                area             .getValue().equals("") ||
                dormitory        .getValue().equals("") ||
                isleft           .getValue().equals("") ||
                aim              .getValue().equals("") ||
                reason           .getValue().equals("") ||
                phonenumber      .getValue().equals("") ||
                emergencycontact .getValue().equals("") ||
                emergencynumber  .getValue().equals("") ||
                homecontact      .getValue().equals("") ||
                homenumber       .getValue().equals("") ||
                isinlib          .getValue().equals("")
                ){
            Toast.makeText(this, "信息未填完整！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if( !isMobile(phonenumber.getValue())
                || !isMobile(emergencynumber.getValue())
                || !isMobile(homenumber.getValue())){
            Toast.makeText(this, "手机号码格式不正确！", Toast.LENGTH_SHORT).show();
            return false;
        }
//        Pattern pattern = Pattern.compile("^1[3|4|5|6|7|8][0-9]\\d{8}$");
//        Matcher matcher = pattern.matcher(phonenumber.getValue());

        return true;
    }
    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、152、157(TD)、158、159、178(新)、182、184、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、170、173、177、180、181、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
//        String num = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
//        String num = "d{11}";
//        String num = "^\d{11}}$";
//        String num = "^1[\\\\d]{10}";
//        if (TextUtils.isEmpty(number)) {
//            return false;
//        } else {
//            //matches():字符串是否在给定的正则表达式匹配
//            return number.matches(num);
//        }

        if(number.length() == 11){
            return true;
        }else {
            return false;
        }
    }

    private void saveMyInfo() {
        JSONArray infosArr = new JSONArray();
//        infos.put(new JSONObject("", ""));
//        infos.
        JSONObject infosObj = new JSONObject();
        try {
            infosObj.put(String.valueOf( grade           .getmKeyView() .getText() .toString() ), grade           .getValue());
            infosObj.put(String.valueOf( major           .getmKeyView() .getText() .toString() ), major           .getValue());
            infosObj.put(String.valueOf( classes         .getmKeyView() .getText() .toString() ), classes         .getValue());
            infosObj.put(String.valueOf( id              .getmKeyView() .getText() .toString() ), id              .getValue());
            infosObj.put(String.valueOf( name            .getmKeyView() .getText() .toString() ), name            .getValue());
            infosObj.put(String.valueOf( area            .getmKeyView() .getText() .toString() ), area            .getValue());
            infosObj.put(String.valueOf( dormitory       .getmKeyView() .getText() .toString() ), dormitory       .getValue());
            infosObj.put(String.valueOf( isleft          .getmKeyView() .getText() .toString() ), isleft          .getValue());
            infosObj.put(String.valueOf( aim             .getmKeyView() .getText() .toString() ), aim             .getValue());
            infosObj.put(String.valueOf( reason          .getmKeyView() .getText() .toString() ), reason          .getValue());
            infosObj.put(String.valueOf( phonenumber     .getmKeyView() .getText() .toString() ), phonenumber     .getValue());
            infosObj.put(String.valueOf( emergencycontact.getmKeyView() .getText() .toString() ), emergencycontact.getValue());
            infosObj.put(String.valueOf( emergencynumber .getmKeyView() .getText() .toString() ), emergencynumber .getValue());
            infosObj.put(String.valueOf( homecontact     .getmKeyView() .getText() .toString() ), homecontact     .getValue());
            infosObj.put(String.valueOf( homenumber      .getmKeyView() .getText() .toString() ), homenumber      .getValue());
            infosObj.put(String.valueOf( isinlib         .getmKeyView() .getText() .toString() ), isinlib         .getValue());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        infosArr.put(infosObj);
        SharedPreferences pref = getSharedPreferences("infos", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isSaveInfoCheBox", true);
        editor.putString("infos", infosArr.toString());
        editor.commit();
        Log.d("ZL", "infosArr.toString():"+infosArr.toString());
        Log.d("ZL", "个人登录信息存储成功");
    }

    private void submit() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();
                Request request1 = new Request.Builder()
                        .url("http://bear.flybear.wang:8080/form")
                        .build();
                String cookie = "";
                try {
                    Response response = client.newCall(request1).execute();
                    Log.d("ZL", "first response is:"+response.toString());
                    if(response.isSuccessful()){
                        cookie = response.header("Set-Cookie").split(";")[0];
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                RequestBody form = new FormBody.Builder()
                        .add(Param. week              , String.valueOf(weekNum))
                        .add(Param. grade             , grade            .getValue())
                        .add(Param. major             , major            .getValue())
                        .add(Param. classes           , classes          .getValue())
                        .add(Param. id                , id               .getValue())
                        .add(Param. name              , name             .getValue())
                        .add(Param. area              , area             .getValue())
                        .add(Param. dormitory         , dormitory        .getValue())
                        .add(Param. isleft            , isleft           .getValue())
                        .add(Param. aim               , aim              .getValue())
                        .add(Param. reason            , reason           .getValue())
                        .add(Param. phonenumber       , phonenumber      .getValue())
                        .add(Param. emergencycontact  , emergencycontact .getValue())
                        .add(Param. emergencynumber   , emergencynumber  .getValue())
                        .add(Param. homecontact       , homecontact      .getValue())
                        .add(Param. homenumber        , homenumber       .getValue())
                        .add(Param. isinlib           , isinlib          .getValue())
                        .add(Param. leavetime         , leaveTime        .getValue())
                        .add(Param. returntime        , returnTime       .getValue())
                        .build();
                Request request = new Request.Builder()
                        .url("http://bear.flybear.wang:8080/handlemessage")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Accept-Language", "zh-CN,zh;q=0.8")
                        .header("Connection", "keep-alive")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Cookie", cookie)
                        .header("Host", "bear.flybear.wang:8080")      //Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0
                        .header("Origin", "http://bear.flybear.wang:8080")
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.108 Safari/537.36 2345Explorer/8.2.2.14352")
                        .header("Referer", "http://bear.flybear.wang:8080/form")
                        .post(form)
                        .build();
                Log.d("ZL", "second request is:"+request.toString());
                try {
                    Response response = client.newCall(request).execute();
                    Log.d("ZL", "second response is:"+response.toString());
                    String bodyStr = response.body().string();
                    Log.d("ZL", "second response body is"+bodyStr);
                    if(response.isSuccessful() && bodyStr.contains("成功")){
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
//                        Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
                    }else {
                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_query:
//                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                if(name.getValue().equals("") || id.getValue().equals("")){
                    Toast.makeText(this, "学号和姓名未填写完整！", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(MainActivity.this, QueryActivity.class);
                    intent.putExtra("username", name.getValue());
                    intent.putExtra("password", id.getValue());
                    startActivity(intent);
                }
                return true;
            case R.id.action_Admin:
                Intent intent = new Intent(MainActivity.this, AdminLoginActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
