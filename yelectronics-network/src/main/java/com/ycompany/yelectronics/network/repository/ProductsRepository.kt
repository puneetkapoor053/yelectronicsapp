package com.ycompany.yelectronics.network.repository

import android.content.Context
import com.ycompany.yelectronics.network.database.ProductHighlightDao
import com.ycompany.yelectronics.network.database.ProductHighlightEntity
import com.ycompany.yelectronics.network.database.ProductsDao
import com.ycompany.yelectronics.network.database.ProductsEntity
import com.ycompany.yelectronics.network.dto.Product
import com.ycompany.yelectronics.network.usecase.ProductsUseCase
import com.ycompany.yelectronics.network.utils.NetworkUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductsRepository @Inject constructor(
    private val productsUseCase: ProductsUseCase,
    private val productHighlightDao: ProductHighlightDao,
    private val productsDao: ProductsDao
) {

    suspend fun getListOfHighlightProducts(context: Context): Any {
        return try {
            if (NetworkUtils.isDeviceOnline(context)) {
                return productsUseCase.getProductHighlightList("CoverProducts.json")
            } else if (productHighlightDao.getAllProductHighlightLists().isNotEmpty()) {
                // No Internet but Data Present in the cache.
                return productHighlightDao.getAllProductHighlightLists().map {
                    Product.toHighlightDTO(it)
                }
            } else {
                Throwable("Please connect to the internet")
            }
        } catch (exception: Exception) {
            Throwable("Please connect to the internet")
        }
    }

    suspend fun getListOfNewProducts(context: Context): Any {
        return try {
            if (NetworkUtils.isDeviceOnline(context)) {
                return productsUseCase.getAllNewProducts("NewProducts.json")
            } else if (productsDao.getAllNewProducts().isNotEmpty()) {
                // No Internet but Data Present in the cache.
                return productsDao.getAllNewProducts().map {
                    Product.toProductDTO(it)
                }
            } else {
                Throwable("Please connect to the internet")
            }
        } catch (exception: Exception) {
            Throwable(exception.message)
        }
    }

   suspend fun getAllNewProducts(): List<ProductsEntity> {
        return productsDao.getAllNewProducts()
    }

    suspend fun getAllProductHighlightLists(): List<ProductHighlightEntity> {
        return productHighlightDao.getAllProductHighlightLists()
    }
}