package com.elmenture.luuk;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import network.RestAdapterBuilderFactory;
import utils.NetUtils;

public class LuukApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        RestAdapterBuilderFactory.APP_ENDPOINT = NetUtils.BASE_URL;
        NetUtils.Companion.setContext(getApplicationContext());
    }

}
