package com.elmenture.luuk;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.luuk.common.network.RestAdapterBuilderFactory;
import com.luuk.common.utils.NetUtils;

public class LuukApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        RestAdapterBuilderFactory.APP_ENDPOINT = NetUtils.LOCAL_TEST_URL;
        NetUtils.Companion.setContext(getApplicationContext());
    }

}
