package com.ycompany.yelectronics.network.repository

import android.content.Context
import com.ycompany.yelectronics.network.database.ProductHighlightDao
import com.ycompany.yelectronics.network.dto.Product
import com.ycompany.yelectronics.network.usecase.ProductHighlightUseCase
import com.ycompany.yelectronics.network.utils.NetworkUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductHighlightRepository @Inject constructor(
    private val productHighlightUseCase: ProductHighlightUseCase,
    private val productHighlightDao: ProductHighlightDao
) {

    private var listOfHighlightProducts = listOf<Product>()

    private fun setListOfHighlightProducts(list: List<Product>) {
        listOfHighlightProducts = list
    }

    suspend fun getListOfHighlightProducts(context: Context): Any {
        return try {
            if (NetworkUtils.isDeviceOnline(context)) {
                return productHighlightUseCase.getProductHighlightList("CoverProducts.json")
            } else if (productHighlightDao.getAllProductHighlightLists().isNotEmpty()) {
                // No Internet but Data Present in the cache.
                return productHighlightDao.getAllProductHighlightLists().map {
                    Product.toDTO(it)
                }
            } else {
                Throwable("Please connect to the internet")
            }
        } catch (exception: Exception) {
            Throwable("Please connect to the internet")
        }
    }

    suspend fun getListOfProducts(context: Context): Any {
        return try {
            if (NetworkUtils.isDeviceOnline(context)) {
                return productHighlightUseCase.getProductHighlightList("NewProducts.json")
            } else if (productHighlightDao.getAllProductHighlightLists().isNotEmpty()) {
                // No Internet but Data Present in the cache.
                return productHighlightDao.getAllProductHighlightLists().map {
                    Product.toDTO(it)
                }
            } else {
                Throwable("Please connect to the internet")
            }
        } catch (exception: Exception) {
            Throwable("Please connect to the internet")
        }
    }
}