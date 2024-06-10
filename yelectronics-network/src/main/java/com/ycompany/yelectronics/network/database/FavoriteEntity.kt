package com.ycompany.yelectronics.network.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_items")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowId") val id: Int = 0,
    @ColumnInfo(name = "productId") val productId: String,
) {
    companion object {
        fun toEntity(favoriteProductId: String): FavoriteEntity {
            return FavoriteEntity(
                productId = favoriteProductId,
            )
        }
    }
}