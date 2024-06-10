package com.ycompany.network.repository

import android.content.Context
import com.ycompany.yelectronics.network.database.ProductHighlightDao
import com.ycompany.yelectronics.network.database.ProductHighlightEntity
import com.ycompany.yelectronics.network.database.ProductsDao
import com.ycompany.yelectronics.network.database.ProductsEntity
import com.ycompany.yelectronics.network.repository.ProductsRepository
import com.ycompany.yelectronics.network.usecase.ProductsUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class ProductsRepositoryTest {

    @Mock
    lateinit var productsUseCase: ProductsUseCase

    @Mock
    lateinit var productHighlightDao: ProductHighlightDao

    @Mock
    lateinit var productsDao: ProductsDao

    @Mock
    lateinit var context: Context

    private lateinit var productsRepository: ProductsRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        productsRepository =
            ProductsRepository(productsUseCase, productHighlightDao, productsDao, context)
    }

    @Test
    fun testGetAllNewProducts(): Unit = runBlocking {
        `when`(productsDao.getAllNewProducts()).thenReturn(getListOfProductsEntity())
        val list = productsRepository.getAllNewProducts()
        assertTrue(list.isNotEmpty())
    }

    @Test
    fun testGetAllProductHighlights(): Unit = runBlocking {
        `when`(productHighlightDao.getAllProductHighlightLists()).thenReturn(
            getListOfProductHighlightEntity()
        )
        val list = productsRepository.getAllProductHighlightLists()
        assertTrue(list.isNotEmpty())
    }

    private fun getListOfProductsEntity(): List<ProductsEntity> {
        val productsEntity: ProductsEntity = ProductsEntity(
            productName = "productName",
            productId = "productId",
            productPrice = "productPrice",
            productDes = "productDes",
            productRating = 4.0F,
            productDisCount = "productDisCount",
            productHave = true,
            productBrand = "productBrand",
            productImage = "productImage",
            productCategory = "productCategory",
            productNote = "productNote"
        )
        val list = arrayListOf<ProductsEntity>()
        list.add(productsEntity)
        return list
    }

    fun getListOfProductHighlightEntity(): List<ProductHighlightEntity> {
        val productsEntity: ProductHighlightEntity = ProductHighlightEntity(
            productName = "productName",
            productId = "productId",
            productPrice = "productPrice",
            productDes = "productDes",
            productRating = 4.0F,
            productDisCount = "productDisCount",
            productHave = true,
            productBrand = "productBrand",
            productImage = "productImage",
            productCategory = "productCategory",
            productNote = "productNote"
        )
        val list = arrayListOf<ProductHighlightEntity>()
        list.add(productsEntity)
        return list
    }

}