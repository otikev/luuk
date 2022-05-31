package com.elmenture.core.utils;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.encoders.Base64;

import java.io.UnsupportedEncodingException;

public class MiscUtils {

    public static String getUserTokenFromHeader(String auth){
        byte[] decoded;
        String authorization;
        try {
            decoded = Base64.decode(auth.getBytes("utf-8"));
            authorization = IOUtils.toString(decoded, "utf-8");
            if (!authorization.contains(":") || authorization.split(":").length != 3) {
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return authorization.split(":")[2];
    }


}
