package com.elmenture.luuk.ui.signin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.ViewModelProvider;

import com.elmenture.luuk.R;
import com.elmenture.luuk.base.BaseActivity;
import com.elmenture.luuk.ui.main.MainActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import models.SignInResponse;
import userdata.User;
import utils.SecureUtils;


//https://firebase.google.com/docs/auth/android/facebook-login
//https://firebase.google.com/docs/auth/android/google-signin
//https://developers.google.com/identity/sign-in/android/backend-auth
public class SignInActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {
    private Button btnGoogle;
    private Button btnFacebook;
    private LoginButton btnFacebookLoginHidden;

    private static final int RC_SIGN_IN = 10001;
    private boolean showOneTapUI = true;
    private LinearLayoutCompat socialLayout;
    private SignInViewModel signInViewModel;
    String idToken;
    private FirebaseAuth firebaseAuth;
    GoogleApiClient googleApiClient;
    CallbackManager callbackManager = CallbackManager.Factory.create();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);
        initView();
        initFirebaseAuth();
        registerFaceBookCallBacks();
        setupEventListeners();
        observeLiveData();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void initFirebaseAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut(); //You are in the Sign in screen, so ensure all social logins are invalidated as we will login again in this screen

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void initView() {
        socialLayout = findViewById(R.id.socialLayout);
        socialLayout.setVisibility(View.VISIBLE);
        btnFacebook = findViewById(R.id.btnFacebook);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnFacebookLoginHidden = findViewById(R.id.btnFacebookLoginHidden);
        btnFacebookLoginHidden.setReadPermissions("email", "public_profile");

        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
    }

    private void setupEventListeners() {
        View.OnClickListener googleOnClickListener = view -> {
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(intent, RC_SIGN_IN);
        };

        btnGoogle.setOnClickListener(googleOnClickListener);

        btnFacebook.setOnClickListener(view -> {
            btnFacebookLoginHidden.performClick();
        });
    }


    private void observeLiveData() {
        signInViewModel.getSignUpApiState().observe(this, baseApiState -> {
            if (baseApiState != null) {
                if (baseApiState.isSuccessful()) {
                    SignInResponse res = (SignInResponse) baseApiState.getData();
                    if (res.isSuccessful()) {
                        User.getCurrent().setUserDetails(res);
                        SecureUtils.setUserSessionKey(SignInActivity.this, res.getSessionKey());
                        logUtils.i("Signin success");
                        setResult(RESULT_OK);
                        showMainScreen();
                    } else {
                        signinFailed();
                    }
                } else {
                    signinFailed();
                }
            }
        });
    }


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
            verifyGoogleTokenWithBackend(account.getIdToken());
        } catch (ApiException e) {
            logUtils.e(e);
        }
    }

    private void showMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    void verifyGoogleTokenWithBackend(String accessToken) {
        signinStarted();

        HashMap<String, String> nameValuePairs = new HashMap<>();
        nameValuePairs.put("idToken", accessToken);

        signInViewModel.signInWithGoogle(nameValuePairs);
    }

    void verifyFaceBookTokenWithBackend(String email, String accessToken) {
        signinStarted();

        HashMap<String, String> nameValuePairs = new HashMap<>();
        nameValuePairs.put("idToken", accessToken);
        nameValuePairs.put("email", email);

        signInViewModel.signInWithFacebook(nameValuePairs);
    }

    void registerFaceBookCallBacks() {
        btnFacebookLoginHidden.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                logUtils.d("facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
                setResult(RESULT_CANCELED);
            }

            @Override
            public void onError(FacebookException exception) {
                logUtils.e(exception);
            }
        });

    }

    private void handleFacebookAccessToken(AccessToken token) {
        logUtils.d("handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            logUtils.d("signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String displayName = user.getDisplayName();
                            verifyFaceBookTokenWithBackend(user.getEmail(), token.getToken());
                        } else {
                            logUtils.w("signInWithCredential:failure -> " + task.getException().getMessage());
                            signinFailed();
                        }
                    }
                });
    }

    private void signinFailed() {
        socialLayout.setVisibility(View.VISIBLE);
        Toast.makeText(SignInActivity.this, "Authentication failed.",
                Toast.LENGTH_SHORT).show();
    }

    private void signinStarted() {
        //Notify the user
        socialLayout.setVisibility(View.GONE);
    }

}
