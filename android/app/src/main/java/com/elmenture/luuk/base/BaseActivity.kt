package com.elmenture.luuk.base

import com.facebook.CallbackManager.Factory.create
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.facebook.CallbackManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.elmenture.luuk.R
import com.elmenture.luuk.utils.LogUtils
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.kokonetworks.kokosasa.base.BaseFragment
import userdata.User

abstract class BaseActivity : AppCompatActivity(), BaseActivityView {
    @JvmField
    protected var logUtils = LogUtils(this.javaClass)
    @JvmField
    protected var mGoogleSignInClient: GoogleSignInClient? = null
    @JvmField
    protected var callbackManager: CallbackManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureGoogleSignIn()
        configureFacebookSignin()
    }

    private fun configureFacebookSignin() {
        callbackManager = create()
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        if (isLoggedIn) {
            if (!User.hasSignedInUser()) {
                LoginManager.getInstance().logOut()
            }
        }
    }

    override fun addFragment(fragment: Fragment, tag: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.layout_fragment_container, fragment, tag)
        fragmentTransaction.addToBackStack(tag).commitAllowingStateLoss()
    }

    override fun replaceFragment(fragment: Fragment, tag: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout_fragment_container, fragment, tag)
            .commitAllowingStateLoss()
    }

    private fun configureGoogleSignIn() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            if (!User.hasSignedInUser()) {
                mGoogleSignInClient!!.signOut()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val fragment = supportFragmentManager.findFragmentById(R.id.layout_fragment_container)
        fragment?.let {
            if (it is BaseFragment)
                it.onResumeFromBackstack()
        }
    }
}