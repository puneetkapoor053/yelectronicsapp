package com.ycompany.yelectronics.network.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CartDao {

    @Query("SELECT * FROM cart_items order by rowId desc")
    fun getAllCartItems(): List<CartEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg product: CartEntity)

    @Delete
    suspend fun delete(product: CartEntity)

    @Update
    suspend fun update(vararg product: CartEntity)

    @Query("SELECT * FROM cart_items WHERE product_ID = :productId")
    suspend fun getCartItem(productId: String) : CartEntity?
}