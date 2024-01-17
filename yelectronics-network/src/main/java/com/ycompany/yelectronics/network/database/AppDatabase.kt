package com.ycompany.yelectronics.network.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProductHighlightEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productHighlightDao(): ProductHighlightDao
}