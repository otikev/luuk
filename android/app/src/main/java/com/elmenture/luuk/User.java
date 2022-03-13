package com.elmenture.luuk;

public class User {

    private SignInResponse signInResponse;

    private static User user = new User();

    public static User getCurrent() {
        return User.user;
    }

    public void setSignInResponse(SignInResponse signInResponse) {
        this.signInResponse = signInResponse;
    }

    public static boolean hasSignedInUser() {
        return user.signInResponse != null;
    }

    public void logout() {
        signInResponse = null;
    }
}
