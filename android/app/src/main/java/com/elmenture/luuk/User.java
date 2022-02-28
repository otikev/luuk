package com.elmenture.luuk;

import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class User {

    private String email;
    private GoogleSignInAccount googleAccount;
    private LoginResult facebookLoginResult;

    private static User user;

    public static User getCurrent() {
        return User.user;
    }

    public String getEmail() {
        return email;
    }

    public static void setGoogleUser(GoogleSignInAccount account) {
        User user = new User();
        user.email = account.getEmail();
        User.user = user;

        user.googleAccount = account;
    }

    public static void setFacebookUser(LoginResult loginResult) {
        User user = new User();
        User.user = user;
        user.facebookLoginResult = loginResult;
    }

    public void logout(){

    }
}
