package com.ycompany.yelectronics.splash

import android.annotation.SuppressLint
import android.os.Bundle
import com.ycompany.yelectronics.ui.base.BaseActivity
import com.ycompany.yelectronics.ui.R
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity)
        setStatusBarColor(R.color.black)
        loadFragment(SplashFragment.getInstance(), true)
    }
}