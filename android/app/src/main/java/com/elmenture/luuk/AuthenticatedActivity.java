package com.elmenture.luuk;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.elmenture.luuk.base.BaseActivity;
import com.elmenture.luuk.ui.signin.SignInActivity;

import userdata.User;

public class AuthenticatedActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!User.hasSignedInUser()) {
            logUtils.w("No user logged in");
            logout();
        }
    }

    protected void logout() {
        User.getCurrent().logout();
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}
