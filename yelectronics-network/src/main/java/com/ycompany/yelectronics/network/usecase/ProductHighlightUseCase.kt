package com.ycompany.yelectronics.network.usecase

import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ycompany.yelectronics.network.database.ProductHighlightDao
import com.ycompany.yelectronics.network.database.ProductHighlightEntity
import com.ycompany.yelectronics.network.dto.Product
import kotlinx.coroutines.delay
import java.io.IOException
import javax.inject.Inject

class ProductHighlightUseCase @Inject constructor(
    private val gson: Gson, private val assetManager: AssetManager,
    private val productHighlightDao: ProductHighlightDao
) {

    suspend fun getProductHighlightList(fileName: String): List<Product> {
        val listCoverType = object : TypeToken<List<Product>>() {}.type
        val list: List<Product> = gson.fromJson(getJsonData(fileName), listCoverType)
        val listEntity: List<ProductHighlightEntity> =
            list.map { ProductHighlightEntity.toEntity(it) }
        // Storing the details in the DB
        productHighlightDao.deleteAllProductHighlights()

        productHighlightDao.insertAllProductHighlightLists(listEntity)
        return list
    }

    private suspend fun getJsonData(fileName: String): String? {
        // Delay added just to show the progress running in case of device connected to the internet.
        delay(500)
        val jsonString: String
        try {
            jsonString = assetManager.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}