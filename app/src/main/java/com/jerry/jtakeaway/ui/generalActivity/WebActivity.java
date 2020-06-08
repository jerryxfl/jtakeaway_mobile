package com.jerry.jtakeaway.ui.generalActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.utils.LogPrint;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import butterknife.BindView;

@SuppressWarnings("all")
public class WebActivity extends BaseActivity {
    @BindView(R.id.return_aib)
    AniImgButton return_aib;
    @BindView(R.id.menu_aib)
    AniImgButton menu_aib;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.top)
    View top;
    @BindView(R.id.load_process)
    ProgressBar load_process;
    @BindView(R.id.title_tv)
    TextView title_tv;
    private String url;

    @Override
    public int getLayout() {
        return R.layout.activity_web;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void InitView() {
        Intent intent = getIntent();
        url = intent.getStringExtra("URL");
        LogPrint.print("网页地址" + url, LogPrint.logType.Waring);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) top.getLayoutParams();
        params.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(params);


        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        webView.loadUrl(url);
    }

    @Override
    public void InitData() {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void InitListener() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                //设定加载结束的操作
                if(load_process!=null)load_process.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //设定加载开始的操作
                if(load_process!=null)load_process.setVisibility(View.VISIBLE);
            }
            @Override
            public void onLoadResource(WebView view, String url) {
                //设定加载资源的操作
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                view.loadUrl("file:///android_asset/web/error.html");
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();    //表示等待证书响应
                // handler.cancel();      //表示挂起连接，为默认方式
                // handler.handleMessage(null);    //可做其他处理
            }

        });


        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                //获得页面标题
                if(title_tv!=null)title_tv.setText(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(load_process!=null)load_process.setProgress(newProgress);
            }



        });

        return_aib.setOnClickListener(v -> {
            if(webView.canGoBack())webView.goBack();
            else finish();
        });


        menu_aib.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){

                }
                return false;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(webView.canGoBack())webView.goBack();
            else finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void destroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
    }

}
