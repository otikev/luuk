package com.elmenture.luuk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

public class SignInActivity extends BaseActivity {
    private CallbackManager callbackManager;
    private SignInButton btnGoogle;
    private LoginButton btnFacebook;

    private static final String EMAIL = "email";
    private static final String AUTH_TYPE = "rerequest";
    private static final int RC_SIGN_IN = 10001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btnFacebook = findViewById(R.id.btnFacebook);
        btnFacebook.setReadPermissions(Arrays.asList(EMAIL));
        btnFacebook.setAuthType(AUTH_TYPE);

        btnGoogle = findViewById(R.id.btnGoogle);
        btnGoogle.setSize(SignInButton.SIZE_STANDARD);

        callbackManager = CallbackManager.Factory.create();
        setupClickListeners();
    }

    private void setupClickListeners() {
        btnGoogle.setOnClickListener(googleOnClickListener);

        // Callback registration
        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                AccessToken accessToken = loginResult.getAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

                if(isLoggedIn){
                    User.setFacebookUser(loginResult);
                    setResult(RESULT_OK);
                    showMainScreen();
                }
            }

            @Override
            public void onCancel() {
                // App code
                setResult(RESULT_CANCELED);
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    View.OnClickListener facebookOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    };

    View.OnClickListener googleOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            logUtils.e(e);
        }
    }

    private void updateUI(GoogleSignInAccount googleSignInAccount) {
        User.setGoogleUser(googleSignInAccount);
        showMainScreen();
    }

    private void showMainScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
