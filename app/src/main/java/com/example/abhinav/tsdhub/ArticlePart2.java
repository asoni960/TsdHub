package com.example.abhinav.tsdhub;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import  org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.security.cert.Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

import static com.example.abhinav.tsdhub.R.id.none;


/**
 * Created by abhinav on 07-11-2016.
 */

public class ArticlePart2 extends AppCompatActivity{
    String url = "http://stackoverflow.com/";
    @SuppressLint("JavascriptInterface")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articlepart2);

       /* webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                webview.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('topbar')[0].style.display='none';"+"})()");
                webview.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('header')[0].style.display='none';"+"})()");
               // webview.loadUrl("javascript:document.getElementById(\"topnav\").setAttribute(\"style\",\"display:none;\");");
            }
        });*/

        new LoadWebPage().execute();
    }

    private class LoadWebPage extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
                         /*Your background work here */
            Document doc = null;
            try {
                doc = Jsoup.connect(url).timeout(10000).get();
                //doc.getElementsByClass("topbar").first().remove();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Element ele = doc.select("div.entry-content-wrapper").first();
            String html = ele.toString();


            return html;
        }

        @Override
        protected void onPostExecute(String result) {
                       /* Called when work in doInBackground() is finished.*/
                       /* So you can write UI here i.e. update your UI after finishing doInBackground*/
            WebView webView = (WebView) findViewById(R.id.articleWebView);
            webView.getSettings().setJavaScriptEnabled(true);
            String mime = "text/html";
            String encoding = "utf-8";
            webView.loadData(result, mime, encoding);
        }
    }
}

