package com.ycompany.yelectronics.network.dto

import com.ycompany.yelectronics.network.database.CartEntity

data class Orders(
    var name: String,
    var quantity: Int,
    var price: Int,
    var productId: String,
    var productImage: String
) {
    companion object {
        fun toOrderList(cartEntity: CartEntity): Orders {
            return Orders(
                cartEntity.name,
                cartEntity.quantity,
                cartEntity.price,
                cartEntity.productId,
                cartEntity.productImage
            )
        }
    }
}
