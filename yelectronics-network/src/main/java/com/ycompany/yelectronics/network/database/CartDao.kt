package com.ycompany.yelectronics.network.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CartDao {

    @Query("SELECT * FROM cart_items order by rowId desc")
    suspend fun getAllCartItems(): List<CartEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg product: CartEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(vararg order: OrdersListEntity)

    @Query("SELECT * FROM orders_list ORDER BY date_of_order DESC")
    suspend fun getAllOrders(): List<OrdersListEntity>

    @Delete
    suspend fun delete(product: CartEntity)

    @Query("DELETE FROM cart_items")
    suspend fun deleteAll()

    @Query("DELETE FROM orders_list")
    suspend fun deleteAllOrders()

    @Update
    suspend fun update(vararg product: CartEntity)

    @Query("SELECT * FROM cart_items WHERE product_ID = :productId")
    suspend fun getCartItem(productId: String) : CartEntity?
}