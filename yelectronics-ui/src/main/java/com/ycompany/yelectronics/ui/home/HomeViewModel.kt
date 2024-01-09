package com.ycompany.yelectronics.ui.home

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.ycompany.yelectronics.utils.Constants
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val sharedPreferences: SharedPreferences) :
    ViewModel() {

    fun getUserName(): String {
        return if (sharedPreferences.getString(Constants.PREF_USERNAME, null)
                .equals(Constants.PREF_GUEST_USERNAME)
        ) {
            "Guest"
        } else {
            sharedPreferences.getString(Constants.PREF_USERNAME, null).toString()
        }
    }
}