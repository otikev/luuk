package com.elmenture.luuk.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.elmenture.luuk.base.BaseActivity
import com.elmenture.luuk.base.repositories.LocalRepository
import com.elmenture.luuk.databinding.ActivitySplashBinding
import com.elmenture.luuk.ui.main.MainActivity
import com.elmenture.luuk.ui.signin.SignInActivity
import models.SignInResponse
import userdata.User
import utils.SecureUtils

class SplashActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivitySplashBinding

    lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        val sessionKey = SecureUtils.getUserSessionKey(this@SplashActivity)
        if (sessionKey == null) {
            startLoginScreen()
        } else {
            observeLiveData()
            User.getCurrent().sessionKey = sessionKey
            splashViewModel.fetchUserDetails()
        }
    }

    private fun observeLiveData() {
        splashViewModel.userDetailsApiState.observe(this) {
            it?.let {
                if (it.isSuccessful) {
                    LocalRepository.updateUserDetails(it.data as SignInResponse)
                    User.getCurrent().setUserDetails(it.data as SignInResponse)
                    startMainScreen()
                } else {
                    logUtils.w("Auth failed : " + it.responseCode)
                    SecureUtils.setUserSessionKey(this@SplashActivity, null)
                    startLoginScreen()
                }
            }
        }
    }

    private fun startLoginScreen() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}