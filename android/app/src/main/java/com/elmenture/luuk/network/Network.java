package com.elmenture.luuk.network;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import com.elmenture.luuk.utils.LogUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class Network {
    public static Network INSTANCE = new Network();
    private static ExecutorService executorService = Executors.newFixedThreadPool(4);

    private static final String BASE_URL = "http://192.168.0.110:8080/";
    private LogUtils logUtils = new LogUtils(this.getClass());
    private Network(){}

    private String networkAuthToken;

    public String getNetworkAuthToken() {
        return networkAuthToken;
    }

    public void setNetworkAuthToken(String networkAuthToken) {
        this.networkAuthToken = networkAuthToken;
    }

    public void post(String endpoint, List<NameValuePair> nameValuePairs, final NetworkCallback callback, final Handler resultHandler){
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(BASE_URL+endpoint);
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);
                    int statusCode = response.getStatusLine().getStatusCode();
                    final String responseBody = EntityUtils.toString(response.getEntity());

                    resultHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResponse(statusCode,responseBody);
                        }
                    });
                }catch (IOException e) {
                    logUtils.e("Error sending ID token to backend.", e);
                    resultHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(e.getMessage());
                        }
                    });
                }
            }
        });
    }
}
