package com.elmenture.luuk.base;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.elmenture.luuk.R;
import com.elmenture.luuk.User;
import com.elmenture.luuk.utils.LogUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public abstract class BaseActivity extends AppCompatActivity implements BaseActivityView {

    protected LogUtils logUtils = new LogUtils(this.getClass());
    protected GoogleSignInClient mGoogleSignInClient;
    protected CallbackManager callbackManager;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureGoogleSignIn();
        configureFacebookSignin();
    }

    private void configureFacebookSignin() {
        callbackManager = CallbackManager.Factory.create();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn) {
            if(!User.hasSignedInUser()){
                LoginManager.getInstance().logOut();
            }
        }
    }

    @Override
    public void addFragment(@NonNull Fragment fragment, @NonNull String tag){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.layout_fragment_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag).commitAllowingStateLoss();
    }

    @Override
    public void replaceFragment(@NonNull Fragment fragment, @NonNull String tag){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layout_fragment_container, fragment, tag).commitAllowingStateLoss();
    }

    private void configureGoogleSignIn() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            if(!User.hasSignedInUser()){
                mGoogleSignInClient.signOut();
            }
        }
    }
}
