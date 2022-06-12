package com.elmenture.luuk;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.FirebaseApp;

import network.RestAdapterBuilderFactory;
import utils.NetUtils;

public class LuukApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        RestAdapterBuilderFactory.APP_ENDPOINT = getResources().getString(R.string.base_url);

        NetUtils.Companion.setContext(getApplicationContext());
    }
}
