package com.ycompany.yelectronics.network.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ProductHighlightEntity::class, ProductsEntity::class, FavoriteEntity::class,
        CartEntity::class, OrdersListEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class, DateConvertor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productHighlightDao(): ProductHighlightDao
    abstract fun productsDao(): ProductsDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun cartDao(): CartDao
}