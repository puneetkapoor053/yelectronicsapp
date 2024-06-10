package com.ycompany.yelectronics.network.repository

import android.content.Context
import com.ycompany.yelectronics.network.R
import com.ycompany.yelectronics.network.database.ProductHighlightDao
import com.ycompany.yelectronics.network.database.ProductHighlightEntity
import com.ycompany.yelectronics.network.database.ProductsDao
import com.ycompany.yelectronics.network.database.ProductsEntity
import com.ycompany.yelectronics.network.dto.Product
import com.ycompany.yelectronics.network.usecase.ProductsUseCase
import com.ycompany.yelectronics.network.utils.NetworkUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductsRepository @Inject constructor(
    private val productsUseCase: ProductsUseCase,
    private val productHighlightDao: ProductHighlightDao,
    private val productsDao: ProductsDao,
    @ApplicationContext private val context: Context
) {

    suspend fun getListOfHighlightProducts(): Any {
        return try {
            if (NetworkUtils.isDeviceOnline(context)) {
                return productsUseCase.getProductHighlightList("CoverProducts.json")
            }

            val cacheList = productHighlightDao.getAllProductHighlightLists()
            if (cacheList.isNotEmpty()) {
                // No Internet but Data Present in the cache.
                return cacheList.map {
                    Product.toHighlightDTO(it)
                }
            } else {
                Throwable(context.getString(R.string.please_connect_to_the_internet))
            }
        } catch (exception: Exception) {
            Throwable(context.getString(R.string.please_connect_to_the_internet))
        }
    }

    suspend fun getListOfNewProducts(): Any {
        return try {
            if (NetworkUtils.isDeviceOnline(context)) {
                return productsUseCase.getAllNewProducts("NewProducts.json")
            }

            val cacheList = productsDao.getAllNewProducts()
            if (cacheList.isNotEmpty()) {
                // No Internet but Data Present in the cache.
                return cacheList.map {
                    Product.toProductDTO(it)
                }
            } else {
                Throwable(context.getString(R.string.please_connect_to_the_internet))
            }
        } catch (exception: Exception) {
            Throwable(context.getString(R.string.please_connect_to_the_internet))
        }
    }

    suspend fun getAllNewProducts(): List<ProductsEntity> {
        return productsDao.getAllNewProducts()
    }

    suspend fun getAllProductHighlightLists(): List<ProductHighlightEntity> {
        return productHighlightDao.getAllProductHighlightLists()
    }
}