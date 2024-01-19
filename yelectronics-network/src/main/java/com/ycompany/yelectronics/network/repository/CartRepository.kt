package com.ycompany.yelectronics.network.repository

import androidx.lifecycle.LiveData
import com.ycompany.yelectronics.network.database.CartDao
import com.ycompany.yelectronics.network.database.CartEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(private val cartDao: CartDao) {

    suspend fun insertToCart(product: CartEntity) {
        cartDao.insert(product)
    }

    suspend fun deleteItemFromCart(product: CartEntity) {
        cartDao.delete(product)
    }

    suspend fun updateCart(product: CartEntity) {
        cartDao.update(product)
    }

    suspend fun getAllCartItems(): List<CartEntity> {
        return cartDao.getAllCartItems()
    }

    suspend fun getCartItem(productId: String): CartEntity? {
        return cartDao.getCartItem(productId)
    }
}