package com.ycompany.yelectronics.ui.home

import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.ycompany.yelectronics.ui.R
import com.ycompany.yelectronics.ui.base.BaseActivity
import com.ycompany.yelectronics.ui.cart.PaymentCompletedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity(), PaymentResultWithDataListener {

    private lateinit var paymentCompletedListener: PaymentCompletedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = Navigation.findNavController(this, R.id.host_fragment)
        NavigationUI.setupWithNavController(bottomNavigation, navController)

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            clearAllBackStack()
            when (item.itemId) {
                R.id.homeFragment -> {
                    Navigation.findNavController(this@HomeActivity, R.id.host_fragment)
                        .navigate(R.id.homeFragment)
                }

                R.id.cartFragment -> {
                    Navigation.findNavController(this@HomeActivity, R.id.host_fragment)
                        .navigate(R.id.cartFragment)
                }

                R.id.favoriteFragment -> {
                    Navigation.findNavController(this@HomeActivity, R.id.host_fragment)
                        .navigate(R.id.favoriteFragment)
                }

                R.id.profileFragment -> {
                    Navigation.findNavController(this@HomeActivity, R.id.host_fragment)
                        .navigate(R.id.profileFragment)

                }
            }
            true
        }

    }

    private fun clearAllBackStack() {
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }
    }

    fun setPaymentListener(listener: PaymentCompletedListener) {
        paymentCompletedListener = listener
    }

    override fun onPaymentSuccess(var1: String?, var2: PaymentData?) {
        paymentCompletedListener.onPaymentSuccess(var1, var2)
    }

    override fun onPaymentError(var1: Int, var2: String?, var3: PaymentData?) {
        paymentCompletedListener.onPaymentError(var1, var2, var3)
    }
}