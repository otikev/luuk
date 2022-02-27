package com.elmenture.luuk;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class User {

    private String email;
    private GoogleSignInAccount googleAccount;

    private static User user;

    public static User getCurrent() {
        return User.user;
    }

    public String getEmail() {
        return email;
    }

    public static void setGoogleUser(GoogleSignInAccount account){
        User user = new User();
        user.email = account.getEmail();
        User.user = user;

        user.googleAccount = account;
    }
}
