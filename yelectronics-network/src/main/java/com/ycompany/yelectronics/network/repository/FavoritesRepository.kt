package com.ycompany.yelectronics.network.repository

import com.ycompany.yelectronics.network.database.FavoriteDao
import com.ycompany.yelectronics.network.database.FavoriteEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepository @Inject constructor(
    private val favoriteDao: FavoriteDao
) {
    suspend fun getListOfFavorites(): List<FavoriteEntity> {
        return favoriteDao.getAllFavoriteProducts()
    }

    suspend fun insertFavorite(productId: String) {
        return favoriteDao.insertFavoriteProduct(FavoriteEntity.toEntity(productId))
    }

    suspend fun deleteFavorite(productId: String) {
        return favoriteDao.removeFavoriteProduct(productId)
    }
}