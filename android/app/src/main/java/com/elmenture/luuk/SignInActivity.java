package com.elmenture.luuk;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.os.HandlerCompat;

import com.elmenture.luuk.network.Network;
import com.elmenture.luuk.network.NetworkCallback;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

//https://developers.google.com/identity/sign-in/android/backend-auth
public class SignInActivity extends BaseActivity {
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
        btnFacebook.setPermissions(Arrays.asList(EMAIL));
        btnFacebook.setAuthType(AUTH_TYPE);

        btnGoogle = findViewById(R.id.btnGoogle);
        btnGoogle.setSize(SignInButton.SIZE_STANDARD);
        TextView textView = (TextView) btnGoogle.getChildAt(0);
        textView.setText("Continue with Google");

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

                if (isLoggedIn) {
                    verifyFacebookTokenWithBackend(accessToken);
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
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            verifyGoogleTokenWithBackend(account);
        } catch (ApiException e) {
            logUtils.e(e);
        }
    }

    private void showMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    void verifyFacebookTokenWithBackend(AccessToken accessToken) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("userToken", accessToken.getToken()));

        Network.INSTANCE.post("auth/facebooksignin", nameValuePairs, new NetworkCallback() {
            @Override
            public void onResponse(int responseCode, String response) {
                if (responseCode == 200) {
                    User.setFacebookUser(Profile.getCurrentProfile());
                    setResult(RESULT_OK);
                    showMainScreen();
                }
            }

            @Override
            public void onError(String error) {
                logUtils.w(error);
            }
        }, HandlerCompat.createAsync(Looper.getMainLooper()));
    }

    void verifyGoogleTokenWithBackend(GoogleSignInAccount account) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("idToken", account.getIdToken()));

        Network.INSTANCE.post("auth/googlesignin", nameValuePairs, new NetworkCallback() {
            @Override
            public void onResponse(int responseCode, String response) {
                if (responseCode == 200) {
                    try {
                        //{"success":true,"isNewAccount":false,"authToken":"somevalue"}
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("success")) {
                            logUtils.i("Google signin success");
                            Network.INSTANCE.setNetworkAuthToken(jsonObject.getString("authToken"));
                            User.setGoogleUser(account);
                            showMainScreen();
                        } else {
                            logUtils.w("Google signin failed");
                            signinFailed();
                        }
                    } catch (JSONException e) {
                        logUtils.e(e);
                    }
                } else {
                    signinFailed();
                }
            }

            @Override
            public void onError(String error) {
                logUtils.w(error);
            }
        }, HandlerCompat.createAsync(Looper.getMainLooper()));
    }

    private void signinFailed() {
        //Notify the user
    }
}
