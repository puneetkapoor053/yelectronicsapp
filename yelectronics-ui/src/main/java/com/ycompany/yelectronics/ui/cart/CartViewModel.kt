package com.ycompany.yelectronics.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ycompany.yelectronics.network.database.CartEntity
import com.ycompany.yelectronics.network.repository.CartRepository
import com.ycompany.yelectronics.viewmodel.StateLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {
    private val cartListLiveData: StateLiveData<List<CartEntity>> = StateLiveData()

    fun getAllCartItems() = viewModelScope.launch(Dispatchers.IO) {
        cartListLiveData.postLoading()
        try {
            cartListLiveData.postSuccess(cartRepository.getAllCartItems())
        } catch (exception: Exception) {
            cartListLiveData.postError(Throwable(exception))
        }
    }

    fun insert(product: CartEntity) = viewModelScope.launch(Dispatchers.IO) {
        val cartEntity = cartRepository.getCartItem(product.productId)
        if (cartEntity != null) {
            product.quantity += cartEntity.quantity
            cartRepository.updateCart(product)
        }else {
            cartRepository.insertToCart(product)
        }
    }

    fun deleteCart(product: CartEntity) = viewModelScope.launch(Dispatchers.IO) {
        cartRepository.deleteItemFromCart(product)
    }

    fun updateCart(product: CartEntity) = viewModelScope.launch(Dispatchers.IO) {
        cartRepository.updateCart(product)
    }

    fun cartListObservable(): StateLiveData<List<CartEntity>> {
        return cartListLiveData
    }
}