package com.leaveschool.query;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.leaveschool.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private WebSettings webSettings;
    private String url = "http://bear.flybear.wang:8080/searchone";
//    private String url = "https://www.baidu.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        findAllViews();
        init();
    }

    private void findAllViews() {
        webView = (WebView) findViewById(R.id.queryPage);

    }

    private void init() {
        webView.setWebViewClient( new MyWebViewClient());

        webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl(url);
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
