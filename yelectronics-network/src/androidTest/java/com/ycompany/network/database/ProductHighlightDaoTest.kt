package com.ycompany.network.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ycompany.yelectronics.network.database.ProductHighlightDao
import com.ycompany.yelectronics.network.database.ProductHighlightEntity
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductHighlightDaoTest : DaoTestBase() {

    private lateinit var productHighlightDao: ProductHighlightDao

    @Throws(Exception::class)
    @Before
    override fun initDb() {
        super.initDb()
        productHighlightDao = database.productHighlightDao()
    }


    @Test
    fun testFavouritesData() = runBlocking {
        var data = productHighlightDao.getAllProductHighlightLists()
        Assert.assertTrue(data.isEmpty())

        productHighlightDao.insertAllProductHighlightLists(getListOfProductHighlightEntity())
        data = productHighlightDao.getAllProductHighlightLists()
        Assert.assertTrue(data[0].productBrand == "productBrand1")

        productHighlightDao.deleteAllProductHighlights()
        data = productHighlightDao.getAllProductHighlightLists()
        Assert.assertTrue(data.isEmpty())
    }

    private fun getListOfProductHighlightEntity(): List<ProductHighlightEntity> {
        val productsEntity: ProductHighlightEntity = ProductHighlightEntity(
            productName = "productName",
            productId = "productId",
            productPrice = "productPrice",
            productDes = "productDes",
            productRating = 4.0F,
            productDisCount = "productDisCount",
            productHave = true,
            productBrand = "productBrand1",
            productImage = "productImage",
            productCategory = "productCategory",
            productNote = "productNote"
        )
        val list = arrayListOf<ProductHighlightEntity>()
        list.add(productsEntity)
        return list
    }
}