package com.ycompany.yelectronics.network.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductHighlightDao {

    @Query("SELECT * FROM product_highlight_items")
    fun getAllProductHighlightLists(): List<ProductHighlightEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProductHighlightLists(list: List<ProductHighlightEntity>)

    @Query("DELETE FROM product_highlight_items")
    fun deleteAllProductHighlights()
}