package com.elmenture.luuk.utils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cz.msebera.android.httpclient.NameValuePair;

public class NetUtils {
    static ExecutorService executorService = Executors.newFixedThreadPool(4);

    public static final String BASE_URL = "http://192.168.0.101:8080/";

    public static void post(List<NameValuePair> nameValuePairs, NetworkCallback callback){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                for(NameValuePair pair : nameValuePairs){

                }
            }
        });
    }
}
