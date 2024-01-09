package com.ycompany.yelectronics.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.ycompany.yelectronics.ui.R

open class BaseActivity : AppCompatActivity(), NavigationHandler {

    private var mFragmentManager: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        mFragmentManager = supportFragmentManager
    }

    override fun loadFragment(fragment: Fragment, isAddToBackStack: Boolean) {
        val mFragmentTransaction: FragmentTransaction =
            mFragmentManager?.beginTransaction() ?: supportFragmentManager.beginTransaction()

        mFragmentTransaction.add(R.id.baseContainer, fragment)

        if (isAddToBackStack) {
            mFragmentTransaction.addToBackStack(fragment.tag)
        }
        mFragmentTransaction.commit()
    }

    /**
     * Set Status Bar Color
     *
     * @param color color to be set
     */
    open fun setStatusBarColor(color: Int) {
        window.statusBarColor = ContextCompat.getColor(this, color)
    }
}