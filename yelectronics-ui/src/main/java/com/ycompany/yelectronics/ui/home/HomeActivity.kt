package com.ycompany.yelectronics.ui.home

import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ycompany.yelectronics.ui.R
import com.ycompany.yelectronics.ui.base.BaseActivity
import com.ycompany.yelectronics.ui.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        bottomNavigationView = findViewById(R.id.bottomNavMenu)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        loadFragment(HomeFragment.getInstance(), false)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.homeMenu -> {
                loadFragment(HomeFragment.getInstance(), false)
                return true
            }

            R.id.shopMenu -> {
//
                return true
            }

            R.id.bagMenu -> {
//                val fragment = BagFragment()
//                supportFragmentManager.beginTransaction().replace(R.id.nav_fragment, fragment, fragment.javaClass.simpleName)
//                    .commit()
                return true
            }

            R.id.favMenu -> {
//                val fragment = FavFragment()
//                supportFragmentManager.beginTransaction().replace(R.id.nav_fragment, fragment, fragment.javaClass.simpleName)
//                    .commit()
                return true
            }

            R.id.profileMenu -> {
                loadFragment(ProfileFragment.getInstance(), false)
                return true
            }
        }
        return false
    }
}