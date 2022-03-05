package com.elmenture.luuk.utils;

public interface NetworkCallback {
    void onResponse(int responseCode, String response);

    void onError(int responseCode, String error);
}
