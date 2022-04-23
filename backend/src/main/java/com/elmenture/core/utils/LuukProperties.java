package com.elmenture.core.utils;

/**
 * Created by otikev on 06-Mar-2022
 */

public class LuukProperties {
    public static String facebookAppId = System.getenv("FACEBOOK_APP_ID");
    public static String facebookAppSecret = System.getenv("FACEBOOK_APP_SECRET");
    public static String googleClientId = System.getenv("GOOGLE_SERVER_CLIENT_ID");
    public static String googleClientSecret = System.getenv("GOOGLE_SERVER_CLIENT_SECRET");
    public static String amazonS3AccessKeyId = System.getenv("S3_ACCESS_KEY_ID");
    public static String amazonS3SecretKeyId = System.getenv("S3_SECRET_KEY_ID");
    public static Boolean enableEmails = Boolean.valueOf(System.getenv("LUUK_ENABLE_EMAILS"));
    public static String smtpPassword = System.getenv("LUUK_MAIL_PASSWORD");
    public static String smtpUsername = System.getenv("LUUK_MAIL_USERNAME");
}
