package com.ycompany.yelectronics.network.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartEntity(
    @ColumnInfo(name = "product_Name") var name: String,
    @ColumnInfo(name = "product_Quantity") var quantity: Int,
    @ColumnInfo(name = "product_Price") var price: Int,
    @PrimaryKey @ColumnInfo(name = "product_ID") var productId: String,
    @ColumnInfo(name = "product_Image") var productImage: String
)