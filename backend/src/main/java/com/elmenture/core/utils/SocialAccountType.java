package com.elmenture.core.utils;

public enum SocialAccountType {
    FACEBOOK("facebook"),
    GOOGLE("google");

    private String value;

    SocialAccountType(String val) {
        value = val;
    }

    public String value(){
        return value;
    }
}
