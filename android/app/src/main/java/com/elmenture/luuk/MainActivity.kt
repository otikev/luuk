package com.elmenture.luuk

import android.os.Bundle

class MainActivity : AuthenticatedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}