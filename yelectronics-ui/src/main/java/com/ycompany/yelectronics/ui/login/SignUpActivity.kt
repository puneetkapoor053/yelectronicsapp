package com.ycompany.yelectronics.ui.login

import android.os.Bundle
import com.ycompany.yelectronics.ui.base.BaseActivity
import com.ycompany.yelectronics.ui.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity)
        loadFragment(SignUpFragment.getInstance(), false)
    }
}