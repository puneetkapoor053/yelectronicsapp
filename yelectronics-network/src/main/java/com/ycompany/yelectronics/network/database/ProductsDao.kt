package com.ycompany.yelectronics.network.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductsDao {

    @Query("SELECT * FROM product_items")
    suspend fun getAllNewProducts(): List<ProductsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNewProducts(list: List<ProductsEntity>)

    @Query("DELETE FROM product_items")
    fun deleteAllNewProducts()
}