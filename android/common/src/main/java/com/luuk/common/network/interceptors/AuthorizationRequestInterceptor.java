package com.luuk.common.network.interceptors;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthorizationRequestInterceptor implements Interceptor {
    private String authKey;

    /**
     * Probably a good idea to get the auth key from the user class instead
     * of passing it through this constructor. @komal @Brian?
     *
     * @param authKey
     */
    public AuthorizationRequestInterceptor(String authKey) {
        this.authKey = authKey;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        builder.addHeader("Authorization", authKey);
        return chain.proceed(builder.build());
    }
}
