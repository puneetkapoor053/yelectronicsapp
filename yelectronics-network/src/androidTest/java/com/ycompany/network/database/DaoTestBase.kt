package com.ycompany.network.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ycompany.yelectronics.network.database.AppDatabase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.lang.Exception

abstract class DaoTestBase {

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    lateinit var database: AppDatabase

    @Throws(Exception::class)
    @Before
    open fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    @Throws(Exception::class)
    @After
    open fun closeDb() {
        database.close()
    }
}