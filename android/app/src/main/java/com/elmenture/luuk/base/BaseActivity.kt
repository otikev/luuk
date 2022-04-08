package com.elmenture.luuk.base

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.elmenture.luuk.R
import com.elmenture.luuk.base.repositories.RemoteRepository
import com.elmenture.luuk.utils.LogUtils
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.CallbackManager.Factory.create
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import userdata.User
import views.ProgressDialog


abstract class BaseActivity : AppCompatActivity(), BaseActivityView {
    @JvmField
    protected var logUtils = LogUtils(this.javaClass)

    @JvmField
    protected var mGoogleSignInClient: GoogleSignInClient? = null

    @JvmField
    protected var callbackManager: CallbackManager? = null
    private var loader: DialogFragment? = null
    private var snackbar: Snackbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureGoogleSignIn()
        configureFacebookSignin()
        observeGlobalEvents()
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

    fun clearBackStack() {
        suspend {
            while (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            }
        }
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

    private fun observeGlobalEvents() {
        RemoteRepository.blockUserInteraction.observe(this) { blockUser ->
            handleProgressDialog(blockUser)
        }
    }

    private fun handleProgressDialog(blockUser: Boolean) {
        if (blockUser) {
            showProgressDialog()
        } else {
            hideProgressDialog()
        }

    }

    private fun showProgressDialog() {
        if (loader == null)
            loader = ProgressDialog.newInstance()
        if (loader!!.isAdded) {
            loader?.dismissAllowingStateLoss()
        }
        loader?.show(supportFragmentManager, ProgressDialog.TAG)
    }

    private fun hideProgressDialog() {
        loader?.dismissAllowingStateLoss()
    }

    fun showMessage(view: View, isSuccessFullAction: Boolean, message: String? = null) {
        snackbar?.dismiss()
        var snackMsg = message
        if(snackMsg.isNullOrEmpty()){
            snackMsg = "Action Failed. Try Again"
            if (isSuccessFullAction) {
                snackMsg = "Action Completed Successfully"
            }
        }

        snackbar = Snackbar.make(view, snackMsg, Snackbar.LENGTH_INDEFINITE)
        val textView = snackbar?.view?.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView?.gravity = Gravity.CENTER
        textView?.setLines(2);
        textView?.setTextColor(resources.getColor(R.color.luuk_yellow))
        snackbar?.setAction("OK") { snackbar?.dismiss() }
        snackbar?.show()
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