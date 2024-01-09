package com.ycompany.yelectronics.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext

object Extensions {
    fun toast(msg: String, context: Context){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun Activity.cardXXGen(card: String):String{
        var size:Int = card.length
        var CardG: String = ""

        for(i in card){
            size--
            if(size < 4){
                CardG.plus(i)
            }
        }
        return "**** **** **** $CardG"
    }
}