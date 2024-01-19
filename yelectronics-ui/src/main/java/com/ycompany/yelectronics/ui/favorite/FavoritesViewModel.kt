package com.ycompany.yelectronics.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ycompany.yelectronics.network.dto.Product
import com.ycompany.yelectronics.network.repository.FavoritesRepository
import com.ycompany.yelectronics.network.repository.ProductsRepository
import com.ycompany.yelectronics.viewmodel.StateLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val productsRepository: ProductsRepository
) : ViewModel() {
    private val handleFavoritesLiveData: StateLiveData<Boolean> = StateLiveData()
    private val favoritesListLiveData: StateLiveData<List<Product>> = StateLiveData()

    fun isProductFavorite(productId: String) {
        var isIdPresent = false
        viewModelScope.launch(Dispatchers.IO) {
            val listOfFavorites = favoritesRepository.getListOfFavorites()
            for (fav in listOfFavorites) {
                if (fav.productId == productId) {
                    isIdPresent = true
                    break
                }
            }
            handleFavoritesLiveData.postSuccess(isIdPresent)
        }
    }

    fun addFavoriteProduct(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            favoritesRepository.insertFavorite(productId)
        }
    }

    fun removeFavoriteProduct(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            favoritesRepository.deleteFavorite(productId)
        }
    }

    fun getListOfFavorites() {
        favoritesListLiveData.postLoading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val listOfFavoriteIds = favoritesRepository.getListOfFavorites()
                val listOfProducts = productsRepository.getAllNewProducts()
                val listOfHighlightProducts = productsRepository.getAllProductHighlightLists()

                val listOfFavorites = arrayListOf<Product>()

                for (product in listOfProducts) {
                    for (favId in listOfFavoriteIds) {
                        if (product.productId == favId.productId) {
                            listOfFavorites.add(Product.toProductDTO(product))
                            break
                        }
                    }
                }
                for (product in listOfHighlightProducts) {
                    for (favId in listOfFavoriteIds) {
                        if (product.productId == favId.productId) {
                            listOfFavorites.add(Product.toHighlightDTO(product))
                            break
                        }
                    }
                }
                favoritesListLiveData.postSuccess(listOfFavorites)
            } catch (exception: Exception) {
                favoritesListLiveData.postError(Throwable(exception))
            }
        }
    }

    fun favoritesObservable(): StateLiveData<Boolean> {
        return handleFavoritesLiveData
    }

    fun favoritesListObservable(): StateLiveData<List<Product>> {
        return favoritesListLiveData
    }
}