package com.luuk.common.network.interceptors;

import com.luuk.common.utils.NetUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by kevin on 26/10/17.
 */

public class ConnectivityInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            if (NetUtils.Companion.isNetworkAvailable()) {
                Request request = chain.request();
                return chain.proceed(request);
            } else {
                throw new NoConnectivityException();
            }
        } catch (NoConnectivityException e) {
            throw e;
        } catch (SocketTimeoutException e) {
            Response.Builder builder = new Response.Builder();
            builder.message("Time out Exception occurred!").body(ResponseBody.create(null, "")).code(408).request(chain.request()).protocol(Protocol.HTTP_1_1);
            return builder.build();
        } catch (Exception e) {
            Response.Builder builder = new Response.Builder();
            builder.message("Time out Exception occured!").body(ResponseBody.create(null, "")).code(400).request(chain.request()).protocol(Protocol.HTTP_1_1);
            return builder.build();
        }
    }

    public static class NoConnectivityException extends IOException {

        @Override
        public String getMessage() {
            return "No network available, please check your WiFi, Ethernet or Data connection";
        }
    }
}
