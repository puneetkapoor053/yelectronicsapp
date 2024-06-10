package com.ycompany.yelectronics.utils

import android.content.Context
import android.widget.Toast

object Extensions {
    fun toast(msg: String, context: Context){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}