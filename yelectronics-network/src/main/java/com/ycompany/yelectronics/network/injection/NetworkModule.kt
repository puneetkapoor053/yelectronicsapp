package com.ycompany.yelectronics.network.injection

import android.content.Context
import android.content.res.AssetManager
import androidx.room.Room
import com.google.gson.Gson
import com.ycompany.yelectronics.network.database.AppDatabase
import com.ycompany.yelectronics.network.database.CartDao
import com.ycompany.yelectronics.network.database.FavoriteDao
import com.ycompany.yelectronics.network.database.ProductHighlightDao
import com.ycompany.yelectronics.network.database.ProductsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//This module will have al the OkHttp and retrofit dependencies when hitting the real server.
@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideGson() = Gson()

    @Provides
    @Singleton
    fun getAssets(@ApplicationContext context: Context): AssetManager {
        return context.assets
    }

    @Provides
    @Singleton
    fun getDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "app_products_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providesProductHighlightDao(appDatabase: AppDatabase): ProductHighlightDao {
        return appDatabase.productHighlightDao()
    }

    @Provides
    @Singleton
    fun providesProductsDao(appDatabase: AppDatabase): ProductsDao {
        return appDatabase.productsDao()
    }

    @Provides
    @Singleton
    fun providesFavoriteDao(appDatabase: AppDatabase): FavoriteDao {
        return appDatabase.favoriteDao()
    }

    @Provides
    @Singleton
    fun providesCartDao(appDatabase: AppDatabase): CartDao {
        return appDatabase.cartDao()
    }
}