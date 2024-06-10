package com.ycompany.yelectronics.network.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite_items")
    suspend fun getAllFavoriteProducts(): List<FavoriteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteProduct(favorite: FavoriteEntity)

    @Query("DELETE FROM favorite_items WHERE productId = :favoriteProductId")
    suspend fun removeFavoriteProduct(favoriteProductId: String)

    @Query("DELETE FROM favorite_items")
    suspend fun deleteAllFavoritesProducts()
}