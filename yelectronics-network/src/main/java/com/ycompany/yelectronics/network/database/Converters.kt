package com.ycompany.yelectronics.network.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.ycompany.yelectronics.network.dto.Orders

class Converters {

    @TypeConverter
    fun listToJson(value: List<Orders>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Orders>::class.java).toList()
}