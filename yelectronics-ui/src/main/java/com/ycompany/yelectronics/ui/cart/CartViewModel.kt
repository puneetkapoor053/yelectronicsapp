package com.ycompany.yelectronics.ui.cart

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ycompany.yelectronics.network.database.CartEntity
import com.ycompany.yelectronics.network.database.OrdersListEntity
import com.ycompany.yelectronics.network.repository.CartRepository
import com.ycompany.yelectronics.utils.Constants
import com.ycompany.yelectronics.viewmodel.StateLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    private val cartListLiveData: StateLiveData<List<CartEntity>> = StateLiveData()
    private val ordersListLiveData: StateLiveData<List<OrdersListEntity>> = StateLiveData()

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
        } else {
            cartRepository.insertToCart(product)
        }
    }

    fun getPaymentPayload(amount: Int): JSONObject {
        val email = sharedPreferences.getString(Constants.PREF_USERNAME, null).toString()
        // initialize json object
        val obj = JSONObject()
        try {
            obj.put("name", "Y Electronics")
            obj.put("description", "Test payment")
            obj.put("currency", "INR")
            obj.put("amount", amount)
            obj.put("prefill.contact", "9284064503")
            obj.put("prefill.email", email)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return obj
    }

    fun deleteCart(product: CartEntity) = viewModelScope.launch(Dispatchers.IO) {
        cartRepository.deleteItemFromCart(product)
    }

    fun deleteAllCart() = viewModelScope.launch(Dispatchers.IO) {
        cartRepository.deleteAllCartItems()
    }

    fun updateCart(product: CartEntity) = viewModelScope.launch(Dispatchers.IO) {
        cartRepository.updateCart(product)
    }

    fun insertOrders(order: OrdersListEntity) = viewModelScope.launch(Dispatchers.IO) {
        cartRepository.insertOrders(order)
    }

    fun getAllOrders() = viewModelScope.launch(Dispatchers.IO) {
        ordersListLiveData.postLoading()
        try {
            ordersListLiveData.postSuccess(cartRepository.getAllOrders())
        } catch (exception: Exception) {
            ordersListLiveData.postError(Throwable(exception))
        }
    }

    fun cartListObservable(): StateLiveData<List<CartEntity>> {
        return cartListLiveData
    }

    fun ordersListObservable(): StateLiveData<List<OrdersListEntity>> {
        return ordersListLiveData
    }
}