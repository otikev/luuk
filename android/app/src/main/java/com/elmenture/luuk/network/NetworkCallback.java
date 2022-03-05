package com.elmenture.luuk.network;

public interface NetworkCallback {
    void onResponse(int responseCode, String response);

    void onError(String error);
}
