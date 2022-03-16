package com.elmenture.luuk.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.navigation.ui.AppBarConfiguration
import com.elmenture.luuk.User
import com.elmenture.luuk.base.BaseActivity
import com.elmenture.luuk.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        object : CountDownTimer(3000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                if (User.hasSignedInUser()) {
                    startMainScreen()
                } else {
                    startLoginScreen()
                }
            }
        }.start()
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