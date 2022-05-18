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

    public static final String DARAJA_STK_URL = System.getenv("DARAJA_STK_URL");
    public static final String DARAJA_AUTH_URL = System.getenv("DARAJA_AUTH_URL");
    public static final String CONSUMER_KEY = System.getenv("CONSUMER_KEY");
    public static final String CONSUMER_SECRET = System.getenv("CONSUMER_SECRET");
    public static final String STK_TRANSACTION_TYPE = System.getenv("STK_TRANSACTION_TYPE");
    public static final String STK_ACCOUNT_REFERENCE = System.getenv("STK_ACCOUNT_REFERENCE");
    public static final String secret = CONSUMER_KEY + ":" + CONSUMER_SECRET;
    public static final String DARAJA_PASSKEY = System.getenv("DARAJA_PASSKEY");
    public static final String BASE_URL = "https://2b43-41-80-25-101.in.ngrok.io/";//System.getenv("BASE_URL");
    public static final String BUSINESS_SHORT_CODE = System.getenv("BUSINESS_SHORT_CODE");

    public static final String STK_CALLBACK_URL = BASE_URL + "order/payment-confirmed";
    public static final String COUNTRY_CODE ="254";

}
