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

    public static final String DARAJA_STK_URL = System.getenv("DARAJA_STK_URL");//"https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest";
    public static final String DARAJA_AUTH_URL = System.getenv("DARAJA_AUTH_URL");//"https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials";
    public static final String CONSUMER_KEY = System.getenv("CONSUMER_KEY");//"G7v6VB93Ax1f01hvFyokdHT679GMSx7Y";
    public static final String CONSUMER_SECRET = System.getenv("CONSUMER_SECRET");//"QD8uVEuhE5GQKHzp";
    public static final String STK_TRANSACTION_TYPE = System.getenv("STK_TRANSACTION_TYPE");//"CustomerPayBillOnline";
    public static final String STK_ACCOUNT_REFERENCE = System.getenv("STK_ACCOUNT_REFERENCE");//"LuukAtMe";
    public static final String secret = CONSUMER_KEY + ":" + CONSUMER_SECRET;
    public static final String DARAJA_PASSKEY = System.getenv("DARAJA_PASSKEY");//"bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
    public static final String BASE_URL = System.getenv("BASE_URL");//"https://7f2e-41-80-23-108.in.ngrok.io/";
    public static final String BUSINESS_SHORT_CODE = System.getenv("BUSINESS_SHORT_CODE");//"174379";

}
