package com.ycompany.network.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ycompany.yelectronics.network.database.FavoriteDao
import com.ycompany.yelectronics.network.database.FavoriteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteDaoTest : DaoTestBase() {

    private lateinit var favoriteDao: FavoriteDao

    @Throws(Exception::class)
    @Before
    override fun initDb() {
        super.initDb()
        favoriteDao = database.favoriteDao()
    }

    @Test
    fun testFavouritesData() = runBlocking{
        var data = favoriteDao.getAllFavoriteProducts()
        Assert.assertTrue(data.isEmpty())

        favoriteDao.insertFavoriteProduct(FavoriteEntity(productId = "123"))
        data = favoriteDao.getAllFavoriteProducts()
        Assert.assertTrue(data[0].productId == "123")

        favoriteDao.deleteAllFavoritesProducts()
        data = favoriteDao.getAllFavoriteProducts()
        Assert.assertTrue(data.isEmpty())

        favoriteDao.insertFavoriteProduct(FavoriteEntity(productId = "123"))
        favoriteDao.removeFavoriteProduct("123")
        Assert.assertTrue(data.isEmpty())
    }

}