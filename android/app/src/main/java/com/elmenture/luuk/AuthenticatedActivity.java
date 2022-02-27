package com.elmenture.luuk;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class AuthenticatedActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(User.getCurrent() == null){
            logUtils.w("No user logged in");
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
