package com.ycompany.yelectronics.ui.home

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ycompany.yelectronics.network.dto.Product
import com.ycompany.yelectronics.network.repository.ProductsRepository
import com.ycompany.yelectronics.utils.Constants
import com.ycompany.yelectronics.viewmodel.StateLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val productsRepository: ProductsRepository
) : ViewModel() {

    private val productHighlightLiveData: StateLiveData<List<Product>> = StateLiveData()
    private val productsLiveData: StateLiveData<List<Product>> = StateLiveData()

    fun getUserName(): String {
        return if (sharedPreferences.getString(Constants.PREF_USERNAME, null)
                .equals(Constants.PREF_GUEST_USERNAME)
        ) {
            "Guest"
        } else {
            sharedPreferences.getString(Constants.PREF_USERNAME, null).toString()
        }
    }

    fun getProductHighlightList() {
        productHighlightLiveData.postLoading()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = productsRepository.getListOfHighlightProducts()
                productHighlightLiveData.postSuccess(result as List<Product>)
            } catch (exception: Exception) {
                productHighlightLiveData.postError(exception)
            }
        }
    }

    fun getNewProductsList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = productsRepository.getListOfNewProducts()
                productsLiveData.postSuccess(result as List<Product>)
            } catch (exception: Exception) {
                productsLiveData.postError(exception)
            }
        }
    }

    fun productHighlightObservable(): StateLiveData<List<Product>> {
        return productHighlightLiveData
    }

    fun productsObservable(): StateLiveData<List<Product>> {
        return productsLiveData
    }
}